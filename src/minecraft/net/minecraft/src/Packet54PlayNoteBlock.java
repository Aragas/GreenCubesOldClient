// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;

import java.io.*;

// Referenced classes of package net.minecraft.src:
//            Packet, NetHandler

public class Packet54PlayNoteBlock extends Packet {

	public int xLocation;
	public int yLocation;
	public int zLocation;
	public int instrumentType;
	public int pitch;

	public Packet54PlayNoteBlock() {
	}

	@Override
	public void readPacketData(DataInputStream datainputstream) throws IOException {
		xLocation = datainputstream.readInt();
		yLocation = datainputstream.readShort();
		zLocation = datainputstream.readInt();
		instrumentType = datainputstream.read();
		pitch = datainputstream.read();
	}

	@Override
	public void writePacketData(DataOutputStream dataoutputstream) throws IOException {
		dataoutputstream.writeInt(xLocation);
		dataoutputstream.writeShort(yLocation);
		dataoutputstream.writeInt(zLocation);
		dataoutputstream.write(instrumentType);
		dataoutputstream.write(pitch);
	}

	@Override
	public void processPacket(NetHandler nethandler) {
		nethandler.handleNotePlay(this);
	}

	@Override
	public int getPacketSize() {
		return 12;
	}
}
