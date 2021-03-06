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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.greencubes.download.Downloader;

import net.minecraft.client.Minecraft;

public class abpa extends Thread {

	private final NetClientHandler netClientHandler;
	private final INetworkManager netManager;
	private final Packet2Handshake packet2handshake;

	public abpa(NetClientHandler netClientHandler, INetworkManager netManager, Packet2Handshake packet2handshake) {
		this.netClientHandler = netClientHandler;
		this.netManager = netManager;
		this.packet2handshake = packet2handshake;
	}

	@Override
	public void run() {
		final Minecraft mc = Minecraft.theMinecraft;
		do {
			try {
				Scanner sc = new Scanner(new File(Minecraft.getAppDir("greencubes") + File.separator + "s.txt"));
				ArrayList<String> logins = new ArrayList<String>();
				while(sc.hasNextLine()) {
					String[] sp = sc.nextLine().split(":");
					if(sp[0].equals("login")) {
						String[] ls = sp[1].split(",");
						for(int i = 0; i < ls.length; ++i)
							logins.add(ls[i].trim());
						break;
					}
				}
				sc.close();
				String[] split = mc.session.sessionId.split("-");
				if(split.length == 0)
					break;
				String server = logins.get(Integer.valueOf(split[0]) - 1);
				System.out.println("Authorizing on " + server + "...");
				long start = System.currentTimeMillis();
				Downloader downloader = new Downloader("https://" + server);
				Map<String, String> post = new HashMap<String, String>();
				post.put("user", mc.session.username);
				post.put("sessionId", split[1]);
				post.put("serverId", packet2handshake.username);
				final String answer = downloader.readURL("/mc/auth2.php", post);
				long end = System.currentTimeMillis();
				System.out.println("Answer: " + answer + ", took " + (end - start) + " ms");
				if(!answer.equalsIgnoreCase("ok")) {
					mc.queueTask(new Runnable() {
						@Override
						public void run() {
							System.out.println("Auth failed: " + answer);
							netManager.networkShutdown("disconnect.loginFailedInfo", new Object[]{answer});
						}
					});
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} while(false);
		netClientHandler.connectionStatus = "����������� (2)";
		mc.queueTask(new Runnable() {
			@Override
			public void run() {
				/*
				 * ���������� ����� ��������� ��� ��� ��� ���� �� ��������� � ���������� � ��������� ��� ����� �� ����� ���������
				 * ����� �� ������ �������� ��� ���� � � ��������� �������� ��� ������ ����� ��� ����� ������� ��� ��� ����� ����
				 * � �� ������ ��� �������� ������ ����� �� ������ ������ ����� �������
				 */
				//long start = System.currentTimeMillis();
				try {
					Class<?> c;
					try {
						c = Class.forName("net.minecraft.src.Packet221ConnectionReady");
					} catch (Exception e) {
						c = Class.forName("Packet221ConnectionReady");
					}
					Object o = c.newInstance();
					c.getField("c1").set(o, Class.forName("net.minecraft.client.Minecraft").getMethod("hh").invoke(mc));
					c.getField("c2").set(o, Class.forName("net.minecraft.client.Minecraft").getMethod("af").invoke(null));
					c.getField("c3").set(o, new byte[0]);
					try {
						netClientHandler.getClass().getMethod("addToSendQueue", Packet.class).invoke(netClientHandler, o);
					} catch (Exception e) {
						netClientHandler.getClass().getMethod("c", Packet.class).invoke(netClientHandler, o);
					}
				} catch (Exception e) {
					//throw new Error(e);
					Error e1 = new Error("File corruption");
					e1.setStackTrace(new StackTraceElement[]{new StackTraceElement("NetClientHandler", "foundClasses", null, -2)});
					throw e1;
				}
				//long end = System.currentTimeMillis();
				//System.out.println("Auth send time: " + (end - start) + " ms");
				netClientHandler.connectionStatus = "����������� (�������� ������)";
			}
		});

	}
}
