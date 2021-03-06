package net.minecraft.src;

public class EntityBluedustFXGC extends EntityFXGC {

	public EntityBluedustFXGC(World world, double d, double d1, double d2, double d3, double d4, double d5) {
		super(world, d, d1, d2, d3, d4, d5);
		particleGravity = -0.03F;
		setTextureIndex(0);
		if(d4 == 0.0D && (d3 != 0.0D || d5 != 0.0D)) {
			motionX = d3;
			motionY = d4;
			motionZ = d5;
		}
		motionX *= 0.109999999552965164D;
		motionY = Math.abs(motionY);
		motionY *= 0.209999999552965164D;
		motionZ *= 0.109999999552965164D;
		particleMaxAge = (int) (7D / (Math.random() * 0.80000000000000004D + 0.20000000000000001D));
		int i = rand.nextInt(4);
		chooseColor(i);
	}

	public void chooseColor(int i) {
		if(i == 0) {
			setColor(.0F, .6F, .75F);
		}
		if(i == 1) {
			setColor(.0F, .69F, .87F);
		}
		if(i == 2) {
			setColor(.12F, .74F, 1.0F);
		}
		if(i == 3) {
			setColor(.30F, .78F, .99F);
		}

	}

	@Override
	public void onUpdate() {
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		if(particleAge++ >= particleMaxAge) {
			setEntityDead();
		}
		setTextureIndex(particleAge < particleMaxAge / 2 ? 1 : 0);
		motionY -= 0.040000000000000001D * particleGravity;
		moveEntity(motionX, motionY, motionZ);
		if(posY == prevPosY) {
			motionX *= 1.1000000000000001D;
			motionZ *= 1.1000000000000001D;
		}
		motionX *= 0.95999997854232788D;
		motionY *= 0.95999997854232788D;
		motionZ *= 0.95999997854232788D;
		if(onGround) {
			setEntityDead();
		}
	}
}
