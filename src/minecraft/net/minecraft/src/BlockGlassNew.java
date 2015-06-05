// This is part of Connected Textures Mod for Minecraft
// Not recommended for watching to children, pregnant women and people with weak nerves.
// File generated by Ctrl+c Ctrl+v magic
// License: GPLv3
// Official thread: http://www.minecraftforum.net/index.php?showtopic=228862
// Author: morpheus
// Author email: morpheus(at)iname(dot)com
package net.minecraft.src;

import java.util.Random;

public class BlockGlassNew extends BlockGlass {

	public static int glass0 = ModLoader.addOverride("/terrain.png", "/gc_images/glass0.png");
	public static int glass1 = ModLoader.addOverride("/terrain.png", "/gc_images/glass1.png");
	public static int glass2 = ModLoader.addOverride("/terrain.png", "/gc_images/glass2.png");
	public static int glass3 = ModLoader.addOverride("/terrain.png", "/gc_images/glass3.png");
	public static int glass4 = ModLoader.addOverride("/terrain.png", "/gc_images/glass4.png");
	public static int glass5 = ModLoader.addOverride("/terrain.png", "/gc_images/glass5.png");
	public static int glass6 = ModLoader.addOverride("/terrain.png", "/gc_images/glass6.png");
	public static int glass7 = ModLoader.addOverride("/terrain.png", "/gc_images/glass7.png");
	public static int glass8 = ModLoader.addOverride("/terrain.png", "/gc_images/glass8.png");
	public static int glass9 = ModLoader.addOverride("/terrain.png", "/gc_images/glass9.png");
	public static int glass10 = ModLoader.addOverride("/terrain.png", "/gc_images/glass10.png");
	public static int glass11 = ModLoader.addOverride("/terrain.png", "/gc_images/glass11.png");
	public static int glass16 = ModLoader.addOverride("/terrain.png", "/gc_images/glass16.png");
	public static int glass17 = ModLoader.addOverride("/terrain.png", "/gc_images/glass17.png");
	public static int glass18 = ModLoader.addOverride("/terrain.png", "/gc_images/glass18.png");
	public static int glass19 = ModLoader.addOverride("/terrain.png", "/gc_images/glass19.png");
	public static int glass20 = ModLoader.addOverride("/terrain.png", "/gc_images/glass20.png");
	public static int glass21 = ModLoader.addOverride("/terrain.png", "/gc_images/glass21.png");
	public static int glass22 = ModLoader.addOverride("/terrain.png", "/gc_images/glass22.png");
	public static int glass23 = ModLoader.addOverride("/terrain.png", "/gc_images/glass23.png");
	public static int glass24 = ModLoader.addOverride("/terrain.png", "/gc_images/glass24.png");
	public static int glass25 = ModLoader.addOverride("/terrain.png", "/gc_images/glass25.png");
	public static int glass26 = ModLoader.addOverride("/terrain.png", "/gc_images/glass26.png");
	public static int glass27 = ModLoader.addOverride("/terrain.png", "/gc_images/glass27.png");
	public static int glass32 = ModLoader.addOverride("/terrain.png", "/gc_images/glass32.png");
	public static int glass33 = ModLoader.addOverride("/terrain.png", "/gc_images/glass33.png");
	public static int glass34 = ModLoader.addOverride("/terrain.png", "/gc_images/glass34.png");
	public static int glass35 = ModLoader.addOverride("/terrain.png", "/gc_images/glass35.png");
	public static int glass36 = ModLoader.addOverride("/terrain.png", "/gc_images/glass36.png");
	public static int glass37 = ModLoader.addOverride("/terrain.png", "/gc_images/glass37.png");
	public static int glass38 = ModLoader.addOverride("/terrain.png", "/gc_images/glass38.png");
	public static int glass39 = ModLoader.addOverride("/terrain.png", "/gc_images/glass39.png");
	public static int glass40 = ModLoader.addOverride("/terrain.png", "/gc_images/glass40.png");
	public static int glass41 = ModLoader.addOverride("/terrain.png", "/gc_images/glass41.png");
	public static int glass42 = ModLoader.addOverride("/terrain.png", "/gc_images/glass42.png");
	public static int glass43 = ModLoader.addOverride("/terrain.png", "/gc_images/glass43.png");
	public static int glass48 = ModLoader.addOverride("/terrain.png", "/gc_images/glass48.png");
	public static int glass49 = ModLoader.addOverride("/terrain.png", "/gc_images/glass49.png");
	public static int glass50 = ModLoader.addOverride("/terrain.png", "/gc_images/glass50.png");
	public static int glass51 = ModLoader.addOverride("/terrain.png", "/gc_images/glass51.png");
	public static int glass52 = ModLoader.addOverride("/terrain.png", "/gc_images/glass52.png");
	public static int glass53 = ModLoader.addOverride("/terrain.png", "/gc_images/glass53.png");
	public static int glass54 = ModLoader.addOverride("/terrain.png", "/gc_images/glass54.png");
	public static int glass55 = ModLoader.addOverride("/terrain.png", "/gc_images/glass55.png");
	public static int glass56 = ModLoader.addOverride("/terrain.png", "/gc_images/glass56.png");
	public static int glass57 = ModLoader.addOverride("/terrain.png", "/gc_images/glass57.png");
	public static int glass58 = ModLoader.addOverride("/terrain.png", "/gc_images/glass58.png");

	public BlockGlassNew(int i, int j, Material material, boolean flag) {
		super(i, j, material, flag);
	}

	@Override
	public int quantityDropped(Random random) {
		return 0;
	}

	@Override
	public int getRenderType() {
		return 0;
	}

	@Override
	public int getBlockTexture(IBlockAccess iblockaccess, int x, int y, int z, int i) {
		int data = iblockaccess.getBlockMetadata(x, y, z);
		int texture = glass0;
		boolean b[] = new boolean[6];
		if(i <= 1) {
			b[0] = isShouldBeConnected(iblockaccess, x - 1, y, z, i, data);
			b[1] = isShouldBeConnected(iblockaccess, x + 1, y, z, i, data);
			b[2] = isShouldBeConnected(iblockaccess, x, y, z + 1, i, data);
			b[3] = isShouldBeConnected(iblockaccess, x, y, z - 1, i, data);
		} else if(i == 2) {
			b[0] = isShouldBeConnected(iblockaccess, x + 1, y, z, i, data);
			b[1] = isShouldBeConnected(iblockaccess, x - 1, y, z, i, data);
			b[2] = isShouldBeConnected(iblockaccess, x, y - 1, z, i, data);
			b[3] = isShouldBeConnected(iblockaccess, x, y + 1, z, i, data);
		} else if(i == 3) {
			b[0] = isShouldBeConnected(iblockaccess, x - 1, y, z, i, data);
			b[1] = isShouldBeConnected(iblockaccess, x + 1, y, z, i, data);
			b[2] = isShouldBeConnected(iblockaccess, x, y - 1, z, i, data);
			b[3] = isShouldBeConnected(iblockaccess, x, y + 1, z, i, data);
		} else if(i == 4) {
			b[0] = isShouldBeConnected(iblockaccess, x, y, z - 1, i, data);
			b[1] = isShouldBeConnected(iblockaccess, x, y, z + 1, i, data);
			b[2] = isShouldBeConnected(iblockaccess, x, y - 1, z, i, data);
			b[3] = isShouldBeConnected(iblockaccess, x, y + 1, z, i, data);
		} else if(i == 5) {
			b[0] = isShouldBeConnected(iblockaccess, x, y, z + 1, i, data);
			b[1] = isShouldBeConnected(iblockaccess, x, y, z - 1, i, data);
			b[2] = isShouldBeConnected(iblockaccess, x, y - 1, z, i, data);
			b[3] = isShouldBeConnected(iblockaccess, x, y + 1, z, i, data);
		}
		if(b[0] & !b[1] & !b[2] & !b[3])
			texture = glass3;
		else if(!b[0] & b[1] & !b[2] & !b[3])
			texture = glass1;
		else if(!b[0] & !b[1] & b[2] & !b[3])
			texture = glass16;
		else if(!b[0] & !b[1] & !b[2] & b[3])
			texture = glass48;
		else if(b[0] & b[1] & !b[2] & !b[3])
			texture = glass2;
		else if(!b[0] & !b[1] & b[2] & b[3])
			texture = glass32;
		else if(b[0] & !b[1] & b[2] & !b[3])
			texture = glass19;
		else if(b[0] & !b[1] & !b[2] & b[3])
			texture = glass51;
		else if(!b[0] & b[1] & b[2] & !b[3])
			texture = glass17;
		else if(!b[0] & b[1] & !b[2] & b[3])
			texture = glass49;
		else if(!b[0] & b[1] & b[2] & b[3])
			texture = glass33;
		else if(b[0] & !b[1] & b[2] & b[3])
			texture = glass35;
		else if(b[0] & b[1] & !b[2] & b[3])
			texture = glass50;
		else if(b[0] & b[1] & b[2] & !b[3])
			texture = glass18;
		else if(b[0] & b[1] & b[2] & b[3])
			texture = glass34;

		if(Block.leaves.isOpaqueCube()) {
			return texture;
		}

		boolean b2[] = new boolean[6];
		if(i <= 1) {
			b2[0] = !isShouldBeConnected(iblockaccess, x + 1, y, z + 1, i, data);
			b2[1] = !isShouldBeConnected(iblockaccess, x - 1, y, z + 1, i, data);
			b2[2] = !isShouldBeConnected(iblockaccess, x + 1, y, z - 1, i, data);
			b2[3] = !isShouldBeConnected(iblockaccess, x - 1, y, z - 1, i, data);
		} else if(i == 2) {
			b2[0] = !isShouldBeConnected(iblockaccess, x - 1, y - 1, z, i, data);
			b2[1] = !isShouldBeConnected(iblockaccess, x + 1, y - 1, z, i, data);
			b2[2] = !isShouldBeConnected(iblockaccess, x - 1, y + 1, z, i, data);
			b2[3] = !isShouldBeConnected(iblockaccess, x + 1, y + 1, z, i, data);
		} else if(i == 3) {
			b2[0] = !isShouldBeConnected(iblockaccess, x + 1, y - 1, z, i, data);
			b2[1] = !isShouldBeConnected(iblockaccess, x - 1, y - 1, z, i, data);
			b2[2] = !isShouldBeConnected(iblockaccess, x + 1, y + 1, z, i, data);
			b2[3] = !isShouldBeConnected(iblockaccess, x - 1, y + 1, z, i, data);
		} else if(i == 4) {
			b2[0] = !isShouldBeConnected(iblockaccess, x, y - 1, z + 1, i, data);
			b2[1] = !isShouldBeConnected(iblockaccess, x, y - 1, z - 1, i, data);
			b2[2] = !isShouldBeConnected(iblockaccess, x, y + 1, z + 1, i, data);
			b2[3] = !isShouldBeConnected(iblockaccess, x, y + 1, z - 1, i, data);
		} else if(i == 5) {
			b2[0] = !isShouldBeConnected(iblockaccess, x, y - 1, z - 1, i, data);
			b2[1] = !isShouldBeConnected(iblockaccess, x, y - 1, z + 1, i, data);
			b2[2] = !isShouldBeConnected(iblockaccess, x, y + 1, z - 1, i, data);
			b2[3] = !isShouldBeConnected(iblockaccess, x, y + 1, z + 1, i, data);
		}

		if(texture == glass17 && b2[0])
			texture = glass4;
		else if(texture == glass19 && b2[1])
			texture = glass5;
		else if(texture == glass49 && b2[2])
			texture = glass20;
		else if(texture == glass51 && b2[3])
			texture = glass21;
		else if(texture == glass18 && b2[0] && b2[1])
			texture = glass7;
		else if(texture == glass33 && b2[0] && b2[2])
			texture = glass6;
		else if(texture == glass35 && b2[3] && b2[1])
			texture = glass23;
		else if(texture == glass50 && b2[3] && b2[2])
			texture = glass22;

		else if(texture == glass18 && !b2[0] && b2[1])
			texture = glass39;
		else if(texture == glass33 && b2[0] && !b2[2])
			texture = glass38;
		else if(texture == glass35 && !b2[3] && b2[1])
			texture = glass53;
		else if(texture == glass50 && b2[3] && !b2[2])
			texture = glass52;

		else if(texture == glass18 && b2[0] && !b2[1])
			texture = glass37;
		else if(texture == glass33 && !b2[0] && b2[2])
			texture = glass36;
		else if(texture == glass35 && b2[3] && !b2[1])
			texture = glass55;
		else if(texture == glass50 && !b2[3] && b2[2])
			texture = glass54;

		else if(texture == glass34 && b2[0] && b2[1] && b2[2] && b2[3])
			texture = glass58;

		else if(texture == glass34 && !b2[0] && b2[1] && b2[2] && b2[3])
			texture = glass9;
		else if(texture == glass34 && b2[0] && !b2[1] && b2[2] && b2[3])
			texture = glass25;
		else if(texture == glass34 && b2[0] && b2[1] && !b2[2] && b2[3])
			texture = glass8;
		else if(texture == glass34 && b2[0] && b2[1] && b2[2] && !b2[3])
			texture = glass24;

		else if(texture == glass34 && b2[0] && b2[1] && !b2[2] && !b2[3])
			texture = glass11;
		else if(texture == glass34 && !b2[0] && !b2[1] && b2[2] && b2[3])
			texture = glass26;
		else if(texture == glass34 && !b2[0] && b2[1] && !b2[2] && b2[3])
			texture = glass27;
		else if(texture == glass34 && b2[0] && !b2[1] && b2[2] && !b2[3])
			texture = glass10;

		else if(texture == glass34 && b2[0] && !b2[1] && !b2[2] && b2[3])
			texture = glass42;
		else if(texture == glass34 && !b2[0] && b2[1] && b2[2] && !b2[3])
			texture = glass43;

		else if(texture == glass34 && b2[0] && !b2[1] && !b2[2] && !b2[3])
			texture = glass40;
		else if(texture == glass34 && !b2[0] && b2[1] && !b2[2] && !b2[3])
			texture = glass41;
		else if(texture == glass34 && !b2[0] && !b2[1] && b2[2] && !b2[3])
			texture = glass56;
		else if(texture == glass34 && !b2[0] && !b2[1] && !b2[2] && b2[3])
			texture = glass57;
		return texture;
	}

	protected boolean isShouldBeConnected(IBlockAccess iblockaccess, int x, int y, int z, int i, int data) {
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
		return (iblockaccess.getBlockId(x, y, z) == blockID) && !iblockaccess.isBlockOpaqueCube(x2, y2, z2) && (iblockaccess.getBlockId(x2, y2, z2) != blockID);
	}

}