package net.minecraft.src;

public class Buff {

	public static final Buff[] byId = new Buff[Short.MAX_VALUE];

	public static final Buff HUNGER_1 = new Buff(1, "hunger1");
	public static final Buff HUNGER_2 = new Buff(2, "hunger2");
	public static final Buff HUNGER_3 = new Buff(3, "hunger3");
	public static final Buff HEALTH_REGEN_FOOD = new Buff(4, "regen");
	public static final Buff SPEED_BOOTS = new Buff(5, "fastRun");
	public static final Buff ENERGY_BAR = new Buff(6, "hungerSlow");
	public static final Buff SNOW_BOOTS = new Buff(7, "snowBoost");
	// 8 - Walked Counter
	public static final Buff NY_MOOD = new Buff(9, "nymood");
	public static final Buff PRESETN_CD = new Buff(10, "presentcd");
	public static final Buff SATYR = new Buff(11, "satyr");

	public final int id;
	public final String name;

	protected Buff(int id, String translateName) {
		this.id = id;
		this.name = translateName;
		if(byId[id] != null)
			throw new AssertionError("Slot " + id + " is already occupied by " + byId[id] + " when adding " + this);
		byId[id] = this;
	}

	public String getName() {
		return StringTranslate.getInstance().translateKey("buff." + name);
	}

	public String getTexture() {
		return "/gc_images/buff_" + name + ".png";
	}

	public String getTextureFramed() {
		return "/gc_images/buff_" + name + "_framed.png";
	}

}
