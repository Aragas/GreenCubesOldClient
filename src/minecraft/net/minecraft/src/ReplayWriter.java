package net.minecraft.src;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.DeflaterOutputStream;
import net.minecraft.client.Minecraft;

public class ReplayWriter extends NetHandler {

	private static final int NO_COPMRESSION = 0;
	private static final int GZIP_COMPRESSION = 1;
	private static final int DEFLATER_COMPRESSION = 2;

	private final File rawReplayFile;
	private final File dataReplayFile;
	private final DataOutputStream output;
	private final String replayName;
	private final long startTime = System.currentTimeMillis();

	private int packetsCount = 0;
	private int cyclesCount = 0;
	private boolean spawnPlayer = false;
	private int playerId = -1;

	public ReplayWriter(String replayName) throws IOException {
		this.replayName = replayName;
		File replayDir = new File(Minecraft.getMinecraftDir(), "replay");
		replayDir = new File(replayDir, replayName);
		replayDir.mkdirs();
		rawReplayFile = new File(replayDir, "replay.raw");
		dataReplayFile = new File(replayDir, "replay.data");
		output = new DataOutputStream(new BufferedOutputStream(new DeflaterOutputStream(new FileOutputStream(rawReplayFile))));
	}

	public void processPacketClient(Packet packet) {
		int id = packet.getPacketId();
		if(id == 11 || id == 12 || id == 13) {
			Packet10Flying pf = (Packet10Flying) packet;
			EntityPlayer player = Minecraft.theMinecraft.thePlayer;
			if(pf.moving) {
				int diffX = GCUtil.getUnitDiff(player.prevPosX, player.posX);
				int diffY = GCUtil.getUnitDiff(player.prevPosY, player.posY);
				int diffZ = GCUtil.getUnitDiff(player.prevPosZ, player.posZ);
				if(Math.abs(diffX) > 127 || Math.abs(diffY) > 127 || Math.abs(diffZ) > 127) {
					handleEntityTeleport(new Packet34EntityTeleport(player));
					return;
				}
				if(pf.rotating) {
					byte yaw = (byte) MathHelper.floor_float(player.rotationYaw * 32.0F / 45.0F);
					byte pitch = (byte) MathHelper.floor_float(player.rotationPitch * 32.0F / 45.0F);
					Packet33RelEntityMoveLook packet2 = new Packet33RelEntityMoveLook();
					packet2.entityId = player.entityId;
					packet2.xPosition = (byte) diffX;
					packet2.yPosition = (byte) diffY;
					packet2.zPosition = (byte) diffZ;
					packet2.pitch = pitch;
					packet2.yaw = yaw;
					handleEntity(packet2);
					return;
				} else {
					Packet31RelEntityMove packet2 = new Packet31RelEntityMove();
					packet2.entityId = player.entityId;
					packet2.xPosition = (byte) diffX;
					packet2.yPosition = (byte) diffY;
					packet2.zPosition = (byte) diffZ;
					handleEntity(packet2);
					return;
				}
			}
			if(pf.rotating) {
				byte yaw = (byte) MathHelper.floor_float(player.rotationYaw * 32.0F / 45.0F);
				byte pitch = (byte) MathHelper.floor_float(player.rotationPitch * 32.0F / 45.0F);
				Packet32EntityLook packet2 = new Packet32EntityLook();
				packet2.entityId = player.entityId;
				packet2.pitch = pitch;
				packet2.yaw = yaw;
				handleEntity(packet2);
			}
		} else if(id == 18) {
			registerPacket(packet);
		} else if(id == 19) {
			registerPacket(packet);
		}
	}

	@Override
	public void registerPacket(Packet packet) {
		write(packet);
	}

	@Override
	public void handleLogin(Packet1Login packet) {
		registerPacket(packet);
		playerId = packet.entityId;
		spawnPlayer = true;
	}

	@Override
	public void handleTextMessage(Packet222TextMessage packet) {
	}

	@Override
	public void handleGiftWindow(Packet202GiftWindow packet) {
	}

	@Override
	public void handleNotify(Packet207Notify packet) {
	}

	@Override
	public void handleDialog(Packet209Dialog packet) {
	}

	@Override
	public void handleFlying(Packet10Flying packet) {
		EntityPlayer playerEntity = Minecraft.theMinecraft.thePlayer;
		if(spawnPlayer) {
			handleNamedEntitySpawn(new Packet20NamedEntitySpawn(playerEntity));
			spawnPlayer = false;
		}
		handleEntityTeleport(new Packet34EntityTeleport(playerEntity));
	}

	@Override
	public void handleKickDisconnect(Packet255KickDisconnect packet) {
	}

	@Override
	public void handleHandshake(Packet2Handshake packet) {
	}

	@Override
	public void handleHealth(Packet8UpdateHealth packet) {
		Packet38EntityStatus packet2 = new Packet38EntityStatus();
		packet2.entityId = Minecraft.theMinecraft.thePlayer.entityId;
		packet2.entityStatus = (byte) (packet.healthMP <= 0 ? 3 : 2);
		handleEntityStatus(packet2);
	}

	@Override
	public void handleExperience(Packet43Experience packet) {
	}

	@Override
	public void handleRespawn(Packet9Respawn packet) {
		registerPacket(packet);
		spawnPlayer = true;
	}

	@Override
	public void handleOpenWindow(Packet100OpenWindow packet100openwindow) {
	}

	@Override
	public void handleSetSlot(Packet103SetSlot packet103setslot) {
	}

	@Override
	public void handleContainerTransaction(Packet106Transaction packet) {
	}

	@Override
	public void handleWindowItems(Packet104WindowItems packet) {
	}

	@Override
	public void handleCraftingProgress(Packet105UpdateProgressbar packet105updateprogressbar) {
	}

	@Override
	public void handleCloseWindow(Packet101CloseWindow packet101closewindow) {
	}

	@Override
	public void processItemData(Packet131MapData packet131mapdata) {
	}

	@Override
	public void handleStatistic(Packet200Statistic packet200statistic) {
	}

	@Override
	public void handleKeepAlive(Packet0KeepAlive packet0keepalive) {
	}

	@Override
	public void handleChat(Packet3Chat packet) {
		if(packet.channel != 3)
			registerPacket(packet);
	}

	public void startNewCycle() {
		cyclesCount++;
	}

	private void write(Packet packet) {
		try {
			writeInternal(packet);
		} catch (IOException e) {
			try {
				close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			reportError(e);
		}
	}

	private void writeInternal(Packet packet) throws IOException {
		packetsCount++;
		output.writeInt(cyclesCount);
		Packet.writePacket(packet, output, 0);
	}

	public void close() throws IOException {
		output.close();
		FileOutputStream tagOut = new FileOutputStream(dataReplayFile);
		NBTTagCompound replayTag = new NBTTagCompound();
		replayTag.setString("Name", replayName);
		replayTag.setInteger("Cycles", cyclesCount);
		replayTag.setInteger("Packets", packetsCount);
		replayTag.setLong("Started", startTime);
		replayTag.setByte("Type", (byte) 1);
		replayTag.setBoolean("HasMap", false);
		replayTag.setByte("Compression", (byte) DEFLATER_COMPRESSION);
		replayTag.setInteger("Version", NetClientHandler.protocolVersion);
		CompressedStreamTools.writeGzippedCompoundToOutputStream(replayTag, tagOut);
	}

	private void reportError(Exception e) {

	}

	@Override
	public boolean isServerHandler() {
		return false;
	}
}
