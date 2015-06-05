// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

// Referenced classes of package net.minecraft.src:
//            GuiScreen, StringTranslate, GuiWorldSlot, ISaveFormat, 
//            SaveFormatComparator, MathHelper, GuiButton, GuiYesNo, 
//            GuiCreateWorld, GuiRenameWorld, PlayerControllerSP, PlayerControllerCreative

public class GuiSelectWorld extends GuiScreen {

	private final DateFormat dateFormatter = new SimpleDateFormat();
	protected GuiScreen parentScreen;
	protected String screenTitle;
	private boolean selected;
	private int selectedWorld;
	private List saveList;
	private GuiWorldSlot worldSlotContainer;
	private String field_22098_o;
	private String field_22097_p;
	private String field_35316_k[];
	private boolean deleting;
	private GuiButton buttonRename;
	private GuiButton buttonSelect;
	private GuiButton buttonDelete;

	public GuiSelectWorld(GuiScreen guiscreen) {
		screenTitle = "Select world";
		selected = false;
		field_35316_k = new String[2];
		parentScreen = guiscreen;
	}

	@Override
	public void initGui() {
		StringTranslate stringtranslate = StringTranslate.getInstance();
		screenTitle = stringtranslate.translateKey("selectWorld.title");
		field_22098_o = stringtranslate.translateKey("selectWorld.world");
		field_22097_p = stringtranslate.translateKey("selectWorld.conversion");
		field_35316_k[0] = stringtranslate.translateKey("gameMode.survival");
		field_35316_k[1] = stringtranslate.translateKey("gameMode.creative");
		loadSaves();
		worldSlotContainer = new GuiWorldSlot(this);
		worldSlotContainer.registerScrollButtons(controlList, 4, 5);
		initButtons();
	}

	private void loadSaves() {
		ISaveFormat isaveformat = mc.getSaveLoader();
		saveList = isaveformat.getSaveList();
		Collections.sort(saveList);
		selectedWorld = -1;
	}

	protected String getSaveFileName(int i) {
		return ((SaveFormatComparator) saveList.get(i)).getFileName();
	}

	protected String getSaveName(int i) {
		String s = ((SaveFormatComparator) saveList.get(i)).getDisplayName();
		if(s == null || MathHelper.stringNullOrLengthZero(s)) {
			StringTranslate stringtranslate = StringTranslate.getInstance();
			s = (new StringBuilder()).append(stringtranslate.translateKey("selectWorld.world")).append(" ").append(i + 1).toString();
		}
		return s;
	}

	public void initButtons() {
		StringTranslate stringtranslate = StringTranslate.getInstance();
		controlList.add(buttonSelect = new GuiButton(1, width / 2 - 154, height - 52, 150, 20, stringtranslate.translateKey("selectWorld.select")));
		controlList.add(buttonDelete = new GuiButton(6, width / 2 - 154, height - 28, 70, 20, stringtranslate.translateKey("selectWorld.rename")));
		controlList.add(buttonRename = new GuiButton(2, width / 2 - 74, height - 28, 70, 20, stringtranslate.translateKey("selectWorld.delete")));
		controlList.add(new GuiButton(3, width / 2 + 4, height - 52, 150, 20, stringtranslate.translateKey("selectWorld.create")));
		controlList.add(new GuiButton(0, width / 2 + 4, height - 28, 150, 20, stringtranslate.translateKey("gui.cancel")));
		buttonSelect.enabled = false;
		buttonRename.enabled = false;
		buttonDelete.enabled = false;
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if(!guibutton.enabled) {
			return;
		}
		if(guibutton.id == 2) {
			String s = getSaveName(selectedWorld);
			if(s != null) {
				deleting = true;
				StringTranslate stringtranslate = StringTranslate.getInstance();
				String s1 = stringtranslate.translateKey("selectWorld.deleteQuestion");
				String s2 = (new StringBuilder()).append("'").append(s).append("' ").append(stringtranslate.translateKey("selectWorld.deleteWarning")).toString();
				String s3 = stringtranslate.translateKey("selectWorld.deleteButton");
				String s4 = stringtranslate.translateKey("gui.cancel");
				GuiYesNo guiyesno = new GuiYesNo(this, s1, s2, s3, s4, selectedWorld);
				mc.displayGuiScreen(guiyesno);
			}
		} else if(guibutton.id == 1) {
			selectWorld(selectedWorld);
		} else if(guibutton.id == 3) {
			mc.displayGuiScreen(new GuiCreateWorld(this));
		} else if(guibutton.id == 6) {
			mc.displayGuiScreen(new GuiRenameWorld(this, getSaveFileName(selectedWorld)));
		} else if(guibutton.id == 0) {
			mc.displayGuiScreen(parentScreen);
		} else {
			worldSlotContainer.actionPerformed(guibutton);
		}
	}

	public void selectWorld(int i) {
		mc.displayGuiScreen(null);
		if(selected) {
			return;
		}
		selected = true;
		int j = ((SaveFormatComparator) saveList.get(i)).getGameType();
		if(j == 0) {
			mc.playerController = new PlayerControllerSP(mc);
		} else {
			mc.playerController = new PlayerControllerCreative(mc);
		}
		String s = getSaveFileName(i);
		if(s == null) {
			s = (new StringBuilder()).append("World").append(i).toString();
		}
		mc.startWorld(s, getSaveName(i), null);
		mc.displayGuiScreen(null);
	}

	@Override
	public void deleteWorld(boolean flag, int i) {
		if(deleting) {
			deleting = false;
			if(flag) {
				ISaveFormat isaveformat = mc.getSaveLoader();
				isaveformat.flushCache();
				isaveformat.deleteWorldDirectory(getSaveFileName(i));
				loadSaves();
			}
			mc.displayGuiScreen(this);
		}
	}

	@Override
	public void drawScreen(int i, int j, float f) {
		worldSlotContainer.drawScreen(i, j, f);
		drawCenteredString(fontRenderer, screenTitle, width / 2, 20, 0xffffff);
		super.drawScreen(i, j, f);
	}

	static List getSize(GuiSelectWorld guiselectworld) {
		return guiselectworld.saveList;
	}

	static int onElementSelected(GuiSelectWorld guiselectworld, int i) {
		return guiselectworld.selectedWorld = i;
	}

	static int getSelectedWorld(GuiSelectWorld guiselectworld) {
		return guiselectworld.selectedWorld;
	}

	static GuiButton getSelectButton(GuiSelectWorld guiselectworld) {
		return guiselectworld.buttonSelect;
	}

	static GuiButton getRenameButton(GuiSelectWorld guiselectworld) {
		return guiselectworld.buttonRename;
	}

	static GuiButton getDeleteButton(GuiSelectWorld guiselectworld) {
		return guiselectworld.buttonDelete;
	}

	static String func_22087_f(GuiSelectWorld guiselectworld) {
		return guiselectworld.field_22098_o;
	}

	static DateFormat getDateFormatter(GuiSelectWorld guiselectworld) {
		return guiselectworld.dateFormatter;
	}

	static String func_22088_h(GuiSelectWorld guiselectworld) {
		return guiselectworld.field_22097_p;
	}

	static String[] func_35315_i(GuiSelectWorld guiselectworld) {
		return guiselectworld.field_35316_k;
	}
}
