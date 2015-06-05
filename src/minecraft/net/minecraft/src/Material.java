// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;

// Referenced classes of package net.minecraft.src:
//            MaterialTransparent, MapColor, MaterialLiquid, MaterialLogic, 
//            MaterialPortal, MaterialWeb

public class Material {

	public static final Material air;
	public static final Material grass;
	public static final Material ground;
	public static final Material wood;
	public static final Material rock;
	public static final Material iron;
	public static final Material water;
	public static final Material lava;
	public static final Material leaves;
	public static final Material plants;
	public static final Material vine;
	public static final Material sponge;
	public static final Material wool;
	public static final Material fire;
	public static final Material sand;
	public static final Material circuits;
	public static final Material glass;
	public static final Material tnt;
	public static final Material unused;
	public static final Material ice;
	public static final Material snow;
	public static final Material craftedSnow;
	public static final Material cactus;
	public static final Material clay;
	public static final Material pumpkin;
	public static final Material field_41056_z;
	public static final Material portal;
	public static final Material cakeMaterial;
	public static final Material web;
	public static final Material piston;
	public static final Material tallGrass;
	private boolean canBurn;
	private boolean isReplaceable;
	private boolean isTranslucent;
	public final MapColor materialMapColor;
	private boolean canHarvest;
	private int mobilityFlag;

	public Material(MapColor mapcolor) {
		canHarvest = true;
		materialMapColor = mapcolor;
	}

	public boolean getIsLiquid() {
		return false;
	}

	public boolean isSolid() {
		return true;
	}

	public boolean notTransparent() {
		return true;
	}

	public boolean getIsSolid() {
		return true;
	}

	private Material setIsTranslucent() {
		isTranslucent = true;
		return this;
	}

	protected Material setNoHarvest() {
		canHarvest = false;
		return this;
	}

	protected Material setBurning() {
		canBurn = true;
		return this;
	}

	public boolean getCanBurn() {
		return canBurn;
	}

	public Material setReplaceable() {
		isReplaceable = true;
		return this;
	}

	public boolean getIsReplaceable() {
		return isReplaceable;
	}

	public boolean getIsOpaque() {
		if(isTranslucent) {
			return false;
		} else {
			return getIsSolid();
		}
	}

	public boolean getIsHarvestable() {
		return canHarvest;
	}

	public int getMaterialMobility() {
		return mobilityFlag;
	}

	protected Material setNoPushMobility() {
		mobilityFlag = 1;
		return this;
	}

	protected Material setImmovableMobility() {
		mobilityFlag = 2;
		return this;
	}

	static {
		air = new MaterialTransparent(MapColor.airColor);
		grass = new Material(MapColor.grassColor);
		ground = new Material(MapColor.dirtColor);
		wood = (new Material(MapColor.woodColor)).setBurning();
		rock = (new Material(MapColor.stoneColor)).setNoHarvest();
		iron = (new Material(MapColor.ironColor)).setNoHarvest();
		water = (new MaterialLiquid(MapColor.waterColor)).setNoPushMobility();
		lava = (new MaterialLiquid(MapColor.tntColor)).setNoPushMobility();
		leaves = (new Material(MapColor.foliageColor)).setBurning().setIsTranslucent().setNoPushMobility();
		plants = (new MaterialLogic(MapColor.foliageColor)).setNoPushMobility();
		vine = (new MaterialLogic(MapColor.foliageColor)).setBurning().setNoPushMobility().setReplaceable();
		sponge = new Material(MapColor.clothColor);
		wool = (new Material(MapColor.clothColor)).setBurning();
		fire = (new MaterialTransparent(MapColor.airColor)).setNoPushMobility();
		sand = new Material(MapColor.sandColor);
		circuits = (new MaterialLogic(MapColor.airColor)).setNoPushMobility();
		glass = (new Material(MapColor.airColor)).setIsTranslucent();
		tnt = (new Material(MapColor.tntColor)).setBurning().setIsTranslucent();
		unused = (new Material(MapColor.foliageColor)).setNoPushMobility();
		ice = (new Material(MapColor.iceColor)).setIsTranslucent();
		snow = (new MaterialLogic(MapColor.snowColor)).setReplaceable().setIsTranslucent().setNoHarvest().setNoPushMobility();
		craftedSnow = (new Material(MapColor.snowColor)).setNoHarvest();
		cactus = (new Material(MapColor.foliageColor)).setIsTranslucent().setNoPushMobility();
		clay = new Material(MapColor.clayColor);
		pumpkin = (new Material(MapColor.foliageColor)).setNoPushMobility();
		field_41056_z = (new Material(MapColor.foliageColor)).setNoPushMobility();
		portal = (new MaterialPortal(MapColor.airColor)).setImmovableMobility();
		cakeMaterial = (new Material(MapColor.airColor)).setNoPushMobility();
		web = (new MaterialWeb(MapColor.clothColor)).setNoHarvest().setNoPushMobility();
		piston = (new Material(MapColor.stoneColor)).setImmovableMobility();
		tallGrass = new MaterialTransparent(MapColor.foliageColor).setNoPushMobility();
	}
}
