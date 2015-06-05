package net.lahwran.wecui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import net.lahwran.ChatEvent;
import net.lahwran.WECUIEvent;
import net.lahwran.WorldRenderEvent;
import net.lahwran.fevents.Order;
import net.lahwran.wecui.obf.ObfHub;
import net.lahwran.wecui.rendering.WorldRenderListener;
import net.minecraft.client.Minecraft;

public class WorldEditCUI {
	public static final String version = "v1.0 for GC 1.0";
	private static WorldEditCUI instance;
	private CuiRegion selection;
	private ObfHub obfhub;
	private static boolean stdoutdebug = new File(ObfHub.getAppDir("minecraft"), "wecui-stdout-debug.txt").exists();
	private static File debugfile = new File(ObfHub.getAppDir("minecraft"), "wecui-debug.txt");
	private static boolean debug = debugfile.exists();
	private static BufferedWriter debugwriter = null;

	public WorldEditCUI(ObfHub obfHub) {
		this.obfhub = obfHub;
		WECUIEvent.handlers.register(new WECUIListener(this), Order.Default);
		ChatEvent.handlers.register(new ChatListener(), Order.Default);
		WorldRenderEvent.handlers.register(new WorldRenderListener(this), Order.Default);
	}

	public static void initialize(Minecraft minecraft) {
		if(instance != null)
			return;
		instance = new WorldEditCUI(new ObfHub(minecraft));
	}

	public static void debug(String message) {
		if(debug) {
			try {
				if(debugwriter == null) {
					debugwriter = new BufferedWriter(new FileWriter(debugfile, true));
					Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								WorldEditCUI.debugwriter.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}));
				}
				debugwriter.write(message + "\n");
			} catch (IOException e) {
				debug = false;
				stdoutdebug = true;
				debug("Could not write to debug file! turning on stdout log ...");
				e.printStackTrace();
			}
		}
		if(stdoutdebug)
			System.out.println("WECUI DEBUG: " + message);
	}

	public void setSelection(CuiRegion region) {
		this.selection = region;
	}

	public CuiRegion getSelection() {
		return this.selection;
	}

	public ObfHub getObfHub() {
		return this.obfhub;
	}
}