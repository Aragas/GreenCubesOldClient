package net.minecraft.src;

public class EntityFireworkOverlayFXGC extends EntityFXGC {

	protected EntityFireworkOverlayFXGC(World par1World, double par2, double par4, double par6) {
		super(par1World, par2, par4, par6, 0, 0, 0);
		this.particleMaxAge = 4;
	}

	@Override
	public void renderParticle(Tessellator par1Tessellator, float par2, float par3, float par4, float par5, float par6, float par7) {
		float var8 = 0.25F;
		float var9 = var8 + 0.25F;
		float var10 = 0.125F;
		float var11 = var10 + 0.25F;
		float var12 = 7.1F * MathHelper.sin((this.particleAge + par2 - 1.0F) * 0.25F * (float) Math.PI);
		this.particleAlpha = 0.6F - (this.particleAge + par2 - 1.0F) * 0.25F * 0.5F;
		float var13 = (float) (this.prevPosX + (this.posX - this.prevPosX) * par2 - interpPosX);
		float var14 = (float) (this.prevPosY + (this.posY - this.prevPosY) * par2 - interpPosY);
		float var15 = (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * par2 - interpPosZ);
		par1Tessellator.setColorRGBA_F(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha);
		par1Tessellator.addVertexWithUV((var13 - par3 * var12 - par6 * var12), (var14 - par4 * var12), (var15 - par5 * var12 - par7 * var12), var9, var11);
		par1Tessellator.addVertexWithUV((var13 - par3 * var12 + par6 * var12), (var14 + par4 * var12), (var15 - par5 * var12 + par7 * var12), var9, var10);
		par1Tessellator.addVertexWithUV((var13 + par3 * var12 + par6 * var12), (var14 + par4 * var12), (var15 + par5 * var12 + par7 * var12), var8, var10);
		par1Tessellator.addVertexWithUV((var13 + par3 * var12 - par6 * var12), (var14 - par4 * var12), (var15 + par5 * var12 - par7 * var12), var8, var11);
	}
}
