/*
 * Copyright 2015 Eiren 'Eirenliel' Rain and GreenCubes.org
 * authors
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall
 * be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE
 * OR OTHER DEALINGS IN THE SOFTWARE.
 */
package net.minecraft.src;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.greencubes.util.Configuration;
import org.greencubes.util.ConfigurationNode;
import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;

public class GChat {

	private static final int SHIFT = 1;
	private static final int CTRL = 2;
	private static final int ALT = 4;
	private static final Pattern objectPattern = Pattern.compile("\247o(.+)\247o(.+)\247o");
	private static final Pattern colorPattern = Pattern.compile("(\247[0-9a-fA-F]|\247[usip]|\247r[0-9a-z]{8})", Pattern.CASE_INSENSITIVE);
	private static final Pattern empty = Pattern.compile("^[ ]*$");
	private static final Pattern channelPattern = Pattern.compile("\247n");
	private static final Pattern channelColorPattern = Pattern.compile("\247w");
	public static final String defaultColor = "f";
	public static final char colorChar = '\247';
	public static final int chatWidth = 320;
	public static final int chatWindowWidth = 315;
	private static final StringBuilder emptySb = new StringBuilder(0);
	private static final Pattern commandpattern = Pattern.compile("񘿿񘿹([^|]*)\\|?(.*)");
	private static final String newline = System.getProperty("line.separator");
	public static final float tabWidth = 152 / 2;

	public static Minecraft mc;
	private static final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	public final int dotsWidth;

	public int lines = 20;
	public boolean locked = false;
	public final Map<Integer, String>[] bindings = new Map[7];
	public final List<String> lastMessagesSent = new ArrayList<String>();
	private int lastMessagesIndex = -1;
	private final OutputStreamWriter[] logs;
	public final Map<Integer, GChannel> channels = new HashMap<Integer, GChannel>();
	public final List<GTab> tabs = new ArrayList<GTab>();
	public final List<String> pastCommands = new ArrayList<String>();
	public int commandScroll = 0;
	public GTab mainTab;
	public int chatScroll = 0;
	public int maxHistoryPerTab = 200;
	public int bgColor = 0x66000000;
	public int tabScroll = 0;
	public GTab activeTab;
	public int activeTabIndex = 0;
	public Configuration config;
	public File chatDir;
	private int nextId = 1;

	public KeyBinding flyKey;
	public KeyBinding speedKey;
	public float flySpeed = -1.0f;
	public float speedSpeed = -1.0f;

	public GChat(Minecraft mc) throws IOException {
		GChat.mc = mc;
		dotsWidth = mc.fontRenderer.getStringWidth("...");
		chatDir = new File(Minecraft.getMinecraftDir(), "chat/");
		if(!chatDir.exists())
			chatDir.mkdirs();
		if(!chatDir.isDirectory())
			throw new IOException("Chat folder is not directory!");
		File logFile = new File(chatDir, "full_chat.log");
		if(!logFile.exists())
			logFile.createNewFile();
		logs = new OutputStreamWriter[3];
		logs[0] = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(logFile, true)), Charset.forName("UTF-8"));
		logFile = new File(chatDir, "last_chat.log");
		if(logFile.exists())
			logFile.delete();
		logFile.createNewFile();
		logs[1] = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(logFile, true)), Charset.forName("UTF-8"));
		File logDir = new File(chatDir, "logs");
		if(!logDir.exists())
			logDir.mkdirs();
		logFile = new File(logDir, GCUtil.fileDateFormat.format(new Date()) + ".log");
		if(!logFile.exists())
			logFile.createNewFile();
		logs[2] = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(logFile, true)), Charset.forName("UTF-8"));
		for(int i = 0; i < bindings.length; ++i)
			bindings[i] = new HashMap<Integer, String>();
		File configFile = new File(chatDir, "config.yml");
		boolean shouldSave = false;
		if(!configFile.exists()) {
			configFile.createNewFile();
			FileWriter fw = new FileWriter(configFile);
			fw.write("node: node");
			fw.close();
			shouldSave = true;
		}
		config = new Configuration(configFile);
		try {
			config.load();
		} catch (Exception e) {
			e.printStackTrace();
			shouldSave = true;
		}
		lines = config.getInt("max-lines", 20);
		if(lines < 1)
			lines = 20;
		locked = config.getBoolean("no-hide", false);
		maxHistoryPerTab = config.getInt("max-chat-history", 200);
		if(maxHistoryPerTab < 1)
			maxHistoryPerTab = 200;
		bgColor = Integer.parseInt(config.getString("bg-color", "40000000"), 16);
		List<ConfigurationNode> nodes = config.getNodeList("tabs", new ArrayList<ConfigurationNode>());
		boolean hasMain = false;
		for(int i = 0; i < nodes.size(); ++i) {
			ConfigurationNode node = nodes.get(i);
			String name = node.getString("name");
			int position = node.getInt("position", -1);
			boolean isSystem = !hasMain && node.getBoolean("main", false);
			if(name != null && position >= 0) {
				GTab tab = new GTab(this, name);
				tab.isSystem = isSystem;
				if(isSystem) {
					hasMain = true;
					mainTab = tab;
					activeTab = tab;
				}
				tabs.add(position, tab);
				tab.readyToJoin = node.getIntList("channels", new ArrayList<Integer>());
				if(tab.isSystem)
					tab.generalLeaved = node.getIntList("leaved", new ArrayList<Integer>());
			}
		}
		String c = config.getString("flyKey", null);
		if(c != null)
			flyKey = new KeyBinding("", Keyboard.getKeyIndex(c));
		c = config.getString("speedKey", null);
		if(c != null)
			speedKey = new KeyBinding("", Keyboard.getKeyIndex(c));
		double d = config.getDouble("flySpeed", -1);
		if(d != -1)
			flySpeed = (float) d;
		d = config.getDouble("speedSpeed", -1);
		if(d != -1)
			speedSpeed = (float) d;
		for(int i = 0; i < tabs.size(); ++i)
			if(tabs.get(i) == null)
				tabs.remove(i--);
		if(!hasMain) {
			shouldSave = true;
			GTab tab = new GTab(this, "Main");
			tab.isSystem = true;
			mainTab = tab;
			activeTab = tab;
			tabs.add(0, tab);
		}
		nodes = config.getNodeList("binds", new ArrayList<ConfigurationNode>());
		for(int i = 0; i < nodes.size(); ++i) {
			ConfigurationNode node = nodes.get(i);
			int sub = node.getInt("sub-key", -1);
			int key = node.getInt("key", -1);
			String command = node.getString("command");
			if(sub >= 0 && sub <= 7 && Keyboard.getKeyName(key) != null && command != null)
				bindings[sub].put(key, command);
		}
		if(shouldSave)
			saveConfig();
	}

	public void resetChannels() {
		channels.clear();
		for(int i = 0; i < tabs.size(); ++i) {
			tabs.get(i).readyToJoin.addAll(tabs.get(i).attachedChannels.keySet());
			tabs.get(i).attachedChannels.clear();
			tabs.get(i).activeChannel = null;
		}
	}

	public StringBuilder processKeyInput(StringBuilder message, int cursor) {
		if(message.length() > 1 && message.charAt(0) == '/') {
			String[] split = message.toString().split(" ");
			if(split.length > 0 && split[0].length() < cursor) {
				String ali = split[0].substring(1);
				Collection<GChannel> channels = activeTab.attachedChannels.values();
				Iterator<GChannel> i = channels.iterator();
				while(i.hasNext()) {
					GChannel c = i.next();
					for(int n = 0; n < c.aliases.size(); ++n)
						if(c.aliases.get(n).equalsIgnoreCase(ali)) {
							activeTab.activeChannel = c;
							activeTab.prefix = c.name + ":";
							activeTab.sendPrefix = null;
							return new StringBuilder(message.substring(ali.length() + 2, message.length()));
						}
				}
				if(ali.equalsIgnoreCase("w")) {
					activeTab.prefix = this.channels.get(2).name + ":";
					activeTab.sendPrefix = null;
					activeTab.activeChannel = this.channels.get(2);
					return new StringBuilder(message.substring(ali.length() + 2, message.length()));
				} else if(ali.equalsIgnoreCase("m")) {
					if(split.length > 1 && cursor > split[0].length() + split[1].length() + 1) {
						activeTab.prefix = "PM to " + split[1] + ":";
						activeTab.sendPrefix = "/m " + split[1] + " ";
						return new StringBuilder(message.substring(ali.length() + 3 + split[1].length(), message.length()));
					}
				}
			}
		}
		return message;
	}

	public List<StringBuilder> processChatDisplay(CharSequence line) {
		List<StringBuilder> list = new ArrayList<StringBuilder>();
		float lineWidth = 0;
		StringBuilder nextLine = new StringBuilder();
		for(int i = 0; i < line.length(); ++i) {
			char c = line.charAt(i);
			int id = ChatAllowedCharacters.allowedCharacters.indexOf(c);
			if(id >= 0) {
				if(lineWidth + mc.fontRenderer.charWidth[id + 32] <= chatWidth) {
					lineWidth += mc.fontRenderer.charWidth[id + 32];
					nextLine.append(c);
				} else {
					list.add(nextLine);
					lineWidth = mc.fontRenderer.charWidth[id + 32];
					nextLine = new StringBuilder();
					nextLine.append(c);
				}
			}
		}
		if(nextLine.length() > 0)
			list.add(nextLine);
		if(list.size() == 0)
			list.add(emptySb);
		return list;
	}

	private boolean executeScript(String name) {
		File f = new File(chatDir, name);
		Scanner reader;
		try {
			reader = new Scanner(f, "UTF-8");
		} catch (FileNotFoundException e) {
			return false;
		}
		try {
			while(reader.hasNextLine()) {
				String command = reader.nextLine();
				command = processKeyInput(new StringBuilder(command), command.length()).toString();
				if(command.endsWith("\\")) {
					GuiChat chat = new GuiChat(false);
					mc.displayGuiScreen(chat);
					chat.message = processKeyInput(new StringBuilder(command.substring(0, command.length() - 1)), command.length() - 1);
					chat.cursorPosition = chat.message.length();
					return true;
				}
				processOutput(command);
			}
		} finally {
			reader.close();
		}
		return true;
	}

	public void saveConfig() {
		File configFile = new File(chatDir, "config.yml");
		try {
			configFile.delete();
			configFile.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		config = new Configuration(configFile);
		config.setProperty("max-lines", lines);
		config.setProperty("no-hide", locked);
		config.setProperty("max-chat-history", maxHistoryPerTab);
		config.setProperty("bg-color", Integer.toHexString(bgColor));
		List<Object> nodes = new ArrayList<Object>();
		for(int i = 0; i < tabs.size(); ++i) {
			Map<String, Object> tabNode = new HashMap<String, Object>();
			GTab tab = tabs.get(i);
			tabNode.put("position", i);
			tabNode.put("name", tab.name);
			if(tab.isSystem)
				tabNode.put("main", tab.isSystem);
			List<Integer> channels = new ArrayList<Integer>();
			for(int n = 0; n < tab.readyToJoin.size(); ++n)
				channels.add(tab.readyToJoin.get(n));
			channels.addAll(tab.attachedChannels.keySet());
			if(channels.size() > 0)
				tabNode.put("channels", channels);
			if(tab.isSystem && tab.generalLeaved.size() > 0)
				tabNode.put("leaved", tab.generalLeaved);
			nodes.add(tabNode);
		}
		config.setProperty("tabs", nodes);
		nodes = new ArrayList<Object>();
		for(int i = 0; i < bindings.length; ++i) {
			Map<Integer, String> bindMap = bindings[i];
			Iterator<Entry<Integer, String>> iterator = bindMap.entrySet().iterator();
			while(iterator.hasNext()) {
				Entry<Integer, String> e = iterator.next();
				Map<String, Object> node = new HashMap<String, Object>();
				node.put("sub-key", i);
				node.put("key", e.getKey());
				node.put("command", e.getValue());
				nodes.add(node);
			}
		}
		config.setProperty("binds", nodes);
		if(flyKey != null)
			config.setProperty("flyKey", Keyboard.getKeyName(flyKey.keyCode));
		if(speedKey != null)
			config.setProperty("speedKey", Keyboard.getKeyName(speedKey.keyCode));
		if(speedSpeed > 0)
			config.setProperty("speedSpeed", speedSpeed);
		if(flySpeed > 0)
			config.setProperty("flySpeed", speedSpeed);
		config.save();
	}

	public void logMessage(String message, String channel) {
		try {
			message = objectPattern.matcher(message).replaceAll("$1");
			message = colorPattern.matcher(message).replaceAll("");
			for(OutputStreamWriter log : logs) {
				log.append(GCUtil.dateFormat.format(new Date()));
				log.append(" [");
				log.append(channel);
				log.append("] ");
				log.append(message);
				log.append(newline);
				log.flush();
			}
			//System.out.println("[" + channel + "] " + message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void tick() {
		for(int i = 0; i < tabs.size(); ++i) {
			GTab tab = tabs.get(i);
			for(int n = 0; n < tab.lines.size(); ++n)
				if(tab.lines.get(n).age++ > 200)
					break;
		}
		/*if(Minecraft.theMinecraft.thePlayer != null && flyKey != null && speedKey != null)
			if(Minecraft.theMinecraft.thePlayer.canFly()) {
				if(Keyboard.isKeyDown(flyKey.keyCode))
					Minecraft.theMinecraft.thePlayer.inFlyMode = !Minecraft.theMinecraft.thePlayer.inFlyMode;
				if(flySpeed > 0 && speedSpeed > 0) {
					if(!Keyboard.isKeyDown(speedKey.keyCode) && Minecraft.theMinecraft.thePlayer.inFlyMode)
						Minecraft.theMinecraft.thePlayer.speedMultipler = flySpeed;
					else if(Keyboard.isKeyDown(speedKey.keyCode))
						Minecraft.theMinecraft.thePlayer.speedMultipler = speedSpeed;
					else
						Minecraft.theMinecraft.thePlayer.speedMultipler = 1.0F;
				}
			}*/
	}

	public boolean pressKey(int key) {
		int sub = 0;
		if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL))
			sub += CTRL;
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
			sub += SHIFT;
		String command = bindings[sub].get(Integer.valueOf(key));
		if(command != null) {
			command = processKeyInput(new StringBuilder(command), command.length()).toString();
			if(command.endsWith("\\")) {
				GuiChat chat = new GuiChat(false);
				mc.displayGuiScreen(chat);
				chat.message = processKeyInput(new StringBuilder(command.substring(0, command.length() - 1)), command.length() - 1);
				chat.cursorPosition = chat.message.length();
				return true;
			}
			processOutput(command);
			return true;
		}
		return false;
	}

	public void processInput(String message, int channel) {
		GChannel c = channels.get(channel);
		if(!ModLoader.onMessageRecieved(message, c))
			return;
		String test = colorPattern.matcher(message).replaceAll("");
		if(empty.matcher(test).matches())
			return;
		if(c == null) {
			logMessage(message, "SYSTEM");
			String[] lines = wrapText(message, chatWindowWidth, 0, false);
			for(int n = 0; n < lines.length; ++n) {
				mainTab.addNewLine(lines[n]).canWide = n != lines.length - 1;
				if(activeTab != mainTab)
					activeTab.addNewLine(lines[n]).canWide = n != lines.length - 1;
			}
		} else {
			message = channelPattern.matcher(message).replaceAll(Integer.toString(c.userId) + "." + c.name);
			message = channelColorPattern.matcher(message).replaceAll(c.color == null ? "" : "\247" + c.color);
			logMessage(message, c.name);
			String[] lines = wrapText(message, chatWindowWidth, 0, false);
			for(int i = 0; i < c.attachedTabs.size(); ++i) {
				for(int n = 0; n < lines.length; ++n) {
					c.attachedTabs.get(i).addNewLine(lines[n]).canWide = n != lines.length - 1;
					if(c.attachedTabs.get(i) != activeTab && (c.type != GChannel.Type.SYSTEM || !c.attachedTabs.get(i).isSystem))
						c.attachedTabs.get(i).hasNewMessage = true;
				}
			}
			if(c.type == GChannel.Type.SYSTEM && !c.attachedTabs.contains(mainTab))
				for(int n = 0; n < lines.length; ++n)
					mainTab.addNewLine(lines[n]).canWide = n != lines.length - 1;
		}
	}

	public void processOutput(String message) {
		if(empty.matcher(message).matches())
			return;
		if(message.startsWith("~")) {
			message = message.substring(1);
			String[] split = message.split(" ");
			if(split[0].equals("bind")) {
				if(split.length < 3) {
					activeTab.addNewLine("锐镱朦珙忄龛�: ~bind <觌噔桫�> <觐爨礓�...>");
				} else {
					String command = join(split, " ", 2);
					int sub = 0;
					split[1] = split[1].toLowerCase();
					if(split[1].contains("shift-")) {
						sub += SHIFT;
						split[1] = split[1].replace("shift-", "");
					}
					if(split[1].contains("ctrl-")) {
						sub += CTRL;
						split[1] = split[1].replace("ctrl-", "");
					}
					if(split[1].contains("alt-")) {
						sub += ALT;
						split[1] = split[1].replace("alt-", "");
					}
					int key = Keyboard.getKeyIndex(split[1].toUpperCase());
					if(key == 0) {
						activeTab.addNewLine("雨噻囗磬� 觌噔桫� 礤 磬殇屙�!");
					} else {
						bindings[sub].put(key, command);
						saveConfig();
						activeTab.addNewLine("皖忸� 耦麇蜞龛� 觌噔桫 漕徉怆屙�!");
					}
				}
			} else if(split[0].equals("script")) {
				if(split.length < 2) {
					activeTab.addNewLine("锐镱朦珙忄龛�: ~script <桁� 羿殡�...>");
				} else {
					String file = join(split, " ", 1);
					if(!executeScript(file)) {
						activeTab.addNewLine("\2474脏殡 礤 磬殇屙!");
					}
				}
			} else if(split[0].equals("unbind")) {
				if(split.length < 2) {
					activeTab.addNewLine("锐镱朦珙忄龛�: ~unbind <觌噔桫�>");
				} else {
					int sub = 0;
					split[1] = split[1].toLowerCase();
					if(split[1].contains("shift-")) {
						sub += SHIFT;
						split[1] = split[1].replace("shift-", "");
					}
					if(split[1].contains("ctrl-")) {
						sub += CTRL;
						split[1] = split[1].replace("ctrl-", "");
					}
					int key = Keyboard.getKeyIndex(split[1].toUpperCase());
					if(key == 0) {
						activeTab.addNewLine("雨噻囗磬� 觌噔桫� 礤 磬殇屙�!");
					} else {
						bindings[sub].remove(key);
						saveConfig();
						activeTab.addNewLine("杨麇蜞龛� 觌噔桫 箐嚯屙�!");
					}
				}
			} else if(split[0].equals("binds")) {
				activeTab.addNewLine("义牦� 徼礓�:");
				for(int sub = 0; sub < bindings.length; ++sub) {
					Map<Integer, String> binds = bindings[sub];
					Iterator<Entry<Integer, String>> iterator = binds.entrySet().iterator();
					while(iterator.hasNext()) {
						Entry<Integer, String> e = iterator.next();
						StringBuilder sb = new StringBuilder();
						sb.append("\247b");
						if((sub & SHIFT) == SHIFT)
							sb.append("SHIFT-");
						if((sub & ALT) == ALT)
							sb.append("ALT-");
						if((sub & CTRL) == CTRL)
							sb.append("CTRL-");
						sb.append(org.lwjgl.input.Keyboard.getKeyName(e.getKey()));
						sb.append("\247f: ");
						sb.append(e.getValue());
						for(String s: wrapText(sb.toString(), chatWindowWidth, 0, false))
							activeTab.addNewLine(s);
					}
				}
			}
		} else {
			if(activeTab.sendPrefix != null && !message.startsWith("/"))
				message = activeTab.sendPrefix + message;
			if(!mc.isMultiplayerWorld()) {
				if(!ModLoader.onMessageSent(message, null))
					return;
				mc.thePlayer.sendChatMessage(message);
				return;
			}
			if(!ModLoader.onMessageSent(message, activeTab.activeChannel))
				return;
			mc.getSendQueue().addToSendQueue(new Packet3Chat(message, activeTab.activeChannel != null ? activeTab.activeChannel.id : 1));
		}
	}

	public static void copy(String s) {
		clipboard.setContents(new StringSelection(s), null);
	}

	public static String paste() {
		String s = "";
		Transferable transferable = clipboard.getContents(null);
		if(transferable != null && transferable.isDataFlavorSupported(DataFlavor.stringFlavor))
			try {
				s = (String) transferable.getTransferData(DataFlavor.stringFlavor);
			} catch (UnsupportedFlavorException unsupportedflavorexception) {
			} catch (IOException ioexception) {
			}
		return s;
	}

	public void handleChannel(Packet204Channel packet) {
		if(packet.update) {
			GChannel c = channels.get(packet.channelId);
			if(c != null) {
				c.name = packet.channelName;
				c.type = GChannel.Type.getById(packet.type);
				return;
			}
			c = new GChannel(nextId++, packet.channelId, packet.channelName, GChannel.Type.getById(packet.type));
			channels.put(c.id, c);
			if(packet.channelAlias.length() > 0)
				c.aliases.add(packet.channelAlias);
			c.aliases.add(Integer.toString(c.userId));
			boolean attached = false;
			for(int i = 0; i < tabs.size(); ++i) {
				GTab tab = tabs.get(i);
				if(tab.readyToJoin.contains(packet.channelId)) {
					tab.attachedChannels.put(c.id, c);
					tab.readyToJoin.remove(Integer.valueOf(c.id));
					c.attachedTabs.add(tab);
					attached = true;
				} else if(tab.isSystem) {
					if(c.type == GChannel.Type.SYSTEM)
						tab.attachedChannels.put(c.id, c);
					if(c.type == GChannel.Type.GENERAL && !tab.generalLeaved.contains(c.id)) {
						tab.attachedChannels.put(c.id, c);
						c.attachedTabs.add(tab);
					}
					attached = true;
				}
			}
			if(!attached) {
				activeTab.attachedChannels.put(c.id, c);
				c.attachedTabs.add(activeTab);
			}
		} else {
			GChannel c = channels.get(packet.channelId);
			if(c != null) {
				channels.remove(c.id);
				for(int i = 0; i < c.attachedTabs.size(); ++i) {
					c.attachedTabs.get(i).attachedChannels.remove(c.id);
					if(c.attachedTabs.get(i).activeChannel == c) {
						c.attachedTabs.get(i).activeChannel = null;
						c.attachedTabs.get(i).prefix = null;
						c.attachedTabs.get(i).sendPrefix = null;
					}
				}
			}
		}
		saveConfig();
	}

	public void handleChat(Packet3Chat packet) {
		if(!processCommands(packet.message))
			processInput(packet.message, packet.channel);
	}

	private boolean processCommands(String string) {
		Matcher matcher = commandpattern.matcher(string);
		if(matcher.find()) {
			String type = matcher.group(1);
			String args = matcher.group(2);
			processCommand(type, args.split("[|]"));
			return true;
		}
		return false;
	}

	private void processCommand(String command, String[] split) {
		if(command.equals("canfly"))
			if(mc.getSendQueue() != null) {
				mc.getSendQueue().canFly = split.length > 0 && split[0].equals("true");
				System.out.println("Flymode allowed");
			}
	}

	public void previosTab() {
		if(activeTabIndex == 0)
			return;
		activeTabIndex--;
		activeTab = tabs.get(activeTabIndex);
		activeTab.hasNewMessage = false;
		if(mc.ingameGUI.inSettings)
			mc.ingameGUI.inSettings = false;
	}

	public void nextTab() {
		if(activeTabIndex == tabs.size() - 1)
			return;
		activeTabIndex++;
		activeTab = tabs.get(activeTabIndex);
		activeTab.hasNewMessage = false;
		if(mc.ingameGUI.inSettings)
			mc.ingameGUI.inSettings = false;
	}

	public static String[] wrapText(final String message, int width, int padding, boolean wrappWords) {
		if(!wrappWords)
			return wrapTextNoWords(message, width, padding);
		String currentColor = defaultColor;
		String nextColor = defaultColor;
		boolean underline = false;
		boolean stroke = false;
		boolean italic = false;
		boolean reset = false;
		float lineWidth = padding;
		boolean colorAppended = true;
		StringBuilder text = new StringBuilder();
		for(int n = 0; n < message.length(); ++n) {
			char c = message.charAt(n);
			if(c == colorChar) {
				if(n < message.length() - 1) {
					int colorSymbols = 1;
					c = Character.toLowerCase(message.charAt(n + 1));
					if("0123456789abcdef".indexOf(c) != -1) {
						nextColor = Character.toString(c);
					} else if(c == 'u') {
						underline = true;
					} else if(c == 's') {
						stroke = true;
					} else if(c == 'i') {
						italic = true;
					} else if(c == 'p') {
						underline = false;
						italic = false;
						stroke = false;
						reset = true;
					} else if(c == 'r') {
						colorSymbols += 8;
						if(n + 8 < message.length() - 1) {
							nextColor = message.substring(n + 1, n + 1 + 9);
						} else
							break;
					}
					colorAppended = false;
					n += colorSymbols;
					continue;
				} else
					break;
			}
			if(c == '\n' && n < message.length() - 1) {
				text.append('\n');
				lineWidth = 0;
				colorAppended = false;
				currentColor = defaultColor;
			}
			int id = ChatAllowedCharacters.allowedCharacters.indexOf(c);
			if(id != -1) {
				float charWidth = mc.fontRenderer.charWidth[id + 32];
				if(charWidth + lineWidth > width) {
					lineWidth = 0;
					text.append('\n');
					colorAppended = false;
					currentColor = defaultColor;
				}
				if(!colorAppended) {
					if(!currentColor.equals(nextColor)) {
						currentColor = nextColor;
						text.append(colorChar).append(nextColor);
					}
					if(reset) {
						reset = false;
						text.append(colorChar).append('p');
					}
					if(stroke)
						text.append(colorChar).append('s');
					if(italic)
						text.append(colorChar).append('i');
					if(underline)
						text.append(colorChar).append('u');
					colorAppended = true;
				}
				text.append(c);
				lineWidth += charWidth;
			}
		}
		return text.toString().split("\n");
	}

	private static String[] wrapTextNoWords(final String message, int width, int padding) {
		StringBuilder text = new StringBuilder();
		StringBuilder word = new StringBuilder();
		String currentWordColor = defaultColor;
		String nextWordColor = defaultColor;
		String currentLineColor = defaultColor;
		StringBuilder currentWordFormatting = new StringBuilder();
		StringBuilder currentLineFormatting = new StringBuilder();
		boolean reset = false;
		float lineWidth = padding;
		float wordWidth = 0;
		final float spaceWidth = mc.fontRenderer.charWidth[32];
		boolean hasSymbols = false;
		boolean colorAppended = false;
		boolean formatAppended = false;
		for(int n = 0; n < message.length(); ++n) {
			char c = message.charAt(n);
			if(c == colorChar) {
				if(n < message.length() - 1) {
					int colorSymbols = 1;
					c = Character.toLowerCase(message.charAt(n + 1));
					if("0123456789abcdef".indexOf(c) != -1) {
						nextWordColor = Character.toString(c);
						colorAppended = false;
					} else if(c == 'u') {
						currentWordFormatting.append(colorChar).append('u');
						formatAppended = false;
					} else if(c == 's') {
						currentWordFormatting.append(colorChar).append('s');
						formatAppended = false;
					} else if(c == 'i') {
						currentWordFormatting.append(colorChar).append('i');
						formatAppended = false;
					} else if(c == 'p') {
						currentWordFormatting = new StringBuilder();
						reset = true;
					} else if(c == 'r') {
						colorSymbols += 8;
						if(n + 8 < message.length() - 1) {
							nextWordColor = message.substring(n + 1, n + 1 + 9);
							colorAppended = false;
						} else
							break;
					}
					n += colorSymbols;
					continue;
				} else
					break;
			}
			int id = ChatAllowedCharacters.allowedCharacters.indexOf(c);
			if(id != -1) {
				float charWidth = mc.fontRenderer.charWidth[id + 32];
				if(charWidth + wordWidth > width) {
					// TODO: Do something!
					text.append(word);
					text.append('\n');
					lineWidth = 0;
					if(!currentLineColor.equals(defaultColor))
						text.append(colorChar).append(currentLineColor);
					if(currentLineFormatting.length() > 0) {
						text.append(currentLineFormatting);
						currentLineFormatting = new StringBuilder();
					}
					currentLineColor = defaultColor;
					colorAppended = false;
					formatAppended = false;
					wordWidth = 0;
					word = new StringBuilder();
					currentWordColor = defaultColor;
					currentWordFormatting = new StringBuilder();
				}
				if(!colorAppended) {
					if(!currentWordColor.equals(nextWordColor)) {
						currentWordColor = nextWordColor;
						word.append(colorChar).append(nextWordColor);
					}
					colorAppended = true;
				}
				if(reset && word.length() != 0) {
					word.append(colorChar).append('p');
					reset = false;
				}
				if(!formatAppended && currentWordFormatting.length() != 0) {
					word.append(currentWordFormatting);
					formatAppended = true;
				}
				word.append(c);
				wordWidth += charWidth;
			}
			boolean newlined = text.length() == 0;
			if(c == ' ' || c == '\n' || n == message.length() - 1) {
				if(lineWidth + wordWidth > width) {
					newlined = true;
					lineWidth = 0;
					if(text.charAt(text.length() - 1) == ' ')
						text.deleteCharAt(text.length() - 1);
					text.append('\n');
					if(!currentLineColor.equals(defaultColor))
						text.append(colorChar).append(currentLineColor);
					if(currentLineFormatting.length() > 0) {
						text.append(currentLineFormatting);
						currentLineFormatting = new StringBuilder();
					}
					currentLineColor = defaultColor;
				}
				text.append(word);
				word = new StringBuilder();
				lineWidth += wordWidth;
				wordWidth = 0;
				currentLineColor = currentWordColor;
				currentLineFormatting = currentWordFormatting;
			}
			if(!newlined && c == '\n' && n < message.length() - 1) {
				if(text.charAt(text.length() - 1) == ' ')
					text.deleteCharAt(text.length() - 1);
				text.append('\n');
				lineWidth = 0;
				if(!currentLineColor.equals(defaultColor))
					text.append(colorChar).append(currentLineColor);
				if(currentLineFormatting.length() > 0) {
					text.append(currentLineFormatting);
					currentLineFormatting = new StringBuilder();
				}
				currentLineColor = defaultColor;
			}
		}
		if(word.length() > 0) {
			boolean newlined = text.length() == 0;
			if(lineWidth + wordWidth + spaceWidth > width) {
				newlined = true;
				text.append('\n');
				if(!currentLineColor.equals(defaultColor))
					text.append(colorChar).append(currentLineColor);
				if(currentLineFormatting.length() > 0)
					text.append(currentLineFormatting);
			}
			text.append(word);
		}
		return text.toString().split("\n");
	}

	public final static String join(String[] split) {
		return join(split, "");
	}

	public final static String join(String[] split, String glue) {
		return join(split, glue, 0);
	}

	public final static String join(String[] split, String glue, int start) {
		return join(split, glue, start, split.length - 1);
	}

	public static String join(String[] split, String glue, int start, int end) {
		start = start >= split.length ? split.length - 1 : start;
		end = end >= split.length ? split.length - 1 : end;
		StringBuilder sb = new StringBuilder();
		boolean set = false;
		for(int i = start; i <= end; ++i) {
			if(set) {
				sb.append(glue);
			}
			sb.append(split[i]);
			set = true;
		}
		return sb.toString();
	}
}
