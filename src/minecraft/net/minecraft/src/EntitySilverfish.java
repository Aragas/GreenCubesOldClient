// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;

// Referenced classes of package net.minecraft.src:
//            EntityMob, World, EntityDamageSource, Entity, 
//            AxisAlignedBB, DamageSource, MathHelper, Block, 
//            Facing, BlockSilverfish, EnumCreatureAttribute, NBTTagCompound

public class EntitySilverfish extends EntityMob {

	private int field_35189_a;

	public EntitySilverfish(World world) {
		super(world);
		texture = "/mob/silverfish.png";
		setSize(0.3F, 0.7F);
		moveSpeed = 0.6F;
		attackStrength = 1;
	}

	@Override
	public int getMaxHealth() {
		return 8;
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	protected Entity findPlayerToAttack() {
		double d = 8D;
		return worldObj.getClosestVulnerablePlayerToEntity(this, d);
	}

	@Override
	protected String getLivingSound() {
		return "mob.silverfish.say";
	}

	@Override
	protected String getHurtSound() {
		return "mob.silverfish.hit";
	}

	@Override
	protected String getDeathSound() {
		return "mob.silverfish.kill";
	}

	@Override
	public boolean attackEntityFrom(DamageSource damagesource, int i) {
		if(field_35189_a <= 0 && (damagesource instanceof EntityDamageSource)) {
			field_35189_a = 20;
		}
		return super.attackEntityFrom(damagesource, i);
	}

	@Override
	protected void attackEntity(Entity entity, float f) {
		if(attackTime <= 0 && f < 1.2F && entity.boundingBox.maxY > boundingBox.minY && entity.boundingBox.minY < boundingBox.maxY) {
			attackTime = 20;
			entity.attackEntityFrom(DamageSource.causeMobDamage(this), attackStrength);
		}
	}

	@Override
	protected void func_41002_a(int i, int j, int k, int l) {
		worldObj.playSoundAtEntity(this, "mob.silverfish.step", 1.0F, 1.0F);
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
		return 0;
	}

	@Override
	public void onUpdate() {
		renderYawOffset = rotationYaw;
		super.onUpdate();
	}

	@Override
	protected void updateEntityActionState() {
		super.updateEntityActionState();
		if(worldObj.multiplayerWorld) {
			return;
		}
		if(field_35189_a > 0) {
			field_35189_a--;
			if(field_35189_a == 0) {
				int i = MathHelper.floor_double(posX);
				int k = MathHelper.floor_double(posY);
				int i1 = MathHelper.floor_double(posZ);
				boolean flag = false;
				for(int l1 = 0; !flag && l1 <= 5 && l1 >= -5; l1 = l1 > 0 ? 0 - l1 : 1 - l1) {
					for(int j2 = 0; !flag && j2 <= 10 && j2 >= -10; j2 = j2 > 0 ? 0 - j2 : 1 - j2) {
						for(int k2 = 0; !flag && k2 <= 10 && k2 >= -10; k2 = k2 > 0 ? 0 - k2 : 1 - k2) {
							int l2 = worldObj.getBlockId(i + j2, k + l1, i1 + k2);
							if(l2 != Block.silverfish.blockID) {
								continue;
							}
							worldObj.playAuxSFX(2001, i + j2, k + l1, i1 + k2, Block.silverfish.blockID + worldObj.getBlockMetadata(i + j2, k + l1, i1 + k2) * 4096); // GreenCubes: 256 -> 4096
							worldObj.setBlockWithNotify(i + j2, k + l1, i1 + k2, 0);
							Block.silverfish.onBlockDestroyedByPlayer(worldObj, i + j2, k + l1, i1 + k2, 0);
							if(!rand.nextBoolean()) {
								continue;
							}
							flag = true;
							break;
						}

					}

				}

			}
		}
		if(entityToAttack == null && !hasPath()) {
			int j = MathHelper.floor_double(posX);
			int l = MathHelper.floor_double(posY + 0.5D);
			int j1 = MathHelper.floor_double(posZ);
			int k1 = rand.nextInt(6);
			int i2 = worldObj.getBlockId(j + Facing.offsetsXForSide[k1], l + Facing.offsetsYForSide[k1], j1 + Facing.offsetsZForSide[k1]);
			if(BlockSilverfish.getPosingIdByMetadata(i2)) {
				worldObj.setBlockAndMetadataWithNotify(j + Facing.offsetsXForSide[k1], l + Facing.offsetsYForSide[k1], j1 + Facing.offsetsZForSide[k1], Block.silverfish.blockID, BlockSilverfish.func_35304_f(i2));
				spawnExplosionParticle();
				setEntityDead();
			} else {
				updateWanderPath();
			}
		} else if(entityToAttack != null && !hasPath()) {
			entityToAttack = null;
		}
	}

	@Override
	protected float getBlockPathWeight(int i, int j, int k) {
		if(worldObj.getBlockId(i, j - 1, k) == Block.stone.blockID) {
			return 10F;
		} else {
			return super.getBlockPathWeight(i, j, k);
		}
	}

	@Override
	protected boolean func_40147_Y() {
		return true;
	}

	@Override
	public boolean getCanSpawnHere() {
		if(super.getCanSpawnHere()) {
			EntityPlayer entityplayer = worldObj.getClosestPlayerToEntity(this, 5D);
			return entityplayer == null;
		} else {
			return false;
		}
	}

	@Override
	public EnumCreatureAttribute func_40124_t() {
		return EnumCreatureAttribute.ARTHROPOD;
	}
}
