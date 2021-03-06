package net.minecraft.src;

public class BlockHalfBlockGlass extends BlockHalfBlockTextured {

	public static int end_down = ModLoader.addOverride("/terrain.png", "/gc_images/glasshalf_end_down.png");
	public static int end_up = ModLoader.addOverride("/terrain.png", "/gc_images/glasshalf_end_up.png");
	public static int end_left = ModLoader.addOverride("/terrain.png", "/gc_images/glasshalf_end_left.png");
	public static int end_right = ModLoader.addOverride("/terrain.png", "/gc_images/glasshalf_end_right.png");
	public static int line_x = ModLoader.addOverride("/terrain.png", "/gc_images/glasshalf_line_x.png");
	public static int line_z = ModLoader.addOverride("/terrain.png", "/gc_images/glasshalf_line_z.png");

	public BlockHalfBlockGlass(int i, int upTexture, int sideTexture, Block original) {
		super(i, upTexture, sideTexture, original);
	}

	@Override
	public int getBlockTexture(IBlockAccess iblockaccess, int x, int y, int z, int i) {
		int d = iblockaccess.getBlockMetadata(x, y, z) & 3;
		int t = super.getBlockTextureFromSideAndMetadata(i, d);
		boolean b[] = new boolean[6];
		if(t != tex) {
			if(i <= 1) {
				if(d == 3 || d == 2) {
					b[0] = isShouldBeConnected(iblockaccess, x - 1, y, z, i, d);
					b[1] = isShouldBeConnected(iblockaccess, x + 1, y, z, i, d);
					if(b[0] && b[1]) {
						return line_x;
					} else if(b[0]) {
						return end_right;
					} else if(b[1]) {
						return end_left;
					}
				}
				if(d == 0 || d == 1) {
					b[2] = isShouldBeConnected(iblockaccess, x, y, z - 1, i, d);
					b[3] = isShouldBeConnected(iblockaccess, x, y, z + 1, i, d);
					if(b[2] && b[3]) {
						return line_z;
					} else if(b[2]) {
						return end_down;
					} else if(b[3]) {
						return end_up;
					}
				}
			} else {
				b[0] = isShouldBeConnected(iblockaccess, x, y - 1, z, i, d);
				b[1] = isShouldBeConnected(iblockaccess, x, y + 1, z, i, d);
				if(b[0] && b[1]) {
					return line_z;
				} else if(b[0]) {
					return end_up;
				} else if(b[1]) {
					return end_down;
				}
			}
			return t;
		}
		int texture = BlockGlassNew.glass0;
		if(i <= 1) {
			b[0] = isShouldBeConnected(iblockaccess, x - 1, y, z, i, d);
			b[1] = isShouldBeConnected(iblockaccess, x + 1, y, z, i, d);
			b[2] = isShouldBeConnected(iblockaccess, x, y, z + 1, i, d);
			b[3] = isShouldBeConnected(iblockaccess, x, y, z - 1, i, d);
		} else if(i == 2) {
			b[0] = isShouldBeConnected(iblockaccess, x + 1, y, z, i, d);
			b[1] = isShouldBeConnected(iblockaccess, x - 1, y, z, i, d);
			b[2] = isShouldBeConnected(iblockaccess, x, y - 1, z, i, d);
			b[3] = isShouldBeConnected(iblockaccess, x, y + 1, z, i, d);
		} else if(i == 3) {
			b[0] = isShouldBeConnected(iblockaccess, x - 1, y, z, i, d);
			b[1] = isShouldBeConnected(iblockaccess, x + 1, y, z, i, d);
			b[2] = isShouldBeConnected(iblockaccess, x, y - 1, z, i, d);
			b[3] = isShouldBeConnected(iblockaccess, x, y + 1, z, i, d);
		} else if(i == 4) {
			b[0] = isShouldBeConnected(iblockaccess, x, y, z - 1, i, d);
			b[1] = isShouldBeConnected(iblockaccess, x, y, z + 1, i, d);
			b[2] = isShouldBeConnected(iblockaccess, x, y - 1, z, i, d);
			b[3] = isShouldBeConnected(iblockaccess, x, y + 1, z, i, d);
		} else if(i == 5) {
			b[0] = isShouldBeConnected(iblockaccess, x, y, z + 1, i, d);
			b[1] = isShouldBeConnected(iblockaccess, x, y, z - 1, i, d);
			b[2] = isShouldBeConnected(iblockaccess, x, y - 1, z, i, d);
			b[3] = isShouldBeConnected(iblockaccess, x, y + 1, z, i, d);
		}
		if(b[0] & !b[1] & !b[2] & !b[3])
			texture = BlockGlassNew.glass3;
		else if(!b[0] & b[1] & !b[2] & !b[3])
			texture = BlockGlassNew.glass1;
		else if(!b[0] & !b[1] & b[2] & !b[3])
			texture = BlockGlassNew.glass16;
		else if(!b[0] & !b[1] & !b[2] & b[3])
			texture = BlockGlassNew.glass48;
		else if(b[0] & b[1] & !b[2] & !b[3])
			texture = BlockGlassNew.glass2;
		else if(!b[0] & !b[1] & b[2] & b[3])
			texture = BlockGlassNew.glass32;
		else if(b[0] & !b[1] & b[2] & !b[3])
			texture = BlockGlassNew.glass19;
		else if(b[0] & !b[1] & !b[2] & b[3])
			texture = BlockGlassNew.glass51;
		else if(!b[0] & b[1] & b[2] & !b[3])
			texture = BlockGlassNew.glass17;
		else if(!b[0] & b[1] & !b[2] & b[3])
			texture = BlockGlassNew.glass49;
		else if(!b[0] & b[1] & b[2] & b[3])
			texture = BlockGlassNew.glass33;
		else if(b[0] & !b[1] & b[2] & b[3])
			texture = BlockGlassNew.glass35;
		else if(b[0] & b[1] & !b[2] & b[3])
			texture = BlockGlassNew.glass50;
		else if(b[0] & b[1] & b[2] & !b[3])
			texture = BlockGlassNew.glass18;
		else if(b[0] & b[1] & b[2] & b[3])
			texture = BlockGlassNew.glass34;

		if(Block.leaves.isOpaqueCube()) {
			return texture;
		}

		boolean b2[] = new boolean[6];
		if(i <= 1) {
			b2[0] = !isShouldBeConnected(iblockaccess, x + 1, y, z + 1, i, d);
			b2[1] = !isShouldBeConnected(iblockaccess, x - 1, y, z + 1, i, d);
			b2[2] = !isShouldBeConnected(iblockaccess, x + 1, y, z - 1, i, d);
			b2[3] = !isShouldBeConnected(iblockaccess, x - 1, y, z - 1, i, d);
		} else if(i == 2) {
			b2[0] = !isShouldBeConnected(iblockaccess, x - 1, y - 1, z, i, d);
			b2[1] = !isShouldBeConnected(iblockaccess, x + 1, y - 1, z, i, d);
			b2[2] = !isShouldBeConnected(iblockaccess, x - 1, y + 1, z, i, d);
			b2[3] = !isShouldBeConnected(iblockaccess, x + 1, y + 1, z, i, d);
		} else if(i == 3) {
			b2[0] = !isShouldBeConnected(iblockaccess, x + 1, y - 1, z, i, d);
			b2[1] = !isShouldBeConnected(iblockaccess, x - 1, y - 1, z, i, d);
			b2[2] = !isShouldBeConnected(iblockaccess, x + 1, y + 1, z, i, d);
			b2[3] = !isShouldBeConnected(iblockaccess, x - 1, y + 1, z, i, d);
		} else if(i == 4) {
			b2[0] = !isShouldBeConnected(iblockaccess, x, y - 1, z + 1, i, d);
			b2[1] = !isShouldBeConnected(iblockaccess, x, y - 1, z - 1, i, d);
			b2[2] = !isShouldBeConnected(iblockaccess, x, y + 1, z + 1, i, d);
			b2[3] = !isShouldBeConnected(iblockaccess, x, y + 1, z - 1, i, d);
		} else if(i == 5) {
			b2[0] = !isShouldBeConnected(iblockaccess, x, y - 1, z - 1, i, d);
			b2[1] = !isShouldBeConnected(iblockaccess, x, y - 1, z + 1, i, d);
			b2[2] = !isShouldBeConnected(iblockaccess, x, y + 1, z - 1, i, d);
			b2[3] = !isShouldBeConnected(iblockaccess, x, y + 1, z + 1, i, d);
		}

		if(texture == BlockGlassNew.glass17 && b2[0])
			texture = BlockGlassNew.glass4;
		else if(texture == BlockGlassNew.glass19 && b2[1])
			texture = BlockGlassNew.glass5;
		else if(texture == BlockGlassNew.glass49 && b2[2])
			texture = BlockGlassNew.glass20;
		else if(texture == BlockGlassNew.glass51 && b2[3])
			texture = BlockGlassNew.glass21;
		else if(texture == BlockGlassNew.glass18 && b2[0] && b2[1])
			texture = BlockGlassNew.glass7;
		else if(texture == BlockGlassNew.glass33 && b2[0] && b2[2])
			texture = BlockGlassNew.glass6;
		else if(texture == BlockGlassNew.glass35 && b2[3] && b2[1])
			texture = BlockGlassNew.glass23;
		else if(texture == BlockGlassNew.glass50 && b2[3] && b2[2])
			texture = BlockGlassNew.glass22;

		else if(texture == BlockGlassNew.glass18 && !b2[0] && b2[1])
			texture = BlockGlassNew.glass39;
		else if(texture == BlockGlassNew.glass33 && b2[0] && !b2[2])
			texture = BlockGlassNew.glass38;
		else if(texture == BlockGlassNew.glass35 && !b2[3] && b2[1])
			texture = BlockGlassNew.glass53;
		else if(texture == BlockGlassNew.glass50 && b2[3] && !b2[2])
			texture = BlockGlassNew.glass52;

		else if(texture == BlockGlassNew.glass18 && b2[0] && !b2[1])
			texture = BlockGlassNew.glass37;
		else if(texture == BlockGlassNew.glass33 && !b2[0] && b2[2])
			texture = BlockGlassNew.glass36;
		else if(texture == BlockGlassNew.glass35 && b2[3] && !b2[1])
			texture = BlockGlassNew.glass55;
		else if(texture == BlockGlassNew.glass50 && !b2[3] && b2[2])
			texture = BlockGlassNew.glass54;

		else if(texture == BlockGlassNew.glass34 && b2[0] && b2[1] && b2[2] && b2[3])
			texture = BlockGlassNew.glass58;

		else if(texture == BlockGlassNew.glass34 && !b2[0] && b2[1] && b2[2] && b2[3])
			texture = BlockGlassNew.glass9;
		else if(texture == BlockGlassNew.glass34 && b2[0] && !b2[1] && b2[2] && b2[3])
			texture = BlockGlassNew.glass25;
		else if(texture == BlockGlassNew.glass34 && b2[0] && b2[1] && !b2[2] && b2[3])
			texture = BlockGlassNew.glass8;
		else if(texture == BlockGlassNew.glass34 && b2[0] && b2[1] && b2[2] && !b2[3])
			texture = BlockGlassNew.glass24;

		else if(texture == BlockGlassNew.glass34 && b2[0] && b2[1] && !b2[2] && !b2[3])
			texture = BlockGlassNew.glass11;
		else if(texture == BlockGlassNew.glass34 && !b2[0] && !b2[1] && b2[2] && b2[3])
			texture = BlockGlassNew.glass26;
		else if(texture == BlockGlassNew.glass34 && !b2[0] && b2[1] && !b2[2] && b2[3])
			texture = BlockGlassNew.glass27;
		else if(texture == BlockGlassNew.glass34 && b2[0] && !b2[1] && b2[2] && !b2[3])
			texture = BlockGlassNew.glass10;

		else if(texture == BlockGlassNew.glass34 && b2[0] && !b2[1] && !b2[2] && b2[3])
			texture = BlockGlassNew.glass42;
		else if(texture == BlockGlassNew.glass34 && !b2[0] && b2[1] && b2[2] && !b2[3])
			texture = BlockGlassNew.glass43;

		else if(texture == BlockGlassNew.glass34 && b2[0] && !b2[1] && !b2[2] && !b2[3])
			texture = BlockGlassNew.glass40;
		else if(texture == BlockGlassNew.glass34 && !b2[0] && b2[1] && !b2[2] && !b2[3])
			texture = BlockGlassNew.glass41;
		else if(texture == BlockGlassNew.glass34 && !b2[0] && !b2[1] && b2[2] && !b2[3])
			texture = BlockGlassNew.glass56;
		else if(texture == BlockGlassNew.glass34 && !b2[0] && !b2[1] && !b2[2] && b2[3])
			texture = BlockGlassNew.glass57;
		return texture;
	}

	private boolean isShouldBeConnected(IBlockAccess iblockaccess, int x, int y, int z, int i, int d) {
		int x2 = x;
		int y2 = y;
		int z2 = z;
		if(i == 0)
			y2--;
		else if(i == 1)
			y2++;
		else if(i == 2)
			z2--;
		else if(i == 3)
			z2++;
		else if(i == 4)
			x2--;
		else if(i == 5)
			x2++;
		return iblockaccess.getBlockId(x, y, z) == blockID && (iblockaccess.getBlockMetadata(x, y, z) & 3) == (d & 3);
	}
}
