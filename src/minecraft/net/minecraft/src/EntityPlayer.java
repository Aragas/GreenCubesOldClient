// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;

import gnu.trove.iterator.TIntObjectIterator;

import java.util.*;

import org.greencubes.party.Party;
import org.greencubes.util.TrigMath;

import net.minecraft.client.Minecraft;

// Referenced classes of package net.minecraft.src:
//            EntityLiving, InventoryPlayer, FoodStats, PlayerCapabilities, 
//            ContainerPlayer, World, ChunkCoordinates, DataWatcher, 
//            ItemStack, Item, EnumAction, Container, 
//            StatList, Vec3D, Potion, PotionEffect, 
//            MathHelper, AxisAlignedBB, Entity, EnchantmentHelper, 
//            EntityItem, Material, NBTTagCompound, NBTTagList, 
//            DamageSource, EntityMob, EntityArrow, EntityCreeper, 
//            EntityGhast, EntityWolf, AchievementList, EnumStatus, 
//            WorldProvider, BlockBed, Block, IChunkProvider, 
//            EntityMinecart, EntityBoat, EntityPig, ItemPotion, 
//            EntityFishHook, IInventory, TileEntityFurnace, TileEntityDispenser, 
//            TileEntitySign, TileEntityBrewingStand, StatBase

public abstract class EntityPlayer extends EntityLiving {

	public InventoryPlayer inventory;
	public Container inventorySlots;
	public Container craftingInventory;
	protected FoodStats foodStats;
	protected int flyToggleTimer;
	public byte unusedByte;
	public int score;
	public float prevCameraYaw;
	public float cameraYaw;
	public String username;
	public int dimension;
	public String playerCloakUrl;
	public int xpCooldown;
	public double field_20066_r;
	public double field_20065_s;
	public double field_20064_t;
	public double field_20063_u;
	public double field_20062_v;
	public double field_20061_w;
	protected boolean sleeping;
	public ChunkCoordinates bedChunkCoordinates;
	private int sleepTimer;
	public float field_22063_x;
	public float field_22062_y;
	public float field_22061_z;
	private ChunkCoordinates playerSpawnCoordinate;
	private ChunkCoordinates startMinecartRidingCoordinate;
	public int timeUntilPortal;
	protected boolean inPortal;
	public float timeInPortal;
	public float prevTimeInPortal;
	public PlayerCapabilities capabilities;
	public int playerLevel;
	public int totalXP;
	public float currentXP;
	protected float speedOnGround;
	protected float speedInAir;

	public FlyControl flyControl = new FlyControl(this);
	
	public int organizationId = 0;
	public String organizationUrl = null;
	public Party party = new Party(this);
	/**
	 * Server-side global player id
	 */
	public int playerId;

	public EntityPlayer(World world) {
		super(world);
		inventory = new InventoryPlayer(this);
		foodStats = new FoodStats();
		flyToggleTimer = 0;
		unusedByte = 0;
		score = 0;
		isSwinging = false;
		swingProgressInt = 0;
		xpCooldown = 0;
		timeUntilPortal = 20;
		inPortal = false;
		capabilities = new PlayerCapabilities();
		speedOnGround = 0.1F;
		speedInAir = 0.02F;
		fishEntity = null;
		inventorySlots = new ContainerPlayer(inventory, !world.multiplayerWorld);
		craftingInventory = inventorySlots;
		yOffset = 1.62F;
		ChunkCoordinates chunkcoordinates = world.getSpawnPoint();
		setLocationAndAngles(chunkcoordinates.posX + 0.5D, chunkcoordinates.posY + 1, chunkcoordinates.posZ + 0.5D, 0.0F, 0.0F);
		entityType = "humanoid";
		field_9353_B = 180F;
		fireResistance = 20;
		texture = "/mob/char.png";
		health = maxHealth = 20;
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataWatcher.addObject(16, Byte.valueOf((byte) 0));
		dataWatcher.addObject(17, Byte.valueOf((byte) 0));
	}

	@Override
	public void onUpdate() {
		flyControl.update1();
		if(xpCooldown > 0) {
			xpCooldown--;
		}
		if(isPlayerSleeping()) {
			sleepTimer++;
			if(sleepTimer > 100) {
				sleepTimer = 100;
			}
			if(!worldObj.multiplayerWorld) {
				if(!isInBed()) {
					wakeUpPlayer(true, true, false);
				} else if(worldObj.isDaytime()) {
					wakeUpPlayer(false, true, true);
				}
			}
		} else if(sleepTimer > 0) {
			sleepTimer++;
			if(sleepTimer >= 110) {
				sleepTimer = 0;
			}
		}
		super.onUpdate();
		if(!worldObj.multiplayerWorld && craftingInventory != null && !craftingInventory.canInteractWith(this)) {
			closeScreen();
			craftingInventory = inventorySlots;
		}
		if(capabilities.isFlying) {
			for(int i = 0; i < 8; i++) {
			}
		}
		if(isBurning() && capabilities.disableDamage) {
			func_40045_B();
		}
		field_20066_r = field_20063_u;
		field_20065_s = field_20062_v;
		field_20064_t = field_20061_w;
		double d = posX - field_20063_u;
		double d1 = posY - field_20062_v;
		double d2 = posZ - field_20061_w;
		double d3 = 10D;
		if(d > d3) {
			field_20066_r = field_20063_u = posX;
		}
		if(d2 > d3) {
			field_20064_t = field_20061_w = posZ;
		}
		if(d1 > d3) {
			field_20065_s = field_20062_v = posY;
		}
		if(d < -d3) {
			field_20066_r = field_20063_u = posX;
		}
		if(d2 < -d3) {
			field_20064_t = field_20061_w = posZ;
		}
		if(d1 < -d3) {
			field_20065_s = field_20062_v = posY;
		}
		field_20063_u += d * 0.25D;
		field_20061_w += d2 * 0.25D;
		field_20062_v += d1 * 0.25D;
		addStat(StatList.minutesPlayedStat, 1);
		if(ridingEntity == null) {
			startMinecartRidingCoordinate = null;
		}
		if(!worldObj.multiplayerWorld) {
			foodStats.onUpdate(this);
		}
		flyControl.update2();
	}
	
	@Override
	public void stopUsingItem() {
		if(itemInUse != null) {
			itemInUse.onPlayerStoppedUsing(worldObj, this, itemInUseCount);
		}
		clearItemInUse();
	}

	@Override
	public void handleHealthUpdate(byte byte0) {
		if(byte0 == 9) {
			func_35208_ae();
		} else {
			super.handleHealthUpdate(byte0);
		}
	}

	@Override
	protected boolean isMovementBlocked() {
		return getEntityHealth() <= 0 || isPlayerSleeping();
	}

	protected void closeScreen() {
		craftingInventory = inventorySlots;
	}

	@Override
	public void updateCloak() {
		playerCloakUrl = new StringBuilder().append("http://greenusercontent.net/mc/capes/").append(username).append(".png").toString();
		cloakUrl = playerCloakUrl;
	}

	@Override
	public void updateRidden() {
		double d = posX;
		double d1 = posY;
		double d2 = posZ;
		super.updateRidden();
		prevCameraYaw = cameraYaw;
		cameraYaw = 0.0F;
		addMountedMovementStat(posX - d, posY - d1, posZ - d2);
	}

	@Override
	public void preparePlayerToSpawn() {
		yOffset = 1.62F;
		setSize(0.6F, 1.8F);
		super.preparePlayerToSpawn();
		setEntityHealth(maxHealth);
		deathTime = 0;
	}

	@Override
	protected int getArmSwingSpeed() {
		int i = 6;
		if(isPotionActive(Potion.potionDigSpeed)) {
			i -= (1 + getActivePotionEffect(Potion.potionDigSpeed).getAmplifier()) * 1;
		}
		if(isPotionActive(Potion.potionDigSlow)) {
			i += (1 + getActivePotionEffect(Potion.potionDigSlow).getAmplifier()) * 2;
		}
		if(activeBuffs.size() > 0) {
			float f1 = 1.0F;
			TIntObjectIterator<BuffActive> iterator = activeBuffs.iterator();
			while(iterator.hasNext()) {
				iterator.advance();
				BuffActive ab = iterator.value();
				for(BuffEffect be : ab.effects) {
					if(be.type == BuffEffectType.DIG_SPEED_DOWN)
						f1 -= be.multipler;
					else if(be.type == BuffEffectType.DIG_SPEED_UP)
						f1 += be.multipler;
				}
			}
			i /= f1;
		}
		return i;
	}

	@Override
	protected void updateEntityActionState() {
		/*int i = getArmSwingSpeed();
		if(isSwinging) {
			swingProgressInt++;
			if(swingProgressInt >= i) {
				swingProgressInt = 0;
				isSwinging = false;
			}
		} else {
			swingProgressInt = 0;
		}
		swingProgress = (float) swingProgressInt / (float) i;*/
	}

	@Override
	public boolean canBePushed() {
		return !Minecraft.theMinecraft.noCollidePlayers;
	}

	@Override
	public void onLivingUpdate() {
		if(flyToggleTimer > 0) {
			flyToggleTimer--;
		}
		if(worldObj.difficultySetting == 0 && getEntityHealth() < maxHealth && (ticksExisted % 20) * 12 == 0) {
			heal(1);
		}
		inventory.decrementAnimations();
		prevCameraYaw = cameraYaw;
		super.onLivingUpdate();
		landMovementFactor = speedOnGround;
		jumpMovementFactor = speedInAir;
		if(isSprinting()) {
			landMovementFactor += speedOnGround * 0.29999999999999999D;
			jumpMovementFactor += speedInAir * 0.29999999999999999D;
		}
		float f = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
		float f1 = flyControl.isFlying() ? 0.0F : (float) TrigMath.atan(-motionY * 0.20000000298023224D) * 15F;
		if(f > 0.1F)
			f = 0.1F;
		if(!onGround || getEntityHealth() <= 0)
			f = 0.0F;
		if(onGround || getEntityHealth() <= 0)
			f1 = 0.0F;
		cameraYaw += (f - cameraYaw) * 0.4F;
		cameraPitch += (f1 - cameraPitch) * 0.8F;
		if(getEntityHealth() > 0) {
			List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(1.0D, 0.0D, 1.0D));
			if(list != null)
				for(int i = 0; i < list.size(); i++) {
					Entity entity = (Entity) list.get(i);
					if(!entity.isDead)
						collideWithPlayer(entity);
				}
		}
	}

	private void collideWithPlayer(Entity entity) {
		entity.onCollideWithPlayer(this);
	}

	public int getScore() {
		return score;
	}

	@Override
	public void onDeath(DamageSource damagesource) {
		super.onDeath(damagesource);
		setSize(0.2F, 0.2F);
		setPosition(posX, posY, posZ);
		motionY = 0.10000000149011612D;
		if(username.equals("Notch")) {
			dropPlayerItemWithRandomChoice(new ItemStack(Item.appleRed, 1), true);
		}
		inventory.dropAllItems();
		if(damagesource != null) {
			motionX = -MathHelper.cos(((attackedAtYaw + rotationYaw) * 3.141593F) / 180F) * 0.1F;
			motionZ = -MathHelper.sin(((attackedAtYaw + rotationYaw) * 3.141593F) / 180F) * 0.1F;
		} else {
			motionX = motionZ = 0.0D;
		}
		yOffset = 0.1F;
		addStat(StatList.deathsStat, 1);
	}

	@Override
	public void addToPlayerScore(Entity entity, int i) {
		score += i;
		if(entity instanceof EntityPlayer) {
			addStat(StatList.playerKillsStat, 1);
		} else {
			addStat(StatList.mobKillsStat, 1);
		}
	}

	@Override
	protected int func_40116_f(int i) {
		int j = EnchantmentHelper.getRespiration(inventory);
		if(j > 0 && rand.nextInt(j + 1) > 0)
			return i;
		else
			return super.func_40116_f(i);
	}

	public void dropCurrentItem() {
		dropPlayerItemWithRandomChoice(inventory.decrStackSize(inventory.currentItem, 1), false);
	}

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

	public void dropPlayerItem(ItemStack itemstack) {
		dropPlayerItemWithRandomChoice(itemstack, false);
	}

	public void dropPlayerItemWithRandomChoice(ItemStack itemstack, boolean flag) {
		if(itemstack == null) {
			return;
		}
		EntityItem entityitem = new EntityItem(worldObj, posX, (posY - 0.30000001192092896D) + getEyeHeight(), posZ, itemstack);
		entityitem.delayBeforeCanPickup = 40;
		float f = 0.1F;
		if(flag) {
			float f2 = rand.nextFloat() * 0.5F;
			float f4 = rand.nextFloat() * 3.141593F * 2.0F;
			entityitem.motionX = -MathHelper.sin(f4) * f2;
			entityitem.motionZ = MathHelper.cos(f4) * f2;
			entityitem.motionY = 0.20000000298023224D;
		} else {
			float f1 = 0.3F;
			entityitem.motionX = -MathHelper.sin((rotationYaw / 180F) * 3.141593F) * MathHelper.cos((rotationPitch / 180F) * 3.141593F) * f1;
			entityitem.motionZ = MathHelper.cos((rotationYaw / 180F) * 3.141593F) * MathHelper.cos((rotationPitch / 180F) * 3.141593F) * f1;
			entityitem.motionY = -MathHelper.sin((rotationPitch / 180F) * 3.141593F) * f1 + 0.1F;
			f1 = 0.02F;
			float f3 = rand.nextFloat() * 3.141593F * 2.0F;
			f1 *= rand.nextFloat();
			entityitem.motionX += Math.cos(f3) * f1;
			entityitem.motionY += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
			entityitem.motionZ += Math.sin(f3) * f1;
		}
		joinEntityItemWithWorld(entityitem);
		addStat(StatList.dropStat, 1);
	}

	protected void joinEntityItemWithWorld(EntityItem entityitem) {
		worldObj.entityJoinedWorld(entityitem);
	}

	public float getDigSpeedMultipler(Block block, int blockData) {
		/*
		 * Well... � ����� ����������� �����, � ��� ��� ���� ��� �� �����
		 * �������������� ������������� ���������� ���������� ��������
		 * � �� ����� ������
		 */
		float f1 = 1.0F;
		if(isPotionActive(Potion.potionDigSpeed))
			f1 *= 1.0F + (getActivePotionEffect(Potion.potionDigSpeed).getAmplifier() + 1) * 0.2F;
		if(isPotionActive(Potion.potionDigSlow))
			f1 *= 1.0F - (getActivePotionEffect(Potion.potionDigSlow).getAmplifier() + 1) * 0.2F;
		if(activeBuffs.size() > 0) {
			TIntObjectIterator<BuffActive> iterator = activeBuffs.iterator();
			while(iterator.hasNext()) {
				iterator.advance();
				BuffActive ab = iterator.value();
				for(BuffEffect be : ab.effects) {
					if(be.type == BuffEffectType.DIG_SPEED_DOWN)
						f1 -= be.multipler;
					else if(be.type == BuffEffectType.DIG_SPEED_UP)
						f1 += be.multipler;
				}
			}
		}
		if(isInsideOfMaterial(Material.water))
			f1 /= 5F;
		if(!onGround)
			f1 /= 5F;
		return f1;
	}

	public boolean canHarvestBlock(Block block) {
		return inventory.canHarvestBlock(block);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		NBTTagList nbttaglist = nbttagcompound.getTagList("Inventory");
		inventory.readFromNBT(nbttaglist);
		dimension = nbttagcompound.getInteger("Dimension");
		sleeping = nbttagcompound.getBoolean("Sleeping");
		sleepTimer = nbttagcompound.getShort("SleepTimer");
		currentXP = nbttagcompound.getFloat("XpP");
		playerLevel = nbttagcompound.getInteger("XpLevel");
		totalXP = nbttagcompound.getInteger("XpTotal");
		if(sleeping) {
			bedChunkCoordinates = new ChunkCoordinates(MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ));
			wakeUpPlayer(true, true, false);
		}
		if(nbttagcompound.hasKey("SpawnX") && nbttagcompound.hasKey("SpawnY") && nbttagcompound.hasKey("SpawnZ")) {
			playerSpawnCoordinate = new ChunkCoordinates(nbttagcompound.getInteger("SpawnX"), nbttagcompound.getInteger("SpawnY"), nbttagcompound.getInteger("SpawnZ"));
		}
		foodStats.readStatsFromNBT(nbttagcompound);
		capabilities.func_40600_b(nbttagcompound);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setTag("Inventory", inventory.writeToNBT(new NBTTagList()));
		nbttagcompound.setInteger("Dimension", dimension);
		nbttagcompound.setBoolean("Sleeping", sleeping);
		nbttagcompound.setShort("SleepTimer", (short) sleepTimer);
		nbttagcompound.setFloat("XpP", currentXP);
		nbttagcompound.setInteger("XpLevel", playerLevel);
		nbttagcompound.setInteger("XpTotal", totalXP);
		if(playerSpawnCoordinate != null) {
			nbttagcompound.setInteger("SpawnX", playerSpawnCoordinate.posX);
			nbttagcompound.setInteger("SpawnY", playerSpawnCoordinate.posY);
			nbttagcompound.setInteger("SpawnZ", playerSpawnCoordinate.posZ);
		}
		foodStats.writeStatsToNBT(nbttagcompound);
		capabilities.func_40601_a(nbttagcompound);
	}

	public void displayGUIChest(IInventory iinventory) {
	}

	public void func_40181_c(int i, int j, int k) {
	}

	public void displayWorkbenchGUI(int i, int j, int k) {
	}

	public void onItemPickup(Entity entity, int i) {
	}

	@Override
	public float getEyeHeight() {
		return 0.12F;
	}

	protected void resetHeight() {
		yOffset = 1.62F;
	}

	@Override
	public boolean attackEntityFrom(DamageSource damagesource, int i) {
		if(capabilities.disableDamage && !damagesource.canHarmInCreative()) {
			return false;
		}
		entityAge = 0;
		if(getEntityHealth() <= 0) {
			return false;
		}
		if(isPlayerSleeping() && !worldObj.multiplayerWorld) {
			wakeUpPlayer(true, true, false);
		}
		Entity entity = damagesource.getEntity();
		if((entity instanceof EntityMob) || (entity instanceof EntityArrow)) {
			if(worldObj.difficultySetting == 0) {
				i = 0;
			}
			if(worldObj.difficultySetting == 1) {
				i = i / 2 + 1;
			}
			if(worldObj.difficultySetting == 3) {
				i = (i * 3) / 2;
			}
		}
		if(i == 0) {
			return false;
		}
		Entity entity1 = entity;
		if((entity1 instanceof EntityArrow) && ((EntityArrow) entity1).shootingEntity != null) {
			entity1 = ((EntityArrow) entity1).shootingEntity;
		}
		if(entity1 instanceof EntityLiving) {
			alertWolves((EntityLiving) entity1, false);
		}
		addStat(StatList.damageTakenStat, i);
		return super.attackEntityFrom(damagesource, i);
	}

	@Override
	protected int func_40128_b(DamageSource damagesource, int i) {
		int j = super.func_40128_b(damagesource, i);
		if(j <= 0) {
			return 0;
		}
		int k = EnchantmentHelper.getEnchantmentModifierDamage(inventory, damagesource);
		if(k > 20) {
			k = 20;
		}
		if(k > 0 && k <= 20) {
			int l = 25 - k;
			int i1 = j * l + field_40129_bA;
			j = i1 / 25;
			field_40129_bA = i1 % 25;
		}
		return j;
	}

	protected boolean isPVPEnabled() {
		return false;
	}

	protected void alertWolves(EntityLiving entityliving, boolean flag) {
		if((entityliving instanceof EntityCreeper) || (entityliving instanceof EntityGhast)) {
			return;
		}
		if(entityliving instanceof EntityWolf) {
			EntityWolf entitywolf = (EntityWolf) entityliving;
			if(entitywolf.isWolfTamed() && username.equals(entitywolf.getWolfOwner())) {
				return;
			}
		}
		if((entityliving instanceof EntityPlayer) && !isPVPEnabled()) {
			return;
		}
		List list = worldObj.getEntitiesWithinAABB(net.minecraft.src.EntityWolf.class, AxisAlignedBB.getBoundingBoxFromPool(posX, posY, posZ, posX + 1.0D, posY + 1.0D, posZ + 1.0D).expand(16D, 4D, 16D));
		Iterator iterator = list.iterator();
		do {
			if(!iterator.hasNext()) {
				break;
			}
			Entity entity = (Entity) iterator.next();
			EntityWolf entitywolf1 = (EntityWolf) entity;
			if(entitywolf1.isWolfTamed() && entitywolf1.getEntityToAttack() == null && username.equals(entitywolf1.getWolfOwner()) && (!flag || !entitywolf1.isWolfSitting())) {
				entitywolf1.setIsSitting(false);
				entitywolf1.setEntityToAttack(entityliving);
			}
		} while(true);
	}

	@Override
	protected void func_40125_g(int i) {
		inventory.damageArmor(i);
	}

	@Override
	protected int func_40119_ar() {
		return inventory.getTotalArmorValue();
	}

	@Override
	protected void damageEntity(DamageSource damagesource, int i) {
		if(!damagesource.unblockable() && func_35162_ad()) {
			i = 1 + i >> 1;
		}
		i = func_40115_d(damagesource, i);
		i = func_40128_b(damagesource, i);
		addExhaustion(damagesource.getHungerDamage());
		super.damageEntity(damagesource, i);
	}

	public void displayGUIFurnace(TileEntityFurnace tileentityfurnace) {
	}

	public void displayGUIDispenser(TileEntityDispenser tileentitydispenser) {
	}

	public void displayGUIEditSign(TileEntitySign tileentitysign) {
	}

	public void func_40180_a(TileEntityBrewingStand tileentitybrewingstand) {
	}

	public void useCurrentItemOnEntity(Entity entity) {
		if(entity.interact(this)) {
			return;
		}
		ItemStack itemstack = getCurrentEquippedItem();
		if(itemstack != null && (entity instanceof EntityLiving)) {
			itemstack.useItemOnEntity((EntityLiving) entity);
			if(itemstack.stackSize <= 0) {
				itemstack.onItemDestroyedByUse(this);
				destroyCurrentEquippedItem();
			}
		}
	}

	public ItemStack getCurrentEquippedItem() {
		return inventory.getCurrentItem();
	}

	public void destroyCurrentEquippedItem() {
		inventory.setInventorySlotContents(inventory.currentItem, null);
	}

	@Override
	public double getYOffset() {
		return (yOffset - 0.5F);
	}

	public void attackTargetEntityWithCurrentItem(Entity entity) {
		int i = inventory.getDamageVsEntity(entity);
		if(isPotionActive(Potion.potionDamageBoost)) {
			i += 3 << getActivePotionEffect(Potion.potionDamageBoost).getAmplifier();
		}
		if(isPotionActive(Potion.potionWeakness)) {
			i -= 2 << getActivePotionEffect(Potion.potionWeakness).getAmplifier();
		}
		int j = 0;
		int k = 0;
		if(entity instanceof EntityLiving) {
			k = EnchantmentHelper.getEnchantmentModifierLiving(inventory, (EntityLiving) entity);
			j += EnchantmentHelper.getKnockbackModifier(inventory, (EntityLiving) entity);
		}
		if(isSprinting()) {
			j++;
		}
		if(i > 0 || k > 0) {
			boolean flag = fallDistance > 0.0F && !onGround && !isOnLadder() && !isInWater() && !isPotionActive(Potion.potionBlindness) && ridingEntity == null && (entity instanceof EntityLiving);
			if(flag) {
				i += rand.nextInt(i / 2 + 2);
			}
			i += k;
			boolean flag1 = entity.attackEntityFrom(DamageSource.causePlayerDamage(this), i);
			if(flag1) {
				if(j > 0) {
					entity.addVelocity(-MathHelper.sin((rotationYaw * 3.141593F) / 180F) * j * 0.5F, 0.10000000000000001D, MathHelper.cos((rotationYaw * 3.141593F) / 180F) * j * 0.5F);
					motionX *= 0.59999999999999998D;
					motionZ *= 0.59999999999999998D;
					setSprinting(false);
				}
				if(flag) {
					onCriticalHit(entity);
				}
				if(k > 0) {
					func_40183_c(entity);
				}
				if(i >= 18) {
					triggerAchievement(AchievementList.overkill);
				}
			}
			ItemStack itemstack = getCurrentEquippedItem();
			if(itemstack != null && (entity instanceof EntityLiving)) {
				itemstack.hitEntity((EntityLiving) entity, this);
				if(itemstack.stackSize <= 0) {
					itemstack.onItemDestroyedByUse(this);
					destroyCurrentEquippedItem();
				}
			}
			if(entity instanceof EntityLiving) {
				if(entity.isEntityAlive()) {
					alertWolves((EntityLiving) entity, true);
				}
				addStat(StatList.damageDealtStat, i);
				int l = EnchantmentHelper.getFireAspectModifier(inventory, (EntityLiving) entity);
				if(l > 0) {
					entity.func_40046_d(l * 4);
				}
			}
			addExhaustion(0.3F);
		}
	}

	public void onCriticalHit(Entity entity) {
	}

	public void func_40183_c(Entity entity) {
	}

	public void respawnPlayer() {
	}

	public abstract void func_6420_o();

	public void onItemStackChanged(ItemStack itemstack) {
	}

	@Override
	public void setEntityDead() {
		super.setEntityDead();
		inventorySlots.onCraftGuiClosed(this);
		if(craftingInventory != null) {
			craftingInventory.onCraftGuiClosed(this);
		}
	}

	@Override
	public boolean isEntityInsideOpaqueBlock() {
		return !sleeping && super.isEntityInsideOpaqueBlock();
	}

	public EnumStatus sleepInBedAt(int i, int j, int k) {
		if(!worldObj.multiplayerWorld) {
			if(isPlayerSleeping() || !isEntityAlive()) {
				return EnumStatus.OTHER_PROBLEM;
			}
			if(worldObj.worldProvider.isNether) {
				return EnumStatus.NOT_POSSIBLE_HERE;
			}
			if(worldObj.isDaytime()) {
				return EnumStatus.NOT_POSSIBLE_NOW;
			}
			if(Math.abs(posX - i) > 3D || Math.abs(posY - j) > 2D || Math.abs(posZ - k) > 3D) {
				return EnumStatus.TOO_FAR_AWAY;
			}
			double d = 8D;
			double d1 = 5D;
			List list = worldObj.getEntitiesWithinAABB(net.minecraft.src.EntityMob.class, AxisAlignedBB.getBoundingBoxFromPool(i - d, j - d1, k - d, i + d, j + d1, k + d));
			if(!list.isEmpty()) {
				return EnumStatus.NOT_SAFE;
			}
		}
		setSize(0.2F, 0.2F);
		yOffset = 0.2F;
		if(worldObj.blockExists(i, j, k)) {
			int l = worldObj.getBlockMetadata(i, j, k);
			int i1 = BlockBed.getDirectionFromMetadata(l);
			float f = 0.5F;
			float f1 = 0.5F;
			switch(i1) {
			case 0: // '\0'
				f1 = 0.9F;
				break;

			case 2: // '\002'
				f1 = 0.1F;
				break;

			case 1: // '\001'
				f = 0.1F;
				break;

			case 3: // '\003'
				f = 0.9F;
				break;
			}
			func_22052_e(i1);
			setPosition(i + f, j + 0.9375F, k + f1);
		} else {
			setPosition(i + 0.5F, j + 0.9375F, k + 0.5F);
		}
		sleeping = true;
		sleepTimer = 0;
		bedChunkCoordinates = new ChunkCoordinates(i, j, k);
		motionX = motionZ = motionY = 0.0D;
		if(!worldObj.multiplayerWorld) {
			worldObj.updateAllPlayersSleepingFlag();
		}
		return EnumStatus.OK;
	}

	private void func_22052_e(int i) {
		field_22063_x = 0.0F;
		field_22061_z = 0.0F;
		switch(i) {
		case 0: // '\0'
			field_22061_z = -1.8F;
			break;

		case 2: // '\002'
			field_22061_z = 1.8F;
			break;

		case 1: // '\001'
			field_22063_x = 1.8F;
			break;

		case 3: // '\003'
			field_22063_x = -1.8F;
			break;
		}
	}

	public void wakeUpPlayer(boolean flag, boolean flag1, boolean flag2) {
		setSize(0.6F, 1.8F);
		resetHeight();
		ChunkCoordinates chunkcoordinates = bedChunkCoordinates;
		ChunkCoordinates chunkcoordinates1 = bedChunkCoordinates;
		if(chunkcoordinates != null && worldObj.getBlockId(chunkcoordinates.posX, chunkcoordinates.posY, chunkcoordinates.posZ) == Block.bed.blockID) {
			BlockBed.setBedOccupied(worldObj, chunkcoordinates.posX, chunkcoordinates.posY, chunkcoordinates.posZ, false);
			ChunkCoordinates chunkcoordinates2 = BlockBed.getNearestEmptyChunkCoordinates(worldObj, chunkcoordinates.posX, chunkcoordinates.posY, chunkcoordinates.posZ, 0);
			if(chunkcoordinates2 == null)
				chunkcoordinates2 = new ChunkCoordinates(chunkcoordinates.posX, chunkcoordinates.posY + 1, chunkcoordinates.posZ);
			setPosition(chunkcoordinates2.posX + 0.5F, chunkcoordinates2.posY + yOffset + 0.1F, chunkcoordinates2.posZ + 0.5F);
		}
		sleeping = false;
		if(!worldObj.multiplayerWorld && flag1)
			worldObj.updateAllPlayersSleepingFlag();
		if(flag)
			sleepTimer = 0;
		else
			sleepTimer = 100;
		if(flag2)
			setPlayerSpawnCoordinate(bedChunkCoordinates);
	}

	private boolean isInBed() {
		return worldObj.getBlockId(bedChunkCoordinates.posX, bedChunkCoordinates.posY, bedChunkCoordinates.posZ) == Block.bed.blockID;
	}

	public static ChunkCoordinates verifyRespawnCoordinates(World world, ChunkCoordinates chunkcoordinates) {
		IChunkProvider ichunkprovider = world.getIChunkProvider();
		ichunkprovider.loadChunk(chunkcoordinates.posX - 3 >> 4, chunkcoordinates.posZ - 3 >> 4);
		ichunkprovider.loadChunk(chunkcoordinates.posX + 3 >> 4, chunkcoordinates.posZ - 3 >> 4);
		ichunkprovider.loadChunk(chunkcoordinates.posX - 3 >> 4, chunkcoordinates.posZ + 3 >> 4);
		ichunkprovider.loadChunk(chunkcoordinates.posX + 3 >> 4, chunkcoordinates.posZ + 3 >> 4);
		if(world.getBlockId(chunkcoordinates.posX, chunkcoordinates.posY, chunkcoordinates.posZ) != Block.bed.blockID) {
			return null;
		} else {
			ChunkCoordinates chunkcoordinates1 = BlockBed.getNearestEmptyChunkCoordinates(world, chunkcoordinates.posX, chunkcoordinates.posY, chunkcoordinates.posZ, 0);
			return chunkcoordinates1;
		}
	}

	public float getBedOrientationInDegrees() {
		if(bedChunkCoordinates != null) {
			int i = worldObj.getBlockMetadata(bedChunkCoordinates.posX, bedChunkCoordinates.posY, bedChunkCoordinates.posZ);
			int j = BlockBed.getDirectionFromMetadata(i);
			switch(j) {
			case 0: // '\0'
				return 90F;

			case 1: // '\001'
				return 0.0F;

			case 2: // '\002'
				return 270F;

			case 3: // '\003'
				return 180F;
			}
		}
		return 0.0F;
	}

	@Override
	public boolean isPlayerSleeping() {
		return sleeping;
	}

	public boolean isPlayerFullyAsleep() {
		return sleeping && sleepTimer >= 100;
	}

	public int getSleepTimer() {
		return sleepTimer;
	}

	public void addChatMessage(String s) {
	}

	public ChunkCoordinates getPlayerSpawnCoordinate() {
		return playerSpawnCoordinate;
	}

	public void setPlayerSpawnCoordinate(ChunkCoordinates chunkcoordinates) {
		if(chunkcoordinates != null) {
			playerSpawnCoordinate = new ChunkCoordinates(chunkcoordinates);
		} else {
			playerSpawnCoordinate = null;
		}
	}

	public void triggerAchievement(StatBase statbase) {
		addStat(statbase, 1);
	}

	public void addStat(StatBase statbase, int i) {
	}

	@Override
	protected void jump() {
		if(flyControl.isFlying()) {
			motionY = 0.4199999868869782D * flyControl.getSpeedMultipler();
			return;
		}
		super.jump();
		addStat(StatList.jumpStat, 1);
		if(isSprinting()) {
			addExhaustion(0.8F);
		} else {
			addExhaustion(0.2F);
		}
	}

	@Override
	public void moveFlying(float f, float f1, float f2) {
		if(flyControl.isFlying() || flyControl.isSpeed()) {
			float speedSaved = speedMultipler;
			speedMultipler *= flyControl.getSpeedMultipler();
			super.moveFlying(f, f1, f2);
			speedMultipler = speedSaved;
			return;
		}
		super.moveFlying(f, f1, f2);
	}

	@Override
	public void moveEntityWithHeading(float f, float f1) {
		double d = posX;
		double d1 = posY;
		double d2 = posZ;
		if(capabilities.isFlying) {
			double d3 = motionY;
			float f2 = jumpMovementFactor;
			jumpMovementFactor = 0.05F;
			super.moveEntityWithHeading(f, f1);
			motionY = d3 * 0.59999999999999998D;
			jumpMovementFactor = f2;
		} else {
			super.moveEntityWithHeading(f, f1);
		}
		addMovementStat(posX - d, posY - d1, posZ - d2);
	}

	public void addMovementStat(double d, double d1, double d2) {
		if(ridingEntity != null) {
			return;
		}
		if(isInsideOfMaterial(Material.water)) {
			int i = Math.round(MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2) * 100F);
			if(i > 0) {
				addStat(StatList.distanceDoveStat, i);
				addExhaustion(0.015F * i * 0.01F);
			}
		} else if(isInWater()) {
			int j = Math.round(MathHelper.sqrt_double(d * d + d2 * d2) * 100F);
			if(j > 0) {
				addStat(StatList.distanceSwumStat, j);
				addExhaustion(0.015F * j * 0.01F);
			}
		} else if(isOnLadder()) {
			if(d1 > 0.0D) {
				addStat(StatList.distanceClimbedStat, (int) Math.round(d1 * 100D));
			}
		} else if(onGround) {
			int k = Math.round(MathHelper.sqrt_double(d * d + d2 * d2) * 100F);
			if(k > 0) {
				addStat(StatList.distanceWalkedStat, k);
				if(isSprinting()) {
					addExhaustion(0.09999999F * k * 0.01F);
				} else {
					addExhaustion(0.01F * k * 0.01F);
				}
			}
		} else {
			int l = Math.round(MathHelper.sqrt_double(d * d + d2 * d2) * 100F);
			if(l > 25) {
				addStat(StatList.distanceFlownStat, l);
			}
		}
	}

	private void addMountedMovementStat(double d, double d1, double d2) {
		if(ridingEntity != null) {
			int i = Math.round(MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2) * 100F);
			if(i > 0) {
				if(ridingEntity instanceof EntityMinecart) {
					addStat(StatList.distanceByMinecartStat, i);
					if(startMinecartRidingCoordinate == null) {
						startMinecartRidingCoordinate = new ChunkCoordinates(MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ));
					} else if(startMinecartRidingCoordinate.getSqDistanceTo(MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ)) >= 1000D) {
						addStat(AchievementList.onARail, 1);
					}
				} else if(ridingEntity instanceof EntityBoat) {
					addStat(StatList.distanceByBoatStat, i);
				} else if(ridingEntity instanceof EntityPig) {
					addStat(StatList.distanceByPigStat, i);
				}
			}
		}
	}

	@Override
	protected void fall(float f) {
		if(capabilities.allowFlying || flyControl.isFlying()) {
			return;
		}
		if(f >= 2.0F) {
			addStat(StatList.distanceFallenStat, (int) Math.round(f * 100D));
		}
		super.fall(f);
	}

	@Override
	public void onKillEntity(EntityLiving entityliving) {
		if(entityliving instanceof EntityMob) {
			triggerAchievement(AchievementList.killEnemy);
		}
	}

	@Override
	public void setInPortal() {
		if(timeUntilPortal > 0) {
			timeUntilPortal = 10;
			return;
		} else {
			inPortal = true;
			return;
		}
	}

	public void increaseXP(int i) {
		score += i;
		currentXP += (float) i / (float) xpBarCap();
		totalXP += i;
		while(currentXP >= 1.0F) {
			currentXP--;
			increaseLevel();
		}
	}

	public void func_40184_i(int i) {
		playerLevel -= i;
		if(playerLevel < 0) {
			playerLevel = 0;
		}
	}

	public int xpBarCap() {
		return 7 + (playerLevel * 7 >> 1);
	}

	private void increaseLevel() {
		playerLevel++;
	}

	public void addExhaustion(float f) {
		if(capabilities.disableDamage) {
			return;
		}
		if(!worldObj.multiplayerWorld) {
			foodStats.addExhaustion(f);
		}
	}

	public FoodStats getFoodStats() {
		return foodStats;
	}

	public boolean func_35197_b(boolean flag) {
		return (flag || foodStats.needFood()) && !capabilities.disableDamage;
	}

	public boolean shouldHeal() {
		return getEntityHealth() > 0 && getEntityHealth() < maxHealth;
	}

	public boolean func_35190_e(int i, int j, int k) {
		return true;
	}

	@Override
	protected int func_36001_a(EntityPlayer entityplayer) {
		int i = playerLevel * 7;
		if(i > 100) {
			return 100;
		} else {
			return i;
		}
	}

	@Override
	protected boolean func_35163_av() {
		return true;
	}

	public void func_40182_b(int i) {
	}

	public void func_41014_d(EntityPlayer entityplayer) {
		inventory.func_41022_a(entityplayer.inventory);
		health = entityplayer.health;
		foodStats = entityplayer.foodStats;
		playerLevel = entityplayer.playerLevel;
		totalXP = entityplayer.totalXP;
		currentXP = entityplayer.currentXP;
		score = entityplayer.score;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "{" + entityId + ", " + username + "}";
	}
}
