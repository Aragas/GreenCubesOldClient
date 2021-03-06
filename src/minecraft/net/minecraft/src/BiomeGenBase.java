// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Referenced classes of package net.minecraft.src:
//            Block, BlockGrass, WorldGenTrees, WorldGenBigTree, 
//            WorldGenForest, WorldGenSwamp, SpawnListEntry, EntitySheep, 
//            EntityPig, EntityChicken, EntityCow, EntitySpider, 
//            EntityZombie, EntitySkeleton, EntityCreeper, EntitySlime, 
//            EntityEnderman, EntitySquid, BiomeDecorator, EnumCreatureType, 
//            IBlockAccess, WorldChunkManager, ColorizerGrass, ColorizerFoliage, 
//            BiomeGenOcean, BiomeGenPlains, BiomeGenDesert, BiomeGenHills, 
//            BiomeGenForest, BiomeGenTaiga, BiomeGenSwamp, BiomeGenRiver, 
//            BiomeGenHell, BiomeGenEnd, BiomeGenSnow, BiomeGenMushroomIsland, 
//            WorldGenerator, World

public class BiomeGenBase {

	public static final BiomeGenBase biomeList[] = new BiomeGenBase[256];
	public static final BiomeGenBase ocean = (new BiomeGenOcean(0)).setBiomeName("Ocean").setMinMaxHeight(-1F, 0.4F).setColor(0x2ca3cc).setTemperatureRainfall(0.5f, 0.8f);
	public static final BiomeGenBase plains = (new BiomeGenPlains(1)).setBiomeName("Plains").setColor(0x71E832).setTemperatureRainfall(0.7f, 0.6f);
	public static final BiomeGenBase desert = (new BiomeGenDesert(2)).setBiomeName("Desert").setDisableRain().setColor(0xf9d20b).setTemperatureRainfall(1.0f, 0.0f).setMinMaxHeight(0.1F, 0.2F);
	public static final BiomeGenBase hills = (new BiomeGenHills(3)).setBiomeName("ExtremeHills").setMinMaxHeight(0.2F, 1.8F).setColor(0x885f29).setTemperatureRainfall(0.4f, 0.5f);
	public static final BiomeGenBase forest = (new BiomeGenForest(4)).setBiomeName("Forest").func_4124_a(0x4eba31).setColor(0x35b832).setTemperatureRainfall(0.7f, 0.8f);
	public static final BiomeGenBase taiga = (new BiomeGenTaiga(5)).setBiomeName("Taiga").func_4124_a(0x4eba31).setColor(0x91AF7B).setEnableSnow().setTemperatureRainfall(0.1f, 0.8f).setMinMaxHeight(0.1F, 0.4F);
	public static final BiomeGenBase swampland = (new BiomeGenSwamp(6)).setBiomeName("Swampland").func_4124_a(0x8baf48).setMinMaxHeight(-0.2F, 0.1F).setColor(0x638C84).setTemperatureRainfall(0.8f, 0.9f);
	public static final BiomeGenBase river = (new BiomeGenRiver(7)).setColor(255).setBiomeName("River").setMinMaxHeight(-0.5F, 0.0F);
	public static final BiomeGenBase hell = (new BiomeGenHell(8)).setColor(0xff0000).setBiomeName("Hell").setDisableRain().setTemperatureRainfall(2.0F, 0.0F);
	public static final BiomeGenBase sky = (new BiomeGenEnd(9)).setColor(0x8080ff).setBiomeName("Sky").setDisableRain();
	public static final BiomeGenBase frozenOcean = (new BiomeGenOcean(10)).setColor(0x9090a0).setBiomeName("FrozenOcean").setMinMaxHeight(-1F, 0.5F).setTemperatureRainfall(0.0F, 0.5F);
	public static final BiomeGenBase frozenRiver = (new BiomeGenRiver(11)).setColor(0xa0a0ff).setBiomeName("FrozenRiver").setMinMaxHeight(-0.5F, 0.0F).setTemperatureRainfall(0.0F, 0.5F);
	public static final BiomeGenBase icePlains = (new BiomeGenSnow(12)).setBiomeName("IcePlains").setEnableSnow().setColor(0xe9e9f7).setTemperatureRainfall(0.0f, 0.5f);
	public static final BiomeGenBase iceMountains = (new BiomeGenSnow(13)).setBiomeName("IceMountains").setMinMaxHeight(0.2F, 1.8F).setEnableSnow().setColor(0xB79B79).setTemperatureRainfall(0.0f, 0.4f);
	public static final BiomeGenBase mushroomIsland = (new BiomeGenMushroomIsland(14)).setBiomeName("MushroomIsland").setColor(0xFF33CC).setTemperatureRainfall(0.7f, 1.0f).setMinMaxHeight(0.2F, 1.0F);
	public static final BiomeGenBase mushroomIslandShore = (new BiomeGenMushroomIsland(15)).setBiomeName("MushroomIslandShore").setColor(0xFF9999).setTemperatureRainfall(0.7f, 1.0f).setMinMaxHeight(-1F, 0.1F);

	public static final BiomeGenBase JUNGLE = new BiomeGenCustom(21).setBiomeName("Jungle").setColor(0x00FF6E).setTemperatureRainfall(1.0f, 1.0f);

	public static final BiomeGenBase SHRUBLAND = new BiomeGenCustom(26).setBiomeName("Shrubland").setColor(0xC6EA27).setTemperatureRainfall(0.8f, 0.2f);

	public static final BiomeGenBase BAO_SAVANNA = new BiomeGenBase(27).setBiomeName("BaoSavanna").setColor(0x8FA727).setTemperatureRainfall(0.9f, 0.5f);
	public static final BiomeGenBase BEACH = new BiomeGenBase(28).setBiomeName("Beach").setColor(0xFFFF00).setTemperatureRainfall(0.6f, 0.8f);
	public static final BiomeGenBase FLOATING_ISLANDS = new BiomeGenBase(29).setBiomeName("GreatCanyons").setColor(0x633E00).setTemperatureRainfall(0.6f, 0.0f);
	public static final BiomeGenBase FROZEN_FOREST_BORDER = new BiomeGenBase(30).setBiomeName("FrozenForestBorder").setColor(0x91C669).setTemperatureRainfall(0.3f, 0.7f);
	public static final BiomeGenBase FROZEN_LAKE = new BiomeGenBase(31).setBiomeName("FrozenLake").setColor(0x727FCC).setTemperatureRainfall(0.0f, 0.5f).setEnableSnow();
	public static final BiomeGenBase LAKE = new BiomeGenBase(32).setBiomeName("Lake").setColor(0x2C44CC).setTemperatureRainfall(0.5f, 0.6f);
	public static final BiomeGenBase OASIS = new BiomeGenBase(33).setBiomeName("Oasis").setColor(0x4DA576).setTemperatureRainfall(1.0f, 0.4f).setDisableRain();
	public static final BiomeGenBase PALM_SAVANNA = new BiomeGenBase(34).setBiomeName("PalmSavanna").setColor(0x8FE127).setTemperatureRainfall(1.0f, 0.2f).setDisableRain();

	public static final BiomeGenBase SAVANNA = new BiomeGenCustom(200).setBiomeName("Savanna").setTemperatureRainfall(0.5f, 0.5f);

	/*public static final BiomeGenBase TAIGA_HILLS = new BiomeGenBase(19).setBiomeName("Taiga Hills").setEnableSnow().setTemperatureRainfall(0.1f, 0.8f);
	public static final BiomeGenBase JUNGLE_HILLS = new BiomeGenBase(22).setBiomeName("Jungle Hills");
	public static final BiomeGenBase EXTREME_HILLS_EDGE = new BiomeGenBase(201).setBiomeName("Extreme Hills Edge");
	public static final BiomeGenBase EXTREME_FOREST = new BiomeGenBase(202).setBiomeName("Extreme Forest").setTemperatureRainfall(0.7f, 0.8f);
	public static final BiomeGenBase EXTREME_JUNGLE = new BiomeGenBase(203).setBiomeName("Extreme Jungle").setColor(0x00FF6E).setTemperatureRainfall(1.0f, 1.0f);
	public static final BiomeGenBase FLOATING_ISLANDS_EDGE = new BiomeGenBase(204).setBiomeName("Floating Islands Edge");
	public static final BiomeGenBase FOREST_2 = new BiomeGenBase(205).setBiomeName("Forest 2").setTemperatureRainfall(0.7f, 0.8f);
	public static final BiomeGenBase FOREST_HILLS = new BiomeGenBase(206).setBiomeName("Forest Hills").setTemperatureRainfall(0.7f, 0.8f);
	public static final BiomeGenBase FOREST_ISLAND = new BiomeGenBase(207).setBiomeName("Forest Island").setTemperatureRainfall(0.7f, 0.8f);
	public static final BiomeGenBase ICE_PIKE = new BiomeGenBase(208).setBiomeName("Ice Pike");
	public static final BiomeGenBase ICE_PIKE_EDGE = new BiomeGenBase(209).setBiomeName("Ice Pike Edge");
	public static final BiomeGenBase OASIS_BORDER = new BiomeGenBase(210).setBiomeName("Oasis Border").setTemperatureRainfall(1.0f, 0.4f).setDisableRain();
	public static final BiomeGenBase SNOW_SAND_BORDER = new BiomeGenBase(211).setBiomeName("SnowSandBorder");
	public static final BiomeGenBase DESERT_HILLS = new BiomeGenBase(212).setBiomeName("Desert Hills").setDisableRain().setTemperatureRainfall(1.0f, 0.0f);
	public static final BiomeGenBase ERROR = new BiomeGenBase(255).setBiomeName("Error").setColor(0xFF0000);*/

	public String biomeName;
	public int color;
	public byte topBlock;
	public byte fillerBlock;
	public int field_6502_q;
	public float minHeight;
	public float maxHeight;
	public float temperature;
	public float rainfall;
	public int field_40256_A;
	public BiomeDecorator biomeDecorator;
	protected List spawnableMonsterList;
	protected List spawnableCreatureList;
	protected List spawnableWaterCreatureList;
	private boolean enableSnow;
	private boolean enableRain;
	public final int biomeID;
	protected WorldGenTrees worldGenTrees;
	protected WorldGenBigTree worldGenBigTree;
	protected WorldGenForest worldGenForest;
	protected WorldGenSwamp worldGenSwamp;

	protected BiomeGenBase(int i) {
		topBlock = (byte) Block.grass.blockID;
		fillerBlock = (byte) Block.dirt.blockID;
		field_6502_q = 0x4ee031;
		minHeight = 0.1F;
		maxHeight = 0.3F;
		temperature = 0.5F;
		rainfall = 0.5F;
		field_40256_A = 0xffffff;
		spawnableMonsterList = new ArrayList();
		spawnableCreatureList = new ArrayList();
		spawnableWaterCreatureList = new ArrayList();
		enableRain = true;
		worldGenTrees = new WorldGenTrees(false);
		worldGenBigTree = new WorldGenBigTree(false);
		worldGenForest = new WorldGenForest(false);
		worldGenSwamp = new WorldGenSwamp();
		biomeID = i;
		biomeList[i] = this;
		biomeDecorator = createBiomeDecorator();
		spawnableCreatureList.add(new SpawnListEntry(net.minecraft.src.EntitySheep.class, 12, 4, 4));
		spawnableCreatureList.add(new SpawnListEntry(net.minecraft.src.EntityPig.class, 10, 4, 4));
		spawnableCreatureList.add(new SpawnListEntry(net.minecraft.src.EntityChicken.class, 10, 4, 4));
		spawnableCreatureList.add(new SpawnListEntry(net.minecraft.src.EntityCow.class, 8, 4, 4));
		spawnableMonsterList.add(new SpawnListEntry(net.minecraft.src.EntitySpider.class, 10, 4, 4));
		spawnableMonsterList.add(new SpawnListEntry(net.minecraft.src.EntityZombie.class, 10, 4, 4));
		spawnableMonsterList.add(new SpawnListEntry(net.minecraft.src.EntitySkeleton.class, 10, 4, 4));
		spawnableMonsterList.add(new SpawnListEntry(net.minecraft.src.EntityCreeper.class, 10, 4, 4));
		spawnableMonsterList.add(new SpawnListEntry(net.minecraft.src.EntitySlime.class, 10, 4, 4));
		spawnableMonsterList.add(new SpawnListEntry(net.minecraft.src.EntityEnderman.class, 1, 1, 4));
		spawnableWaterCreatureList.add(new SpawnListEntry(net.minecraft.src.EntitySquid.class, 10, 4, 4));
	}

	public String getBiomeTranslatedName() {
		StringTranslate stringtranslate = StringTranslate.getInstance();
		return stringtranslate.translateKey("biome." + biomeName);
	}

	protected BiomeDecorator createBiomeDecorator() {
		return new BiomeDecorator(this);
	}

	private BiomeGenBase setTemperatureRainfall(float f, float f1) {
		if(f > 0.1F && f < 0.2F) {
			throw new IllegalArgumentException("Please avoid temperatures in the range 0.1 - 0.2 because of snow");
		} else {
			temperature = f;
			rainfall = f1;
			return this;
		}
	}

	private BiomeGenBase setMinMaxHeight(float f, float f1) {
		minHeight = f;
		maxHeight = f1;
		return this;
	}

	private BiomeGenBase setDisableRain() {
		enableRain = false;
		return this;
	}

	public WorldGenerator getRandomWorldGenForTrees(Random random) {
		if(random.nextInt(10) == 0) {
			return worldGenBigTree;
		} else {
			return worldGenTrees;
		}
	}

	protected BiomeGenBase setBiomeName(String s) {
		biomeName = s;
		return this;
	}

	protected BiomeGenBase func_4124_a(int i) {
		field_6502_q = i;
		return this;
	}

	protected BiomeGenBase setColor(int i) {
		color = i;
		return this;
	}

	public int getSkyColorByTemp(float f) {
		f /= 3F;
		if(f < -1F) {
			f = -1F;
		}
		if(f > 1.0F) {
			f = 1.0F;
		}
		return java.awt.Color.getHSBColor(0.6222222F - f * 0.05F, 0.5F + f * 0.1F, 1.0F).getRGB();
	}

	public List getSpawnableList(EnumCreatureType enumcreaturetype) {
		if(enumcreaturetype == EnumCreatureType.monster) {
			return spawnableMonsterList;
		}
		if(enumcreaturetype == EnumCreatureType.creature) {
			return spawnableCreatureList;
		}
		if(enumcreaturetype == EnumCreatureType.waterCreature) {
			return spawnableWaterCreatureList;
		} else {
			return null;
		}
	}

	public boolean getEnableSnow() {
		return enableSnow;
	}

	protected BiomeGenBase setEnableSnow() {
		this.enableSnow = true;
		return this;
	}

	public boolean canSpawnLightningBolt() {
		if(enableSnow) {
			return false;
		} else {
			return enableRain;
		}
	}

	public float getSpawningChance() {
		return 0.1F;
	}

	public final int func_35476_e() {
		return (int) (rainfall * 65536F);
	}

	public final int func_35474_f() {
		return (int) (temperature * 65536F);
	}

	public void func_35477_a(World world, Random random, int i, int j) {
		biomeDecorator.decorate(world, random, i, j);
	}

	public int getBiomeGrassColor() {
		double d = temperature < 0.0f ? 0.0f : temperature > 1.0f ? 1.0f : temperature;
		double d1 = rainfall < 0.0f ? 0.0f : rainfall > 1.0f ? 1.0f : rainfall;
		return ColorizerGrass.getGrassColor(d, d1);
	}

	public int getBiomeFoliageColor() {
		double d = temperature < 0.0f ? 0.0f : temperature > 1.0f ? 1.0f : temperature;
		double d1 = rainfall < 0.0f ? 0.0f : rainfall > 1.0f ? 1.0f : rainfall;
		return ColorizerGrass.getGrassColor(d, d1);
	}

	public static BiomeGenBase getById(int id) {
		BiomeGenBase bgb = biomeList[id];
		if(bgb == null)
			return plains;
		return bgb;
	}

}
