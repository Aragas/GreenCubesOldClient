// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;

import java.io.*;

// Referenced classes of package net.minecraft.src:
//            Packet, NetHandler

public class Packet22Collect extends Packet {

	public int collectedEntityId;
	public int collectorEntityId;

	public Packet22Collect() {
	}

	@Override
	public void readPacketData(DataInputStream datainputstream) throws IOException {
		collectedEntityId = datainputstream.readInt();
		collectorEntityId = datainputstream.readInt();
	}

	@Override
	public void writePacketData(DataOutputStream dataoutputstream) throws IOException {
		dataoutputstream.writeInt(collectedEntityId);
		dataoutputstream.writeInt(collectorEntityId);
	}

	@Override
	public void processPacket(NetHandler nethandler) {
		nethandler.handleCollect(this);
	}

	@Override
	public int getPacketSize() {
		return 8;
	}
}
