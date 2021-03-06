// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;

import java.io.*;

// Referenced classes of package net.minecraft.src:
//            Packet, NetHandler

public class Packet38EntityStatus extends Packet {

	public int entityId;
	public byte entityStatus;

	public Packet38EntityStatus() {
	}

	@Override
	public void readPacketData(DataInputStream datainputstream) throws IOException {
		entityId = datainputstream.readInt();
		entityStatus = datainputstream.readByte();
	}

	@Override
	public void writePacketData(DataOutputStream dataoutputstream) throws IOException {
		dataoutputstream.writeInt(entityId);
		dataoutputstream.writeByte(entityStatus);
	}

	@Override
	public void processPacket(NetHandler nethandler) {
		nethandler.handleEntityStatus(this);
	}

	@Override
	public int getPacketSize() {
		return 5;
	}
}
