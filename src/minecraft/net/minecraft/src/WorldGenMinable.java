// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;

import java.util.Random;

// Referenced classes of package net.minecraft.src:
//            WorldGenerator, MathHelper, World, Block

public class WorldGenMinable extends WorldGenerator {

	private int minableBlockId;
	private int numberOfBlocks;

	public WorldGenMinable(int i, int j) {
		minableBlockId = i;
		numberOfBlocks = j;
	}

	@Override
	public boolean generate(World world, Random random, int i, int j, int k) {
		float f = random.nextFloat() * 3.141593F;
		double d = (i + 8) + (MathHelper.sin(f) * numberOfBlocks) / 8F;
		double d1 = (i + 8) - (MathHelper.sin(f) * numberOfBlocks) / 8F;
		double d2 = (k + 8) + (MathHelper.cos(f) * numberOfBlocks) / 8F;
		double d3 = (k + 8) - (MathHelper.cos(f) * numberOfBlocks) / 8F;
		double d4 = (j + random.nextInt(3)) - 2;
		double d5 = (j + random.nextInt(3)) - 2;
		for(int l = 0; l <= numberOfBlocks; l++) {
			double d6 = d + ((d1 - d) * l) / numberOfBlocks;
			double d7 = d4 + ((d5 - d4) * l) / numberOfBlocks;
			double d8 = d2 + ((d3 - d2) * l) / numberOfBlocks;
			double d9 = (random.nextDouble() * numberOfBlocks) / 16D;
			double d10 = (MathHelper.sin((l * 3.141593F) / numberOfBlocks) + 1.0F) * d9 + 1.0D;
			double d11 = (MathHelper.sin((l * 3.141593F) / numberOfBlocks) + 1.0F) * d9 + 1.0D;
			int i1 = MathHelper.floor_double(d6 - d10 / 2D);
			int j1 = MathHelper.floor_double(d7 - d11 / 2D);
			int k1 = MathHelper.floor_double(d8 - d10 / 2D);
			int l1 = MathHelper.floor_double(d6 + d10 / 2D);
			int i2 = MathHelper.floor_double(d7 + d11 / 2D);
			int j2 = MathHelper.floor_double(d8 + d10 / 2D);
			for(int k2 = i1; k2 <= l1; k2++) {
				double d12 = ((k2 + 0.5D) - d6) / (d10 / 2D);
				if(d12 * d12 >= 1.0D) {
					continue;
				}
				for(int l2 = j1; l2 <= i2; l2++) {
					double d13 = ((l2 + 0.5D) - d7) / (d11 / 2D);
					if(d12 * d12 + d13 * d13 >= 1.0D) {
						continue;
					}
					for(int i3 = k1; i3 <= j2; i3++) {
						double d14 = ((i3 + 0.5D) - d8) / (d10 / 2D);
						if(d12 * d12 + d13 * d13 + d14 * d14 < 1.0D && world.getBlockId(k2, l2, i3) == Block.stone.blockID) {
							world.setBlock(k2, l2, i3, minableBlockId);
						}
					}

				}

			}

		}

		return true;
	}
}
