// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;

import java.io.*;

// Referenced classes of package net.minecraft.src:
//            Packet, EntityItem, ItemStack, MathHelper, 
//            NetHandler

public class Packet21PickupSpawn extends Packet {

	public int entityId;
	public int xPosition;
	public int yPosition;
	public int zPosition;
	public byte rotation;
	public byte pitch;
	public byte roll;
	public ItemStack item;

	public Packet21PickupSpawn() {
	}

	public Packet21PickupSpawn(EntityItem entityitem) {
		entityId = entityitem.entityId;
		item = entityitem.item;
		xPosition = MathHelper.floor_double(entityitem.posX * 32D);
		yPosition = MathHelper.floor_double(entityitem.posY * 32D);
		zPosition = MathHelper.floor_double(entityitem.posZ * 32D);
		rotation = (byte) (int) (entityitem.motionX * 128D);
		pitch = (byte) (int) (entityitem.motionY * 128D);
		roll = (byte) (int) (entityitem.motionZ * 128D);
	}

	@Override
	public void readPacketData(DataInputStream datainputstream) throws IOException {
		entityId = datainputstream.readInt();
		item = readItemStack(datainputstream);
		xPosition = datainputstream.readInt();
		yPosition = datainputstream.readInt();
		zPosition = datainputstream.readInt();
		rotation = datainputstream.readByte();
		pitch = datainputstream.readByte();
		roll = datainputstream.readByte();
	}

	@Override
	public void writePacketData(DataOutputStream dataoutputstream) throws IOException {
		dataoutputstream.writeInt(entityId);
		writeItemStack(item, dataoutputstream);
		dataoutputstream.writeInt(xPosition);
		dataoutputstream.writeInt(yPosition);
		dataoutputstream.writeInt(zPosition);
		dataoutputstream.writeByte(rotation);
		dataoutputstream.writeByte(pitch);
		dataoutputstream.writeByte(roll);
	}

	@Override
	public void processPacket(NetHandler nethandler) {
		nethandler.handlePickupSpawn(this);
	}

	@Override
	public int getPacketSize() {
		return 24;
	}
}
