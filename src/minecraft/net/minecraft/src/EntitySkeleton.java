// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;

// Referenced classes of package net.minecraft.src:
//            EntityMob, DamageSource, EntityArrow, EntityPlayer, 
//            AchievementList, World, MathHelper, Entity, 
//            Item, EnumCreatureAttribute, ItemStack, NBTTagCompound

public class EntitySkeleton extends EntityMob {

	private static final ItemStack defaultHeldItem;

	public EntitySkeleton(World world) {
		super(world);
		texture = "/mob/skeleton.png";
		this.health = this.maxHealth = 20;
	}

	@Override
	protected String getLivingSound() {
		return "mob.skeleton";
	}

	@Override
	protected String getHurtSound() {
		return "mob.skeletonhurt";
	}

	@Override
	protected String getDeathSound() {
		return "mob.skeletonhurt";
	}

	@Override
	public boolean attackEntityFrom(DamageSource damagesource, int i) {
		return super.attackEntityFrom(damagesource, i);
	}

	@Override
	public void onDeath(DamageSource damagesource) {
		super.onDeath(damagesource);
		if((damagesource.getSourceOfDamage() instanceof EntityArrow) && (damagesource.getEntity() instanceof EntityPlayer)) {
			EntityPlayer entityplayer = (EntityPlayer) damagesource.getEntity();
			double d = entityplayer.posX - posX;
			double d1 = entityplayer.posZ - posZ;
			if(d * d + d1 * d1 >= 2500D) {
				entityplayer.triggerAchievement(AchievementList.snipeSkeleton);
			}
		}
	}

	@Override
	public void onLivingUpdate() {
		if(worldObj.isDaytime() && !worldObj.multiplayerWorld) {
			float f = getEntityBrightness(1.0F);
			if(f > 0.5F && worldObj.canBlockSeeTheSky(MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ)) && rand.nextFloat() * 30F < (f - 0.4F) * 2.0F) {
				func_40046_d(8);
			}
		}
		super.onLivingUpdate();
	}

	@Override
	protected void attackEntity(Entity entity, float f) {
		if(f < 10F) {
			double d = entity.posX - posX;
			double d1 = entity.posZ - posZ;
			if(attackTime == 0) {
				EntityArrow entityarrow = new EntityArrow(worldObj, this, 1.0F);
				double d2 = (entity.posY + entity.getEyeHeight()) - 0.69999998807907104D - entityarrow.posY;
				float f1 = MathHelper.sqrt_double(d * d + d1 * d1) * 0.2F;
				worldObj.playSoundAtEntity(this, "random.bow", 1.0F, 1.0F / (rand.nextFloat() * 0.4F + 0.8F));
				worldObj.entityJoinedWorld(entityarrow);
				entityarrow.setArrowHeading(d, d2 + f1, d1, 1.6F, 12F);
				attackTime = 60;
			}
			rotationYaw = (float) ((Math.atan2(d1, d) * 180D) / 3.1415927410125732D) - 90F;
			hasAttacked = true;
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
	}

	@Override
	protected int getDropItemId() {
		return Item.arrow.shiftedIndex;
	}

	@Override
	protected void dropFewItems(boolean flag, int i) {
		int j = rand.nextInt(3 + i);
		for(int k = 0; k < j; k++) {
			dropItem(Item.arrow.shiftedIndex, 1);
		}

		j = rand.nextInt(3 + i);
		for(int l = 0; l < j; l++) {
			dropItem(Item.bone.shiftedIndex, 1);
		}

	}

	@Override
	public ItemStack getHeldItem() {
		return super.getHeldItem() == null ? defaultHeldItem : super.getHeldItem();
	}

	@Override
	public EnumCreatureAttribute func_40124_t() {
		return EnumCreatureAttribute.UNDEAD;
	}

	static {
		defaultHeldItem = new ItemStack(Item.bow, 1);
	}
}
