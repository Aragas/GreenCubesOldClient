// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;

import gnu.trove.map.hash.THashMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;

import java.io.*;
import java.lang.reflect.Constructor;
import java.util.*;

public abstract class Packet {

	public static TIntObjectHashMap<Class<? extends Packet>> packetIdToClassMap = new TIntObjectHashMap<Class<? extends Packet>>();
	private static final Constructor<? extends Packet>[] constructors = new Constructor[256];
	private static Map<Class<? extends Packet>, Integer> packetClassToIdMap = new THashMap<Class<? extends Packet>, Integer>();
	private static TIntSet clientPacketIdList = new TIntHashSet();
	private static TIntSet serverPacketIdList = new TIntHashSet();
	public final long creationTimeMillis = System.currentTimeMillis();
	public boolean isChunkDataPacket;

	public Packet() {
		isChunkDataPacket = false;
	}

	static void addIdClassMapping(int i, boolean fromServer, boolean fromClient, Class<? extends Packet> class1) {
		if(packetIdToClassMap.containsKey(i))
			throw new IllegalArgumentException((new StringBuilder()).append("Duplicate packet id:").append(i).toString());
		if(packetClassToIdMap.containsKey(class1))
			throw new IllegalArgumentException((new StringBuilder()).append("Duplicate packet class:").append(class1).toString());
		packetIdToClassMap.put(i, class1);
		packetClassToIdMap.put(class1, i);
		if(fromServer) {
			clientPacketIdList.add(i);
			try {
				constructors[i] = class1.getConstructor();
			} catch(Throwable e) {
				throw new AssertionError("Packet " + i + " (" + class1.getName() + ") missing empty constructor, but can be recieved from server");
			}
		}
		if(fromClient)
			serverPacketIdList.add(i);
	}

	public static Packet getNewPacket(int i) {
		try {
			Constructor<? extends Packet> constructor = constructors[i];
			if(constructor == null)
				return null;
			else
				return constructor.newInstance();
		} catch(Exception exception) {
			exception.printStackTrace();
		}
		System.out.println((new StringBuilder()).append("Skipping packet with id ").append(i).toString());
		return null;
	}

	public final int getPacketId() {
		return packetClassToIdMap.get(getClass()).intValue();
	}

	public static Packet readPacket(DataInputStream datainputstream, boolean flag) throws IOException {
		int i = 0;
		Packet packet = null;
		try {
			while(datainputstream.available() < 1) {
				try {
					Thread.sleep(2L);
				} catch (InterruptedException e) {
				}
			}
			i = datainputstream.read();
			if(i == -1)
				return null;
			packet = getNewPacket(i);
			if(packet == null) {
				throw new IOException((new StringBuilder()).append("Bad packet id ").append(i).toString());
			}
			//System.out.println("Recieving " + packet.getClass().getSimpleName());
			packet.readPacketData(datainputstream);
		} catch (EOFException eofexception) {
			System.out.println("Reached end of stream");
			return null;
		}
		PacketCount.func_40561_a(i, packet.getPacketSize());
		return packet;
	}

	public static void writePacket(Packet packet, DataOutputStream dataoutputstream, int packetNumber) throws IOException {
		dataoutputstream.write(packet.getPacketId() ^ (packetNumber & 0xFF));
		packet.writePacketData(dataoutputstream);
	}

	public static void writeString(String s, DataOutputStream dataoutputstream) throws IOException {
		if(s.length() > 32767) {
			throw new IOException("String too big");
		} else {
			dataoutputstream.writeShort(s.length());
			dataoutputstream.writeChars(s);
			return;
		}
	}

	public static void writeBigString(String s, DataOutputStream out) throws IOException {
		out.writeInt(s.length());
		out.writeChars(s);
	}

	public static String readString(DataInputStream input, int i) throws IOException {
		short word0 = input.readShort();
		if(word0 < 0)
			throw new IOException("Received string length is less than zero! Weird string!");
		StringBuilder stringbuilder = new StringBuilder();
		for(int j = 0; j < word0; j++)
			stringbuilder.append(input.readChar());
		return stringbuilder.toString();
	}

	public abstract void readPacketData(DataInputStream input) throws IOException;

	public abstract void writePacketData(DataOutputStream output) throws IOException;

	public abstract void processPacket(NetHandler handler);

	public abstract int getPacketSize();

	public static ItemStack readItemStack(DataInputStream input) throws IOException {
		ItemStack itemstack = null;
		short id = input.readShort();
		if(id >= 0) {
			short count = input.readShort();
			short damage = input.readShort();
			itemstack = new ItemStack(id, count, damage);
			itemstack.nbtData = readNBTData(input);
		}
		return itemstack;
	}

	public static void writeItemStack(ItemStack itemstack, DataOutputStream output) throws IOException {
		if(itemstack == null || itemstack.itemID == 0 || itemstack.stackSize == 0) {
			output.writeShort(-1);
		} else {
			output.writeShort(itemstack.itemID);
			output.writeByte(itemstack.stackSize);
			output.writeShort(itemstack.getItemDamage());
			writeNBTData(itemstack.nbtData, output);
		}
	}

	public static NBTTagCompound readNBTData(DataInputStream input) throws IOException {
		short length = input.readShort();
		if(length > 0) {
			byte abyte0[] = new byte[length];
			input.readFully(abyte0);
			return CompressedStreamTools.fromDeflateArray(abyte0);
		}
		return null;
	}

	public static void writeNBTData(NBTTagCompound nbttagcompound, DataOutputStream dataoutputstream) throws IOException {
		if(nbttagcompound == null) {
			dataoutputstream.writeShort(-1);
		} else {
			byte abyte0[] = CompressedStreamTools.toDeflateArray(nbttagcompound);
			dataoutputstream.writeShort((short) abyte0.length);
			dataoutputstream.write(abyte0);
		}
	}

	static {
		addIdClassMapping(0, true, true, net.minecraft.src.Packet0KeepAlive.class);
		addIdClassMapping(1, true, true, net.minecraft.src.Packet1Login.class);
		addIdClassMapping(2, true, true, net.minecraft.src.Packet2Handshake.class);
		addIdClassMapping(3, true, true, net.minecraft.src.Packet3Chat.class);
		addIdClassMapping(4, true, false, net.minecraft.src.Packet4UpdateTime.class);
		addIdClassMapping(5, true, false, net.minecraft.src.Packet5PlayerInventory.class);
		addIdClassMapping(6, true, false, net.minecraft.src.Packet6SpawnPosition.class);
		addIdClassMapping(7, false, true, net.minecraft.src.Packet7UseEntity.class);
		addIdClassMapping(8, true, false, net.minecraft.src.Packet8UpdateHealth.class);
		addIdClassMapping(9, true, true, net.minecraft.src.Packet9Respawn.class);
		addIdClassMapping(10, true, true, net.minecraft.src.Packet10Flying.class);
		addIdClassMapping(11, true, true, net.minecraft.src.Packet11PlayerPosition.class);
		addIdClassMapping(12, true, true, net.minecraft.src.Packet12PlayerLook.class);
		addIdClassMapping(13, true, true, net.minecraft.src.Packet13PlayerLookMove.class);
		addIdClassMapping(14, false, true, net.minecraft.src.Packet14BlockDig.class);
		addIdClassMapping(15, false, true, net.minecraft.src.Packet15Place.class);
		addIdClassMapping(16, false, true, net.minecraft.src.Packet16BlockItemSwitch.class);
		addIdClassMapping(17, true, false, net.minecraft.src.Packet17Sleep.class);
		addIdClassMapping(18, true, true, net.minecraft.src.Packet18Animation.class);
		addIdClassMapping(19, false, true, net.minecraft.src.Packet19EntityAction.class);
		addIdClassMapping(20, true, false, net.minecraft.src.Packet20NamedEntitySpawn.class);
		addIdClassMapping(21, true, false, net.minecraft.src.Packet21PickupSpawn.class);
		addIdClassMapping(22, true, false, net.minecraft.src.Packet22Collect.class);
		addIdClassMapping(23, true, false, net.minecraft.src.Packet23VehicleSpawn.class);
		addIdClassMapping(24, true, false, net.minecraft.src.Packet24MobSpawn.class);
		addIdClassMapping(25, true, false, net.minecraft.src.Packet25EntityPainting.class);
		addIdClassMapping(26, true, false, net.minecraft.src.Packet026EntityOrb.class);
		addIdClassMapping(27, false, true, net.minecraft.src.Packet27Position.class);
		addIdClassMapping(28, true, false, net.minecraft.src.Packet28EntityVelocity.class);
		addIdClassMapping(29, true, false, net.minecraft.src.Packet29DestroyEntity.class);
		addIdClassMapping(30, true, false, net.minecraft.src.Packet30Entity.class);
		addIdClassMapping(31, true, false, net.minecraft.src.Packet31RelEntityMove.class);
		addIdClassMapping(32, true, false, net.minecraft.src.Packet32EntityLook.class);
		addIdClassMapping(33, true, false, net.minecraft.src.Packet33RelEntityMoveLook.class);
		addIdClassMapping(34, true, false, net.minecraft.src.Packet34EntityTeleport.class);
		addIdClassMapping(35, true, false, Packet035EntityHealthChange.class);
		addIdClassMapping(36, true, false, Packet036EntityHealth.class);
		
		
		addIdClassMapping(38, true, false, net.minecraft.src.Packet38EntityStatus.class);
		addIdClassMapping(39, true, false, net.minecraft.src.Packet39AttachEntity.class);
		addIdClassMapping(40, true, false, net.minecraft.src.Packet40EntityMetadata.class);
		addIdClassMapping(41, true, false, net.minecraft.src.Packet41EntityEffect.class);
		addIdClassMapping(42, true, false, net.minecraft.src.Packet42RemoveEntityEffect.class);
		addIdClassMapping(43, true, false, net.minecraft.src.Packet43Experience.class);
		addIdClassMapping(44, true, true, Packet044PlayerReturn.class);

		addIdClassMapping(50, true, false, net.minecraft.src.Packet50PreChunk.class);
		addIdClassMapping(51, true, false, net.minecraft.src.Packet51MapChunk.class);
		addIdClassMapping(52, true, false, net.minecraft.src.Packet52MultiBlockChange.class);
		addIdClassMapping(53, true, false, net.minecraft.src.Packet53BlockChange.class);
		addIdClassMapping(54, true, false, net.minecraft.src.Packet54PlayNoteBlock.class);
		addIdClassMapping(55, true, false, net.minecraft.src.Packet55MultiChunk.class);

		addIdClassMapping(60, true, false, net.minecraft.src.Packet60Explosion.class);
		addIdClassMapping(61, true, false, net.minecraft.src.Packet61DoorChange.class);
		addIdClassMapping(62, true, false, net.minecraft.src.Packet62PlaySound.class);

		addIdClassMapping(70, true, false, net.minecraft.src.Packet70Bed.class);
		addIdClassMapping(71, true, false, net.minecraft.src.Packet71Weather.class);

		addIdClassMapping(100, true, false, net.minecraft.src.Packet100OpenWindow.class);
		addIdClassMapping(101, true, true, net.minecraft.src.Packet101CloseWindow.class);
		addIdClassMapping(102, false, true, net.minecraft.src.Packet102WindowClick.class);
		addIdClassMapping(103, true, false, net.minecraft.src.Packet103SetSlot.class);
		addIdClassMapping(104, true, false, net.minecraft.src.Packet104WindowItems.class);
		addIdClassMapping(105, true, false, net.minecraft.src.Packet105UpdateProgressbar.class);
		addIdClassMapping(106, true, true, net.minecraft.src.Packet106Transaction.class);
		addIdClassMapping(107, true, true, net.minecraft.src.Packet107CreativeSetSlot.class);
		addIdClassMapping(108, false, true, net.minecraft.src.Packet108EnchantItem.class);

		addIdClassMapping(130, true, true, net.minecraft.src.Packet130UpdateSign.class);
		addIdClassMapping(131, true, false, net.minecraft.src.Packet131MapData.class);
		addIdClassMapping(132, true, true, Packet132TileData.class);

		addIdClassMapping(200, true, false, net.minecraft.src.Packet200Statistic.class);
		addIdClassMapping(201, true, false, net.minecraft.src.Packet201PlayerInfo.class);
		// GreenCubes start
		addIdClassMapping(202, true, false, Packet202GiftWindow.class);
		addIdClassMapping(203, false, true, Packet203GiftWindowTitle.class);
		addIdClassMapping(204, true, false, Packet204Channel.class);
		addIdClassMapping(205, true, true, Packet205StatusReport.class);
		addIdClassMapping(206, true, false, Packet206SpawnEffect.class);
		addIdClassMapping(207, true, true, Packet207Notify.class);
		addIdClassMapping(208, true, true, Packet208NotifyAnswer.class);
		addIdClassMapping(209, true, true, Packet209Dialog.class);
		addIdClassMapping(210, false, true, Packet210DialogAnswer.class);
		addIdClassMapping(211, true, true, Packet211UseBlock2.class);
		addIdClassMapping(212, true, true, Packet212MultiData.class);

		addIdClassMapping(221, true, true, net.minecraft.src.Packet221ConnectionReady.class);
		addIdClassMapping(222, true, true, net.minecraft.src.Packet222TextMessage.class);
		addIdClassMapping(224, true, true, net.minecraft.src.Packet224AskBook.class);
		// GreenCubes end
		addIdClassMapping(254, false, true, net.minecraft.src.Packet254ServerPing.class);
		addIdClassMapping(255, true, true, net.minecraft.src.Packet255KickDisconnect.class);
	}
}
