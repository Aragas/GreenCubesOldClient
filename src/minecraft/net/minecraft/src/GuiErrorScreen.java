// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, GuiButton

public class GuiErrorScreen extends GuiScreen {

	private int field_28098_a;

	public GuiErrorScreen() {
		field_28098_a = 0;
	}

	@Override
	public void updateScreen() {
		field_28098_a++;
	}

	@Override
	public void initGui() {
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
	}

	@Override
	protected void keyTyped(char c, int i) {
	}

	@Override
	public void drawScreen(int i, int j, float f) {
		drawDefaultBackground();
		// GreenCubes start
		drawCenteredString(fontRenderer, "�� ������� ������!", width / 2, (height / 4 - 60) + 20, 0xffffff);
		drawString(fontRenderer, "���������� �� ������� ������.", width / 2 - 140, (height / 4 - 60) + 60 + 0, 0xa0a0a0);
		drawString(fontRenderer, "��� ����� ���� ������� ����� � ���� ��� ���, ���", width / 2 - 140, (height / 4 - 60) + 60 + 18, 0xa0a0a0);
		drawString(fontRenderer, "Java Virtual Machine �� �������� ����������", width / 2 - 140, (height / 4 - 60) + 60 + 27, 0xa0a0a0);
		drawString(fontRenderer, "������. ", width / 2 - 140, (height / 4 - 60) + 60 + 36, 0xa0a0a0);
		drawString(fontRenderer, "��� ��������� ����������� ������ ���� �����������.", width / 2 - 140, (height / 4 - 45) + 60 + 63, 0xa0a0a0);
		drawString(fontRenderer, "����������, ������������� ����.", width / 2 - 140, (height / 4 - 60) + 60 + 60, 0xa0a0a0);
		// GreenCubes end
		super.drawScreen(i, j, f);
	}
}
