// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;

// Referenced classes of package net.minecraft.src:
//            Material, MapColor

public class MaterialTransparent extends Material {

	public MaterialTransparent(MapColor mapcolor) {
		super(mapcolor);
		setReplaceable();
	}

	@Override
	public boolean isSolid() {
		return false;
	}

	@Override
	public boolean notTransparent() {
		return false;
	}

	@Override
	public boolean getIsSolid() {
		return false;
	}
}
