// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;

import java.util.HashMap;
import java.util.Map;

public class PacketCount {

	public static boolean field_40565_a = true;
	private static final Map field_40563_b = new HashMap();
	private static final Map field_40564_c = new HashMap();
	private static final Object field_40562_d = new Object();

	public PacketCount() {
	}

	public static void func_40561_a(int i, long l) {
		if(!field_40565_a) {
			return;
		}
		synchronized(field_40562_d) {
			if(field_40563_b.containsKey(Integer.valueOf(i))) {
				field_40563_b.put(Integer.valueOf(i), Long.valueOf(((Long) field_40563_b.get(Integer.valueOf(i))).longValue() + 1L));
				field_40564_c.put(Integer.valueOf(i), Long.valueOf(((Long) field_40564_c.get(Integer.valueOf(i))).longValue() + l));
			} else {
				field_40563_b.put(Integer.valueOf(i), Long.valueOf(1L));
				field_40564_c.put(Integer.valueOf(i), Long.valueOf(l));
			}
		}
	}

}
