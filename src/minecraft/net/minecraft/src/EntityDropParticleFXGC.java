// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;

// Referenced classes of package net.minecraft.src:
//            EntityFX, Material, World, MathHelper, 
//            BlockFluid, Tessellator

public class EntityDropParticleFXGC extends EntityFXGC {

	private Material field_40103_a;
	private int field_40104_aw;

	public EntityDropParticleFXGC(World world, double d, double d1, double d2, Material material) {
		super(world, d, d1, d2, 0.0D, 0.0D, 0.0D);
		motionX = motionY = motionZ = 0.0D;
		if(material == Material.water) {
			particleRed = 0.0F;
			particleGreen = 0.0F;
			particleBlue = 1.0F;
		} else {
			particleRed = 1.0F;
			particleGreen = 0.0F;
			particleBlue = 0.0F;
		}
		setTextureIndex(113);
		setSize(0.01F, 0.01F);
		particleGravity = 0.06F;
		field_40103_a = material;
		field_40104_aw = 40;
		particleMaxAge = (int) (64D / (Math.random() * 0.80000000000000004D + 0.20000000000000001D));
		motionX = motionY = motionZ = 0.0D;
	}

	@Override
	public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5) {
		super.renderParticle(tessellator, f, f1, f2, f3, f4, f5);
	}

	@Override
	public int getEntityBrightnessForRender(float f) {
		if(field_40103_a == Material.water) {
			return super.getEntityBrightnessForRender(f);
		} else {
			return 257;
		}
	}

	@Override
	public float getEntityBrightness(float f) {
		if(field_40103_a == Material.water) {
			return super.getEntityBrightness(f);
		} else {
			return 1.0F;
		}
	}

	@Override
	public void onUpdate() {
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		if(field_40103_a == Material.water) {
			particleRed = 0.2F;
			particleGreen = 0.3F;
			particleBlue = 1.0F;
		} else {
			particleRed = 1.0F;
			particleGreen = 16F / ((40 - field_40104_aw) + 16);
			particleBlue = 4F / ((40 - field_40104_aw) + 8);
		}
		motionY -= particleGravity;
		if(field_40104_aw-- > 0) {
			motionX *= 0.02D;
			motionY *= 0.02D;
			motionZ *= 0.02D;
			setTextureIndex(113);
		} else {
			setTextureIndex(112);
		}
		moveEntity(motionX, motionY, motionZ);
		motionX *= 0.98000001907348633D;
		motionY *= 0.98000001907348633D;
		motionZ *= 0.98000001907348633D;
		if(particleMaxAge-- <= 0) {
			setEntityDead();
		}
		if(onGround) {
			if(field_40103_a == Material.water) {
				setEntityDead();
				worldObj.spawnParticle("splash", posX, posY, posZ, 0.0D, 0.0D, 0.0D);
			} else {
				setTextureIndex(114);
			}
			motionX *= 0.69999998807907104D;
			motionZ *= 0.69999998807907104D;
		}
		Material material = worldObj.getBlockMaterial(MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ));
		if(material.getIsLiquid() || material.isSolid()) {
			double d = (MathHelper.floor_double(posY) + 1) - BlockFluid.getFluidHeightPercent(worldObj.getBlockMetadata(MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ)));
			if(posY < d) {
				setEntityDead();
			}
		}
	}
}
