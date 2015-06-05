// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;

import java.util.Random;

// Referenced classes of package net.minecraft.src:
//            BlockLeavesBase, Material, ColorizerFoliage, IBlockAccess, 
//            WorldChunkManager, BiomeGenBase, World, Block, 
//            EntityPlayer, ItemStack, Item, ItemShears, 
//            StatList, Entity

public class BlockLeaves extends BlockLeavesBase {

	private int baseIndexInPNG;
	int adjacentTreeBlocks[];

	protected BlockLeaves(int i, int j) {
		super(i, j, Material.leaves, false);
		baseIndexInPNG = j;
		setTickOnLoad(true);
	}

	@Override
	public int getBlockColor() {
		double d = 0.5D;
		double d1 = 1.0D;
		return ColorizerFoliage.getFoliageColor(d, d1);
	}

	@Override
	public int getRenderColor(int i) {
		if((i & 1) == 1) {
			return ColorizerFoliage.getFoliageColorPine();
		}
		if((i & 2) == 2) {
			return ColorizerFoliage.getFoliageColorBirch();
		} else {
			return ColorizerFoliage.getFoliageColorBasic();
		}
	}

	@Override
	public int colorMultiplier(IBlockAccess iblockaccess, int i, int j, int k) {
		int l = iblockaccess.getBlockMetadata(i, j, k);
		if((l & 3) == 1) {
			return ColorizerFoliage.getFoliageColorPine();
		}
		if((l & 3) == 2) {
			return ColorizerFoliage.getFoliageColorBirch();
		} else {
			int c1 = 0;
			int c2 = 0;
			int c3 = 0;
			for(l = -2; l <= 2; l++) {
				for(int i1 = -2; i1 <= 2; i1++) {
					int j1 = iblockaccess.getBiomeAt(i + i1, j, k + l).getBiomeFoliageColor();
					c1 += (j1 & 0xff0000) >> 16;
					c2 += (j1 & 0xff00) >> 8;
					c3 += j1 & 0xff;
				}
			}
			return (c1 / 25 & 0xff) << 16 | (c2 / 25 & 0xff) << 8 | c3 / 25 & 0xff;
		}
	}

	@Override
	public void onBlockRemoval(World world, int i, int j, int k) {
		int l = 1;
		int i1 = l + 1;
		if(world.checkChunksExist(i - i1, j - i1, k - i1, i + i1, j + i1, k + i1)) {
			for(int j1 = -l; j1 <= l; j1++) {
				for(int k1 = -l; k1 <= l; k1++) {
					for(int l1 = -l; l1 <= l; l1++) {
						int i2 = world.getBlockId(i + j1, j + k1, k + l1);
						if(i2 == Block.leaves.blockID) {
							int j2 = world.getBlockMetadata(i + j1, j + k1, k + l1);
							world.setBlockMetadata(i + j1, j + k1, k + l1, j2 | 8);
						}
					}

				}

			}

		}
	}

	@Override
	public void updateTick(World world, int i, int j, int k, Random random) {
		if(world.multiplayerWorld) {
			return;
		}
		int l = world.getBlockMetadata(i, j, k);
		if((l & 8) != 0 && (l & 4) == 0) {
			byte byte0 = 4;
			int i1 = byte0 + 1;
			byte byte1 = 32;
			int j1 = byte1 * byte1;
			int k1 = byte1 / 2;
			if(adjacentTreeBlocks == null) {
				adjacentTreeBlocks = new int[byte1 * byte1 * byte1];
			}
			if(world.checkChunksExist(i - i1, j - i1, k - i1, i + i1, j + i1, k + i1)) {
				for(int l1 = -byte0; l1 <= byte0; l1++) {
					for(int k2 = -byte0; k2 <= byte0; k2++) {
						for(int i3 = -byte0; i3 <= byte0; i3++) {
							int k3 = world.getBlockId(i + l1, j + k2, k + i3);
							if(k3 == Block.wood.blockID) {
								adjacentTreeBlocks[(l1 + k1) * j1 + (k2 + k1) * byte1 + (i3 + k1)] = 0;
								continue;
							}
							if(k3 == Block.leaves.blockID) {
								adjacentTreeBlocks[(l1 + k1) * j1 + (k2 + k1) * byte1 + (i3 + k1)] = -2;
							} else {
								adjacentTreeBlocks[(l1 + k1) * j1 + (k2 + k1) * byte1 + (i3 + k1)] = -1;
							}
						}

					}

				}

				for(int i2 = 1; i2 <= 4; i2++) {
					for(int l2 = -byte0; l2 <= byte0; l2++) {
						for(int j3 = -byte0; j3 <= byte0; j3++) {
							for(int l3 = -byte0; l3 <= byte0; l3++) {
								if(adjacentTreeBlocks[(l2 + k1) * j1 + (j3 + k1) * byte1 + (l3 + k1)] != i2 - 1) {
									continue;
								}
								if(adjacentTreeBlocks[((l2 + k1) - 1) * j1 + (j3 + k1) * byte1 + (l3 + k1)] == -2) {
									adjacentTreeBlocks[((l2 + k1) - 1) * j1 + (j3 + k1) * byte1 + (l3 + k1)] = i2;
								}
								if(adjacentTreeBlocks[(l2 + k1 + 1) * j1 + (j3 + k1) * byte1 + (l3 + k1)] == -2) {
									adjacentTreeBlocks[(l2 + k1 + 1) * j1 + (j3 + k1) * byte1 + (l3 + k1)] = i2;
								}
								if(adjacentTreeBlocks[(l2 + k1) * j1 + ((j3 + k1) - 1) * byte1 + (l3 + k1)] == -2) {
									adjacentTreeBlocks[(l2 + k1) * j1 + ((j3 + k1) - 1) * byte1 + (l3 + k1)] = i2;
								}
								if(adjacentTreeBlocks[(l2 + k1) * j1 + (j3 + k1 + 1) * byte1 + (l3 + k1)] == -2) {
									adjacentTreeBlocks[(l2 + k1) * j1 + (j3 + k1 + 1) * byte1 + (l3 + k1)] = i2;
								}
								if(adjacentTreeBlocks[(l2 + k1) * j1 + (j3 + k1) * byte1 + ((l3 + k1) - 1)] == -2) {
									adjacentTreeBlocks[(l2 + k1) * j1 + (j3 + k1) * byte1 + ((l3 + k1) - 1)] = i2;
								}
								if(adjacentTreeBlocks[(l2 + k1) * j1 + (j3 + k1) * byte1 + (l3 + k1 + 1)] == -2) {
									adjacentTreeBlocks[(l2 + k1) * j1 + (j3 + k1) * byte1 + (l3 + k1 + 1)] = i2;
								}
							}

						}

					}

				}

			}
			int j2 = adjacentTreeBlocks[k1 * j1 + k1 * byte1 + k1];
			if(j2 >= 0) {
				world.setBlockMetadata(i, j, k, l & -9);
			} else {
				removeLeaves(world, i, j, k);
			}
		}
	}

	private void removeLeaves(World world, int i, int j, int k) {
		dropBlockAsItem(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
		world.setBlockWithNotify(i, j, k, 0);
	}

	@Override
	public int quantityDropped(Random random) {
		return random.nextInt(20) != 0 ? 0 : 1;
	}

	@Override
	public int idDropped(int i, Random random, int j) {
		return Block.sapling.blockID;
	}

	@Override
	public void harvestBlock(World world, EntityPlayer entityplayer, int i, int j, int k, int l) {
		if(!world.multiplayerWorld && entityplayer.getCurrentEquippedItem() != null && entityplayer.getCurrentEquippedItem().itemID == Item.shears.shiftedIndex) {
			entityplayer.addStat(StatList.mineBlockStatArray[blockID], 1);
			dropBlockAsItem_do(world, i, j, k, new ItemStack(Block.leaves.blockID, 1, l & 3));
		} else {
			super.harvestBlock(world, entityplayer, i, j, k, l);
		}
	}

	@Override
	protected int damageDropped(int i) {
		return i & 3;
	}

	@Override
	public boolean isOpaqueCube() {
		return !graphicsLevel;
	}

	@Override
	public int getBlockTextureFromSideAndMetadata(int i, int j) {
		if((j & 3) == 1) {
			return blockIndexInTexture + 80;
		}
		if((j & 3) == 3) {
			return blockIndexInTexture + 144;
		} else {
			return blockIndexInTexture;
		}
	}

	public void setGraphicsLevel(boolean flag) {
		graphicsLevel = flag;
		blockIndexInTexture = baseIndexInPNG + (flag ? 0 : 1);
	}

	@Override
	public void onEntityWalking(World world, int i, int j, int k, Entity entity) {
		super.onEntityWalking(world, i, j, k, entity);
	}
}
