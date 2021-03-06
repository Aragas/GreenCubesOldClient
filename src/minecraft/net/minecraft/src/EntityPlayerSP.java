// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;

import gnu.trove.iterator.TIntObjectIterator;

import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import net.minecraft.client.Minecraft;

// Referenced classes of package net.minecraft.src:
//            EntityPlayer, MouseFilter, Session, MovementInput, 
//            PlayerController, AchievementList, StatFileWriter, GuiAchievement, 
//            World, SoundManager, Potion, PotionEffect, 
//            AxisAlignedBB, FoodStats, PlayerCapabilities, GuiWinGame, 
//            ItemStack, Item, NBTTagCompound, GuiEditSign, 
//            GuiChest, GuiCrafting, GuiEnchantment, GuiFurnace, 
//            GuiBrewingStand, GuiDispenser, EntityCrit2FX, EffectRenderer, 
//            EntityPickupFX, InventoryPlayer, DamageSource, GuiIngame, 
//            StatBase, Achievement, MathHelper, TileEntitySign, 
//            IInventory, TileEntityFurnace, TileEntityBrewingStand, TileEntityDispenser, 
//            Entity

public class EntityPlayerSP extends EntityPlayer {

	public MovementInput movementInput;
	protected Minecraft mc;
	protected int sprintToggleTimer;
	public int sprintingTicksLeft;
	public float renderArmYaw;
	public float renderArmPitch;
	public float prevRenderArmYaw;
	public float prevRenderArmPitch;
	private MouseFilter field_21903_bJ;
	private MouseFilter field_21904_bK;
	private MouseFilter field_21902_bL;
	public PlayerHelper ph;
	public boolean multiplayer;
	public boolean phexists;
	public static Object MESSAGESHOWN;
	public static Object STARTUP;
	public String curmcversion;
	public static final String MCVERSION = "GreenCubes";
	public static final SPCVersion SPCVERSION = new SPCVersion("Single Player Commands", "3.0.1", new Date(0x1340dc29478L));
	public Vector missingRequiredClasses;
	public Vector missingOptionalClasses;

	public EntityPlayerSP(Minecraft minecraft, World world, Session session, int i) {
		super(world);
		sprintToggleTimer = 0;
		sprintingTicksLeft = 0;
		field_21903_bJ = new MouseFilter();
		field_21904_bK = new MouseFilter();
		field_21902_bL = new MouseFilter();
		mc = minecraft;
		dimension = i;
		if(session != null && session.username != null && session.username.length() > 0) {
			skinUrl = (new StringBuilder()).append("http://greenusercontent.net/mc/skins/").append(session.username).append(".png").toString();
		}
		username = session.username;
		initPlayerHelper(session);
		phexists = true;
	}

	@Override
	public ItemStack getEquipment(int slot) {
		if(slot == 0)
			return inventory.getCurrentItem();
		return inventory.armorItemInSlot(slot - 1);
	}

	@Override
	public void updateCloak() {
		playerCloakUrl = (new StringBuilder()).append("http://greenusercontent.net/mc/capes/").append(username).append(".png").toString();
		cloakUrl = playerCloakUrl;
	}

	@Override
	public void moveEntity(double d, double d1, double d2) {
		if(canRunSPC() && ph.moveplayer && !ph.movecamera && (mc.renderViewEntity instanceof SPCEntityCamera)) {
			((SPCEntityCamera) mc.renderViewEntity).setCamera(0.0D, 0.0D, 0.0D, ph.freezecamyaw, ph.freezecampitch);
		} else {
			if(canRunSPC() && ph.noClip) {
				posX += d;
				posY += d1;
				posZ += d2;
				return;
			}
			if(canRunSPC() && (mc.renderViewEntity instanceof SPCEntityCamera)) {
				((SPCEntityCamera) mc.renderViewEntity).setCamera(d, d1, d2, rotationYaw, rotationPitch);
				return;
			}
		}
		super.moveEntity(d, d1, d2);
	}

	@Override
	public void updateEntityActionState() {
		super.updateEntityActionState();
		moveStrafing = movementInput.moveStrafe;
		moveForward = movementInput.moveForward;
		isJumping = movementInput.jump;
		prevRenderArmYaw = renderArmYaw;
		prevRenderArmPitch = renderArmPitch;
		renderArmPitch += (rotationPitch - renderArmPitch) * 0.5D;
		renderArmYaw += (rotationYaw - renderArmYaw) * 0.5D;
	}

	@Override
	public void onLivingUpdate() {
		if(canRunSPC() && ph.sprinting) {
			setSprinting(true);
		} else if(sprintingTicksLeft > 0) {
			sprintingTicksLeft--;
			if(sprintingTicksLeft == 0) {
				setSprinting(false);
			}
		}
		if(sprintToggleTimer > 0) {
			sprintToggleTimer--;
		}
		if(mc.playerController.func_35643_e()) {
			posX = posZ = 0.5D;
			posX = 0.0D;
			posZ = 0.0D;
			rotationYaw = ticksExisted / 12F;
			rotationPitch = 10F;
			posY = 68.5D;
			return;
		}
		//if(!mc.statFileWriter.hasAchievementUnlocked(AchievementList.openInventory)) {
		//	mc.guiAchievement.queueAchievementInformation(AchievementList.openInventory);
		//}
		prevTimeInPortal = timeInPortal;
		if(inPortal) {
			if(!worldObj.multiplayerWorld && ridingEntity != null) {
				mountEntity(null);
			}
			if(mc.currentScreen != null) {
				mc.displayGuiScreen(null);
			}
			if(timeInPortal == 0.0F) {
				mc.sndManager.playSoundFX("portal.trigger", 1.0F, rand.nextFloat() * 0.4F + 0.8F);
			}
			timeInPortal += 0.0125F;
			if(timeInPortal >= 1.0F) {
				timeInPortal = 1.0F;
				if(!worldObj.multiplayerWorld) {
					timeUntilPortal = 10;
					mc.sndManager.playSoundFX("portal.travel", 1.0F, rand.nextFloat() * 0.4F + 0.8F);
					byte byte0 = 0;
					if(dimension == -1) {
						byte0 = 0;
					} else {
						byte0 = -1;
					}
					mc.usePortal(byte0);
					triggerAchievement(AchievementList.portal);
				}
			}
			inPortal = false;
		} else if(isPotionActive(Potion.potionConfusion) && getActivePotionEffect(Potion.potionConfusion).getDuration() > 60) {
			timeInPortal += 0.006666667F;
			if(timeInPortal > 1.0F) {
				timeInPortal = 1.0F;
			}
		} else {
			if(timeInPortal > 0.0F) {
				timeInPortal -= 0.05F;
			}
			if(timeInPortal < 0.0F) {
				timeInPortal = 0.0F;
			}
		}
		if(timeUntilPortal > 0) {
			timeUntilPortal--;
		}
		boolean flag = movementInput.jump;
		float f = 0.8F;
		boolean flag1 = movementInput.moveForward >= f;
		movementInput.updatePlayerMoveState(this);
		if(isUsingItem()) {
			float ff = 0.8f;
			if(getItemInUse().getItem() instanceof ItemBow)
				ff /= getBowSpeedModifier();
			ff = 1.0f - ff;
			movementInput.moveStrafe *= ff;
			movementInput.moveForward *= ff;
			sprintToggleTimer = 0;
		}
		if(movementInput.sneak && ySize < 0.2F) {
			ySize = 0.2F;
		}
		pushOutOfBlocks(posX - width * 0.34999999999999998D, boundingBox.minY + 0.5D, posZ + width * 0.34999999999999998D);
		pushOutOfBlocks(posX - width * 0.34999999999999998D, boundingBox.minY + 0.5D, posZ - width * 0.34999999999999998D);
		pushOutOfBlocks(posX + width * 0.34999999999999998D, boundingBox.minY + 0.5D, posZ - width * 0.34999999999999998D);
		pushOutOfBlocks(posX + width * 0.34999999999999998D, boundingBox.minY + 0.5D, posZ + width * 0.34999999999999998D);
		boolean flag2 = getFoodStats().getFoodLevel() > 6F;
		if(onGround && !flag1 && movementInput.moveForward >= f && !isSprinting() && flag2 && !isUsingItem() && !isPotionActive(Potion.potionBlindness) && canSprint()) {
			if(sprintToggleTimer == 0) {
				sprintToggleTimer = 7;
			} else {
				setSprinting(true);
				sprintToggleTimer = 0;
			}
		}
		if(isSneaking()) {
			sprintToggleTimer = 0;
		}
		if(isSprinting() && (movementInput.moveForward < f || isCollidedHorizontally || !flag2)) {
			setSprinting(false);
		}
		if(capabilities.allowFlying && !flag && movementInput.jump) {
			if(flyToggleTimer == 0) {
				flyToggleTimer = 7;
			} else {
				capabilities.isFlying = !capabilities.isFlying;
				flyToggleTimer = 0;
			}
		}
		if(capabilities.isFlying) {
			if(movementInput.sneak) {
				motionY -= 0.14999999999999999D;
			}
			if(movementInput.jump) {
				motionY += 0.14999999999999999D;
			}
		} else if(canRunSPC() && ph.flying && ph.flymode.equalsIgnoreCase("minecraft") && !capabilities.isFlying) {
			if(movementInput.sneak) {
				motionY -= 0.29999999999999999D;
			}
			if(movementInput.jump) {
				motionY += 0.29999999999999999D;
			}
		}
		super.onLivingUpdate();
		if(onGround && capabilities.isFlying) {
			capabilities.isFlying = false;
		}
	}

	public boolean canSprint() {
		if(activeBuffs.size() > 0) {
			TIntObjectIterator<BuffActive> iterator = activeBuffs.iterator();
			while(iterator.hasNext()) {
				iterator.advance();
				BuffActive ab = iterator.value();
				for(BuffEffect be : ab.effects) {
					if(be.type == BuffEffectType.NO_RUN)
						return false;
				}
			}
		}
		return true;
	}

	@Override
	public void func_40182_b(int i) {
		if(!worldObj.multiplayerWorld) {
			if(dimension == 1 && i == 1) {
				triggerAchievement(AchievementList.theEnd2);
				mc.displayGuiScreen(new GuiWinGame());
			} else {
				triggerAchievement(AchievementList.theEnd);
				mc.sndManager.playSoundFX("portal.travel", 1.0F, rand.nextFloat() * 0.4F + 0.8F);
				mc.usePortal(1);
			}
		}
	}

	public float getFOVMultiplier() {
		float f = 1.0F;
		if(capabilities.isFlying) {
			f *= 1.1F;
		}
		f *= ((landMovementFactor * (1.0f + (getBuffSpeedModifier() - 1.0f) / 3.0f)) / speedOnGround + 1.0F) / 2.0F;
		if(isUsingItem() && getItemInUse().getItem() instanceof ItemBow) {
			int i = (int) (getItemInUseDuration() * getBowSpeedModifier());
			float f1 = i / ((ItemBow) getItemInUse().getItem()).getMaxUse();
			if(f1 > 1.0F) {
				f1 = 1.0F;
			} else {
				f1 *= f1;
			}
			f *= 1.0F - f1 * 0.15F;
		}
		return f;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setInteger("Score", score);
		if(canRunSPC()) {
			ph.writeWaypointsToNBT(((SaveHandler) mc.theWorld.saveHandler).getSaveDirectory());
		}
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		score = nbttagcompound.getInteger("Score");
		if(canRunSPC()) {
			ph.readWaypointsFromNBT(((SaveHandler) mc.theWorld.saveHandler).getSaveDirectory());
		}
	}

	@Override
	public void closeScreen() {
		super.closeScreen();
		mc.displayGuiScreen(null);
	}

	@Override
	public void displayGUIEditSign(TileEntitySign tileentitysign) {
		mc.displayGuiScreen(new GuiEditSign(tileentitysign));
	}

	@Override
	public void displayGUIChest(IInventory iinventory) {
		mc.displayGuiScreen(new GuiChest(inventory, iinventory));
	}

	@Override
	public void displayWorkbenchGUI(int i, int j, int k) {
		mc.displayGuiScreen(new GuiCrafting(inventory, worldObj, i, j, k));
	}

	@Override
	public void func_40181_c(int i, int j, int k) {
		mc.displayGuiScreen(new GuiEnchantment(inventory, worldObj, i, j, k));
	}

	@Override
	public void displayGUIFurnace(TileEntityFurnace tileentityfurnace) {
		mc.displayGuiScreen(new GuiFurnace(inventory, tileentityfurnace));
	}

	@Override
	public void func_40180_a(TileEntityBrewingStand tileentitybrewingstand) {
		mc.displayGuiScreen(new GuiBrewingStand(inventory, tileentitybrewingstand));
	}

	@Override
	public void displayGUIDispenser(TileEntityDispenser tileentitydispenser) {
		mc.displayGuiScreen(new GuiDispenser(inventory, tileentitydispenser));
	}

	@Override
	public void onCriticalHit(Entity entity) {
		mc.effectRenderer.addEffect(new EntityCrit2FXGC(mc.theWorld, entity));
	}

	@Override
	public void func_40183_c(Entity entity) {
		EntityCrit2FXGC entitycrit2fx = new EntityCrit2FXGC(mc.theWorld, entity, "magicCrit");
		mc.effectRenderer.addEffect(entitycrit2fx);
	}

	@Override
	public void onItemPickup(Entity entity, int i) {
		mc.effectRenderer.addEffect(new EntityPickupFXGC(mc.theWorld, entity, this, -0.5F));
	}

	public int getPlayerArmorValue() {
		return inventory.getTotalArmorValue();
	}

	public void sendChatMessage(String s) {
		if(canRunSPC()) {
			ph.processCommand(s);
		}
	}

	@Override
	public boolean isSneaking() {
		return movementInput.sneak && !sleeping;
	}

	public void setHealth(int i) {
		int j = getEntityHealth() - i;
		if(j <= 0) {
			setEntityHealth(i);
			if(j < 0) {
				heartsLife = heartsHalvesLife / 2;
			}
		} else {
			naturalArmorRating = j;
			setEntityHealth(getEntityHealth());
			heartsLife = heartsHalvesLife;
			damageEntity(DamageSource.generic, j);
			hurtTime = maxHurtTime = 10;
		}
	}

	@Override
	public void respawnPlayer() {
		mc.respawn(false, 0, false);
	}

	@Override
	public void func_6420_o() {
	}

	@Override
	public void addChatMessage(String s) {
		mc.ingameGUI.addChatMessageTranslate(s);
	}

	@Override
	public void addStat(StatBase statbase, int i) {
		if(statbase == null) {
			return;
		}
		if(statbase.isAchievement()) {
			Achievement achievement = (Achievement) statbase;
			if(achievement.parentAchievement == null || mc.statFileWriter.hasAchievementUnlocked(achievement.parentAchievement)) {
				if(!mc.statFileWriter.hasAchievementUnlocked(achievement)) {
					mc.guiAchievement.queueTakenAchievement(achievement);
				}
				mc.statFileWriter.readStat(statbase, i);
			}
		} else {
			mc.statFileWriter.readStat(statbase, i);
		}
	}

	private boolean isBlockTranslucent(int i, int j, int k) {
		return worldObj.isBlockNormalCube(i, j, k);
	}

	@Override
	protected boolean pushOutOfBlocks(double d, double d1, double d2) {
		int i = MathHelper.floor_double(d);
		int j = MathHelper.floor_double(d1);
		int k = MathHelper.floor_double(d2);
		double d3 = d - i;
		double d4 = d2 - k;
		if(isBlockTranslucent(i, j, k) || isBlockTranslucent(i, j + 1, k)) {
			boolean flag = !isBlockTranslucent(i - 1, j, k) && !isBlockTranslucent(i - 1, j + 1, k);
			boolean flag1 = !isBlockTranslucent(i + 1, j, k) && !isBlockTranslucent(i + 1, j + 1, k);
			boolean flag2 = !isBlockTranslucent(i, j, k - 1) && !isBlockTranslucent(i, j + 1, k - 1);
			boolean flag3 = !isBlockTranslucent(i, j, k + 1) && !isBlockTranslucent(i, j + 1, k + 1);
			byte byte0 = -1;
			double d5 = 9999D;
			if(flag && d3 < d5) {
				d5 = d3;
				byte0 = 0;
			}
			if(flag1 && 1.0D - d3 < d5) {
				d5 = 1.0D - d3;
				byte0 = 1;
			}
			if(flag2 && d4 < d5) {
				d5 = d4;
				byte0 = 4;
			}
			if(flag3 && 1.0D - d4 < d5) {
				double d6 = 1.0D - d4;
				byte0 = 5;
			}
			float f = 0.1F;
			if(byte0 == 0) {
				motionX = -f;
			}
			if(byte0 == 1) {
				motionX = f;
			}
			if(byte0 == 4) {
				motionZ = -f;
			}
			if(byte0 == 5) {
				motionZ = f;
			}
		}
		return false;
	}

	@Override
	public void setSprinting(boolean flag) {
		super.setSprinting(flag);
		if(!flag) {
			sprintingTicksLeft = 0;
		} else {
			sprintingTicksLeft = 600;
		}
	}

	public void setXPStats(float f, int i, int j) {
		currentXP = f;
		totalXP = i;
		playerLevel = j;
	}

	@Override
	public boolean isEntityInsideOpaqueBlock() {
		if(canRunSPC() && ph.noClip) {
			return false;
		} else {
			return super.isEntityInsideOpaqueBlock();
		}
	}

	@Override
	protected String getHurtSound() {
		if(multiplayer || canRunSPC() && ph.damage) {
			return super.getHurtSound();
		} else {
			return "";
		}
	}

	@Override
	public float getDigSpeedMultipler(Block block, int blockData) {
		if(canRunSPC() && ph.instant)
			return 100000F;
		return super.getDigSpeedMultipler(block, blockData);
	}

	@Override
	public boolean canHarvestBlock(Block block) {
		if(canRunSPC() && ph.instant) {
			return true;
		} else {
			return super.canHarvestBlock(block);
		}
	}

	@Override
	protected void fall(float f) {
		if(canRunSPC() && !ph.falldamage) {
			return;
		} else {
			super.fall(f);
			return;
		}
	}

	@Override
	public void addExhaustion(float f) {
		if(canRunSPC() && (ph.flying || ph.disableHunger)) {
			return;
		} else {
			super.addExhaustion(f);
			return;
		}
	}

	@Override
	protected void jump() {
		if(canRunSPC() && ph.gravity > 1.0D) {
			motionY = 0.4199999868869782D * ph.gravity;
			return;
		} else {
			super.jump();
			return;
		}
	}

	@Override
	public void moveFlying(float f, float f1, float f2) {
		if(!canRunSPC() || ph.speed <= 1.0D) {
			super.moveFlying(f, f1, f2);
			return;
		}
		float f3 = MathHelper.sqrt_float(f * f + f1 * f1);
		if(f3 < 0.01F) {
			return;
		}
		if(f3 < 1.0F) {
			f3 = 1.0F;
		}
		f3 = f2 / f3;
		f *= f3;
		f1 *= f3;
		float f4 = MathHelper.sin((rotationYaw * 3.141593F) / 180F);
		float f5 = MathHelper.cos((rotationYaw * 3.141593F) / 180F);
		double d = canRunSPC() ? ph.speed : 1.0D;
		motionX += (f * f5 - f1 * f4) * d;
		motionZ += (f1 * f5 + f * f4) * d;
	}

	@Override
	public void onUpdate() {
		if(canRunSPC()) {
			ph.beforeUpdate();
			super.onUpdate();
			ph.afterUpdate();
		} else {
			super.onUpdate();
		}
	}

	@Override
	protected void damageEntity(DamageSource damagesource, int i) {
		if(canRunSPC() && !ph.damage) {
			return;
		} else {
			super.damageEntity(damagesource, i);
			return;
		}
	}

	@Override
	public void setEntityDead() {
		if(canRunSPC()) {
			ph.setCurrentPosition();
		}
		super.setEntityDead();
	}

	@Override
	public double getDistanceSqToEntity(Entity entity) {
		if(canRunSPC() && (!ph.mobdamage || ph.mobsfrozen)) {
			return 1.7976931348623157E+308D;
		} else {
			return super.getDistanceSqToEntity(entity);
		}
	}

	@Override
	public void onDeath(DamageSource damagesource) {
		if(canRunSPC() && ph.keepitems && PlayerHelper.INV_BEFORE_DEATH != null) {
			for(int i = 0; i < inventory.armorInventory.length; i++) {
				PlayerHelper.INV_BEFORE_DEATH.armorInventory[i] = inventory.armorItemInSlot(i);
			}

			for(int j = 0; j < inventory.mainInventory.length; j++) {
				PlayerHelper.INV_BEFORE_DEATH.mainInventory[j] = inventory.mainInventory[j];
			}

			ph.destroyInventory();
		}
		super.onDeath(damagesource);
	}

	@Override
	public void attackTargetEntityWithCurrentItem(Entity entity) {
		if(canRunSPC() && ph.instantkill) {
			entity.attackEntityFrom(DamageSource.causePlayerDamage(this), 0x7fffffff);
			return;
		}
		if(canRunSPC() && ph.criticalHit) {
			double d = motionY;
			boolean flag = onGround;
			boolean flag1 = inWater;
			float f = fallDistance;
			super.motionY = -0.10000000000000001D;
			super.inWater = false;
			super.onGround = false;
			super.fallDistance = 0.1F;
			System.out.println("Called - critical hit!");
			super.attackTargetEntityWithCurrentItem(entity);
			motionY = d;
			onGround = flag;
			inWater = flag1;
			fallDistance = f;
			return;
		} else {
			super.attackTargetEntityWithCurrentItem(entity);
			return;
		}
	}

	@Override
	public boolean handleWaterMovement() {
		if(canRunSPC() && !ph.watermovement) {
			return false;
		} else {
			return super.handleWaterMovement();
		}
	}

	@Override
	public boolean handleLavaMovement() {
		if(canRunSPC() && !ph.watermovement) {
			return false;
		} else {
			return super.handleLavaMovement();
		}
	}

	@Override
	public void dropCurrentStack() {
		ItemStack prev = inventory.mainInventory[inventory.currentItem];
		dropPlayerItemWithRandomChoice(prev, false);
		inventory.mainInventory[inventory.currentItem] = null;
		int slot = inventory.getInventorySlotContainItemWithData(prev.itemID, prev.getItemDamage());
		if(slot != -1) {
			inventory.mainInventory[inventory.currentItem] = inventory.mainInventory[slot];
			inventory.mainInventory[slot] = null;
		}
	}

	@Override
	public void dropPlayerItemWithRandomChoice(ItemStack itemstack, boolean flag) {
		if(canRunSPC()) {
			ph.givePlayerItemNaturally(itemstack);
			return;
		} else {
			super.dropPlayerItemWithRandomChoice(itemstack, flag);
			return;
		}
	}

	@Override
	public MovingObjectPosition rayTrace(double d, float f) {
		if(canRunSPC() && d == mc.playerController.getBlockReachDistance()) {
			d = ph.reachdistance;
		}
		return super.rayTrace(d, f);
	}

	@Override
	public boolean isOnLadder() {
		if(canRunSPC() && ph.ladderMode && isCollidedHorizontally) {
			return true;
		} else {
			return super.isOnLadder();
		}
	}

	public void initPlayerHelper(Session session) {
		ph = new PlayerHelper(mc, this);
		ph.readWaypointsFromNBT(ph.getWorldDir());
		multiplayer = mc.isMultiplayerWorld();
		if(session != null && session.username != null && session.username.length() > 0) {
			ph.sessionusername = session.username;
		}
	}

	public boolean checkClasses() {
		missingRequiredClasses = new Vector();
		missingOptionalClasses = new Vector();
		phexists = true;
		Package package1 = (net.minecraft.src.EntityPlayerSP.class).getPackage();
		String s = package1 != null ? (new StringBuilder()).append(package1.getName()).append(".").toString() : "";
		String as[] = {"PlayerHelper", "Settings", "SPCPlugin", "SPCPluginManager", "SPCCommand"};
		String as1[] = {"spc_WorldEdit", "SPCLocalConfiguration", "SPCLocalPlayer", "SPCLocalWorld", "SPCServerInterface", "WorldEditPlugin"};
		String as2[] = as;
		int i = as2.length;
		for(int j = 0; j < i; j++) {
			String s3 = as2[j];
			try {
				Class.forName((new StringBuilder()).append(s).append(s3).toString());
			} catch (Throwable throwable) {
				missingRequiredClasses.add(s3);
			}
		}

		as2 = as1;
		i = as2.length;
		for(int k = 0; k < i; k++) {
			String s4 = as2[k];
			try {
				Class.forName((new StringBuilder()).append(s).append(s4).toString());
			} catch (Throwable throwable1) {
				missingOptionalClasses.add(s4);
			}
		}

		if(missingRequiredClasses.size() != 0) {
			addChatMessage("\2474You are missing these class files: ");
			String s1 = "";
			for(Iterator iterator = missingRequiredClasses.iterator(); iterator.hasNext();) {
				String s2 = (String) iterator.next();
				s1 = (new StringBuilder()).append(s1).append(s2).append(", ").toString();
			}

			addChatMessage((new StringBuilder()).append("\2474").append(s1).toString());
			addChatMessage("\2474Please try reinstalling.");
			phexists = false;
		}
		return phexists;
	}

	public boolean canRunSPC() {
		return phexists && !multiplayer;
	}

	// GreenCubes Books start
	public void displayGUIBookShelf(boolean canEdit, IInventory iinventory) {
		mc.displayGuiScreen(new GuiBookShelf(mc, canEdit, inventory, iinventory));
	}

	public void displayGUIGift(IInventory iinventory, String title, boolean editable) {
		mc.displayGuiScreen(new GuiGift(inventory, iinventory, title, editable));
	}

	// GreenCubes end

}
