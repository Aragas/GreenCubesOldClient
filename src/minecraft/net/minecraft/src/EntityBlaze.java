// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;

// Referenced classes of package net.minecraft.src:
//            EntityMob, DataWatcher, World, DamageSource, 
//            Entity, AxisAlignedBB, MathHelper, EntitySmallFireball, 
//            Item, NBTTagCompound

public class EntityBlaze extends EntityMob {

	private float field_40154_a;
	private int field_40153_b;
	private int field_40152_d;

	public EntityBlaze(World world) {
		super(world);
		field_40154_a = 0.5F;
		texture = "/mob/fire.png";
		isImmuneToFire = true;
		attackStrength = 6;
		field_35171_bJ = 10;
		this.health = this.maxHealth = 20;
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataWatcher.addObject(16, new Byte((byte) 0));
	}

	@Override
	protected String getLivingSound() {
		return "mob.blaze.breathe";
	}

	@Override
	protected String getHurtSound() {
		return "mob.blaze.hit";
	}

	@Override
	protected String getDeathSound() {
		return "mob.blaze.death";
	}

	@Override
	public boolean attackEntityFrom(DamageSource damagesource, int i) {
		return super.attackEntityFrom(damagesource, i);
	}

	@Override
	public void onDeath(DamageSource damagesource) {
		super.onDeath(damagesource);
	}

	@Override
	public int getEntityBrightnessForRender(float f) {
		return 0xf000f0;
	}

	@Override
	public float getEntityBrightness(float f) {
		return 1.0F;
	}

	@Override
	public void onLivingUpdate() {
		if(!worldObj.multiplayerWorld) {
			if(isWet()) {
				attackEntityFrom(DamageSource.drown, 1);
			}
			field_40153_b--;
			if(field_40153_b <= 0) {
				field_40153_b = 100;
				field_40154_a = 0.5F + (float) rand.nextGaussian() * 3F;
			}
			if(getEntityToAttack() != null && getEntityToAttack().posY + getEntityToAttack().getEyeHeight() > posY + getEyeHeight() + field_40154_a) {
				motionY = motionY + (0.30000001192092896D - motionY) * 0.30000001192092896D;
			}
		}
		if(rand.nextInt(24) == 0) {
			worldObj.playSoundEffect(posX + 0.5D, posY + 0.5D, posZ + 0.5D, "fire.fire", 1.0F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.3F);
		}
		if(!onGround && motionY < 0.0D) {
			motionY *= 0.59999999999999998D;
		}
		for(int i = 0; i < 2; i++) {
			worldObj.spawnParticle("largesmoke", posX + (rand.nextDouble() - 0.5D) * width, posY + rand.nextDouble() * height, posZ + (rand.nextDouble() - 0.5D) * width, 0.0D, 0.0D, 0.0D);
		}

		super.onLivingUpdate();
	}

	@Override
	protected void attackEntity(Entity entity, float f) {
		if(attackTime <= 0 && f < 2.0F && entity.boundingBox.maxY > boundingBox.minY && entity.boundingBox.minY < boundingBox.maxY) {
			attackTime = 20;
			attackEntityAsMob(entity);
		} else if(f < 30F) {
			double d = entity.posX - posX;
			double d1 = (entity.boundingBox.minY + (entity.height / 2.0F)) - (posY + (height / 2.0F));
			double d2 = entity.posZ - posZ;
			if(attackTime == 0) {
				field_40152_d++;
				if(field_40152_d == 1) {
					attackTime = 60;
					func_40150_a(true);
				} else if(field_40152_d <= 4) {
					attackTime = 6;
				} else {
					attackTime = 100;
					field_40152_d = 0;
					func_40150_a(false);
				}
				if(field_40152_d > 1) {
					float f1 = MathHelper.sqrt_float(f) * 0.5F;
					worldObj.playAuxSFXAtEntity(null, 1009, (int) posX, (int) posY, (int) posZ, 0);
					for(int i = 0; i < 1; i++) {
						EntitySmallFireball entitysmallfireball = new EntitySmallFireball(worldObj, this, d + rand.nextGaussian() * f1, d1, d2 + rand.nextGaussian() * f1);
						entitysmallfireball.posY = posY + (height / 2.0F) + 0.5D;
						worldObj.entityJoinedWorld(entitysmallfireball);
					}

				}
			}
			rotationYaw = (float) ((Math.atan2(d2, d) * 180D) / 3.1415927410125732D) - 90F;
			hasAttacked = true;
		}
	}

	@Override
	protected void fall(float f) {
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
		return Item.blazeRod.shiftedIndex;
	}

	@Override
	public boolean isBurning() {
		return func_40151_ac();
	}

	@Override
	protected void dropFewItems(boolean flag, int i) {
		if(flag) {
			int j = rand.nextInt(2 + i);
			for(int k = 0; k < j; k++) {
				dropItem(Item.blazeRod.shiftedIndex, 1);
			}

		}
	}

	public boolean func_40151_ac() {
		return (dataWatcher.getWatchableObjectByte(16) & 1) != 0;
	}

	public void func_40150_a(boolean flag) {
		byte byte0 = dataWatcher.getWatchableObjectByte(16);
		if(flag) {
			byte0 |= 1;
		} else {
			byte0 &= 0xfe;
		}
		dataWatcher.updateObject(16, Byte.valueOf(byte0));
	}

	@Override
	protected boolean func_40147_Y() {
		return true;
	}
}
