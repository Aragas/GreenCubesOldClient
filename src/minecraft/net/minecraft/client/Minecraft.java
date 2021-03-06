package net.minecraft.client;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Future;

import javax.imageio.ImageIO;

import net.minecraft.src.*;

import org.greencubes.executor.IInvokeable;
import org.greencubes.executor.Task;
import org.greencubes.executor.TaskWithResult;
import org.greencubes.util.MacOSX;
import org.greencubes.util.Natives;
import org.greencubes.util.Util;
import org.json.JSONArray;
import org.json.JSONObject;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controllers;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.glu.GLU;

public abstract class Minecraft implements Runnable {

	public static byte field_28006_b[] = new byte[0xa00000];
	public static Minecraft theMinecraft;
	public static long frameTimes[] = new long[512];
	public static long tickTimes[] = new long[512];
	public static int numRecordedFrameTimes = 0;
	public static long hasPaidCheckTime = 0L;

	private final ConcurrentLinkedQueue<IInvokeable> taskQueue = new ConcurrentLinkedQueue<IInvokeable>();

	public PlayerController playerController;
	private boolean fullscreen = false;
	private boolean hasCrashed = false;
	public boolean noCollidePlayers = false;
	public int displayWidth;
	public int displayHeight;
	private Timer timer = new Timer(20);
	public World theWorld;
	public RenderGlobal renderGlobal;
	public EntityPlayerSP thePlayer;
	public EntityLiving renderViewEntity;
	public EffectRenderer effectRenderer;
	public Session session = null;
	public String minecraftUri;
	public Canvas mcCanvas;
	public boolean hideQuitButton = false;
	public volatile boolean isGamePaused = false;
	public RenderEngine renderEngine;
	public FontRenderer fontRenderer;
	public FontRenderer worldFontRenderer;
	public FontRenderer standardGalacticFontRenderer;
	public GuiScreen currentScreen = null;
	public LoadingScreenRenderer loadingScreen;
	public EntityRenderer entityRenderer;
	//private int ticksRan = 0;
	private int leftClickCounter = 0;
	private int tempDisplayWidth;
	private int tempDisplayHeight;
	public GuiAchievement guiAchievement = new GuiAchievement(this);
	public GuiIngame ingameGUI;
	public boolean skipRenderWorld = false;
	public ModelBiped playerModelBiped = new ModelBiped(0.0F);
	public MovingObjectPosition objectMouseOver = null;
	public GameSettings gameSettings;
	protected MinecraftApplet mcApplet;
	public SoundManager sndManager = new SoundManager();
	public MouseHelper mouseHelper;
	public TexturePackList texturePackList;
	public File mcDataDir;
	private ISaveFormat saveLoader;
	private int rightClickDelayTimer = 0;
	public StatFileWriter statFileWriter;
	private String serverName;
	private int serverPort;
	private TextureWaterFX textureWaterFX = new TextureWaterFX();
	private TextureLavaFX textureLavaFX = new TextureLavaFX();
	private static File minecraftDir = null;
	public volatile boolean running = true;
	public String debug = "";
	long field_40004_N = System.currentTimeMillis();
	int fpsCounter = 0;
	boolean isTakingScreenshot = false;
	long prevFrameTime = -1L;
	private String field_40006_ak = "root";
	public boolean inGameHasFocus = false;
	public boolean isRaining = false;
	long systemTime = System.currentTimeMillis();
	private int joinPlayerCounter = 0;

	public GChat chat;

	public Minecraft(Component component, Canvas canvas, MinecraftApplet minecraftapplet, int i, int j, boolean flag) {
		StatList.func_27360_a();
		tempDisplayHeight = j;
		fullscreen = flag;
		mcApplet = minecraftapplet;
		new ThreadSleepForever(this, "Timer hack thread");
		mcCanvas = canvas;
		displayWidth = i;
		displayHeight = j;
		if(minecraftapplet == null || "true".equals(minecraftapplet.getParameter("stand-alone")))
			hideQuitButton = false;
		theMinecraft = this;
	}

	public <E> Future<E> queueTask(Callable<E> task) {
		TaskWithResult<E> appTask = new TaskWithResult<E>(task);
		taskQueue.add(appTask);
		return appTask;
	}

	public Task queueTask(Runnable task) {
		Task appTask = new Task(task);
		taskQueue.add(appTask);
		return appTask;
	}

	public void onMinecraftCrash(UnexpectedThrowable unexpectedthrowable) {
		hasCrashed = true;
		displayUnexpectedThrowable(unexpectedthrowable);
	}

	public abstract void displayUnexpectedThrowable(UnexpectedThrowable unexpectedthrowable);

	public void setServer(String s, int i) {
		serverName = s;
		serverPort = i;
	}

	public void startGame() throws LWJGLException {
		if(mcCanvas != null) {
			Graphics g = mcCanvas.getGraphics();
			if(g != null) {
				g.setColor(Color.BLACK);
				g.fillRect(0, 0, displayWidth, displayHeight);
				g.dispose();
			}
			Display.setParent(mcCanvas);
		} else if(fullscreen) {
			Display.setFullscreen(true);
			displayWidth = Display.getDisplayMode().getWidth();
			displayHeight = Display.getDisplayMode().getHeight();
			if(displayWidth <= 0)
				displayWidth = 1;
			if(displayHeight <= 0)
				displayHeight = 1;
		} else {
			Display.setDisplayMode(new DisplayMode(displayWidth, displayHeight));
		}
		Display.setTitle("GreenCubes");
		try {
			PixelFormat pixelformat = new PixelFormat().withStencilBits(8).withDepthBits(24);
			Display.create(pixelformat);
		} catch (LWJGLException lwjglexception) {
			lwjglexception.printStackTrace();
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
			}
			Display.create();
		}
		OpenGlHelper.initializeTextures();
		mcDataDir = getMinecraftDir();
		saveLoader = new SaveConverterMcRegion(new File(mcDataDir, "saves"));
		gameSettings = new GameSettings(this, mcDataDir);
		texturePackList = new TexturePackList(this, mcDataDir);
		renderEngine = new RenderEngine(texturePackList, gameSettings);
		loadScreen();
		fontRenderer = new FontRenderer(gameSettings, "/font/default.png", renderEngine, true);
		//renderEngine.blurTexture = true;
		worldFontRenderer = new FontRenderer(gameSettings, "/font/default.png", renderEngine, true);
		//renderEngine.blurTexture = false;
		standardGalacticFontRenderer = new FontRenderer(gameSettings, "/font/alternate.png", renderEngine, false);
		ColorizerWater.getWaterBiomeColorizer(renderEngine.getTextureContents("/misc/watercolor.png"));
		ColorizerGrass.setGrassBiomeColorizer(renderEngine.getTextureContents("/misc/grasscolor.png"));
		ColorizerFoliage.getFoilageBiomeColorizer(renderEngine.getTextureContents("/misc/foliagecolor.png"));
		entityRenderer = new EntityRenderer(this);
		RenderManager.instance.itemRenderer = entityRenderer.itemRenderer;
		statFileWriter = new StatFileWriter(session, mcDataDir);
		//AchievementList.openInventory.setStatStringFormatter(new StatStringFormatKeyInv(this));
		loadScreen();
		Keyboard.create();
		Mouse.create();
		mouseHelper = new MouseHelper(mcCanvas);
		try {
			Controllers.create();
		} catch (Exception exception) {
		}
		checkGLError("Pre startup");
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glClearDepth(1.0D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(515);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glAlphaFunc(516, 0.1F);
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glMatrixMode(5888 /* GL_MODELVIEW0_ARB */);
		checkGLError("Startup");
		sndManager.loadSoundSettings(gameSettings);
		renderEngine.registerTextureFX(textureLavaFX);
		renderEngine.registerTextureFX(textureWaterFX);
		renderEngine.registerTextureFX(new TexturePortalFX());
		renderEngine.registerTextureFX(new TextureCompassFX(this));
		renderEngine.registerTextureFX(new TextureWatchFX(this));
		renderEngine.registerTextureFX(new TextureWaterFlowFX());
		renderEngine.registerTextureFX(new TextureLavaFlowFX());
		renderEngine.registerTextureFX(new TextureFlamesFX(0));
		renderEngine.registerTextureFX(new TextureFlamesFX(1));
		renderGlobal = new RenderGlobal(this, renderEngine);
		ARBOcclusionChecker.instance.init();
		GL11.glViewport(0, 0, displayWidth, displayHeight);
		effectRenderer = new EffectRenderer(theWorld, renderEngine);
		new ThreadDownloadResources(mcDataDir, this).reloadResources();
		checkGLError("Post startup");
		ingameGUI = new GuiIngame(this);
		if(serverName != null)
			displayGuiScreen(new GuiConnecting(this, serverName, serverPort));
		else
			displayGuiScreen(new GuiMainMenu());
		loadingScreen = new LoadingScreenRenderer(this);
		if(this.gameSettings.fullScreen && !this.fullscreen) {
			this.toggleFullscreen();
		}
		try {
			chat = new GChat(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadScreen() throws LWJGLException {
		ScaledResolution scaledresolution = new ScaledResolution(gameSettings, displayWidth, displayHeight);
		GL11.glClear(16640);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0.0D, scaledresolution.scaledWidthD, scaledresolution.scaledHeightD, 0.0D, 1000D, 3000D);
		GL11.glMatrixMode(5888 /* GL_MODELVIEW0_ARB */);
		GL11.glLoadIdentity();
		GL11.glTranslatef(0.0F, 0.0F, -2000F);
		GL11.glViewport(0, 0, displayWidth, displayHeight);
		GL11.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
		Tessellator tessellator = Tessellator.instance;
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_FOG);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, renderEngine.getTexture("/title/greencubes.png"));
		tessellator.startDrawingQuads();
		tessellator.setColorOpaque_I(0xffffff);
		tessellator.addVertexWithUV(0.0D, displayHeight, 0.0D, 0.0D, 0.0D);
		tessellator.addVertexWithUV(displayWidth, displayHeight, 0.0D, 0.0D, 0.0D);
		tessellator.addVertexWithUV(displayWidth, 0.0D, 0.0D, 0.0D, 0.0D);
		tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
		tessellator.draw();
		char c = '\u0100';
		char c1 = '\u0100';
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		tessellator.setColorOpaque_I(0xffffff);
		scaledTessellator((scaledresolution.getScaledWidth() - c) / 2, (scaledresolution.getScaledHeight() - c1) / 2, 0, 0, c, c1);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_FOG);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glAlphaFunc(516, 0.1F);
		Display.swapBuffers();
	}

	public void scaledTessellator(int i, int j, int k, int l, int i1, int j1) {
		float f = 0.00390625F;
		float f1 = 0.00390625F;
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(i + 0, j + j1, 0.0D, (k + 0) * f, (l + j1) * f1);
		tessellator.addVertexWithUV(i + i1, j + j1, 0.0D, (k + i1) * f, (l + j1) * f1);
		tessellator.addVertexWithUV(i + i1, j + 0, 0.0D, (k + i1) * f, (l + 0) * f1);
		tessellator.addVertexWithUV(i + 0, j + 0, 0.0D, (k + 0) * f, (l + 0) * f1);
		tessellator.draw();
	}

	public static File getMinecraftDir() {
		if(minecraftDir == null)
			minecraftDir = getAppDir("greencubes");
		return minecraftDir;
	}

	public static File getAppDir(String s) {
		return new File("").getAbsoluteFile();
	}

	public static File getAppDir() {
		return new File("").getAbsoluteFile();
	}

	private static EnumOS2 getOs() {
		String s = System.getProperty("os.name").toLowerCase();
		if(s.contains("win"))
			return EnumOS2.windows;
		if(s.contains("mac"))
			return EnumOS2.macos;
		if(s.contains("solaris"))
			return EnumOS2.solaris;
		if(s.contains("sunos"))
			return EnumOS2.solaris;
		if(s.contains("linux"))
			return EnumOS2.linux;
		if(s.contains("unix"))
			return EnumOS2.linux;
		return EnumOS2.unknown;
	}

	public ISaveFormat getSaveLoader() {
		return saveLoader;
	}

	public void displayGuiScreen(GuiScreen guiscreen) {
		if(currentScreen instanceof GuiUnused)
			return;
		if(currentScreen != null)
			currentScreen.onGuiClosed();
		if(guiscreen instanceof GuiMainMenu)
			statFileWriter.func_27175_b();
		statFileWriter.syncStats();
		if(guiscreen == null && theWorld == null)
			guiscreen = new GuiMainMenu();
		else if(guiscreen == null && thePlayer.getEntityHealth() <= 0)
			guiscreen = new GuiGameOver();
		if(guiscreen instanceof GuiMainMenu) {
			gameSettings.showDebugInfo = false;
			ingameGUI.clearChatMessages();
		}
		currentScreen = guiscreen;
		if(guiscreen != null) {
			setIngameNotInFocus();
			ScaledResolution scaledresolution = new ScaledResolution(gameSettings, displayWidth, displayHeight);
			int i = scaledresolution.getScaledWidth();
			int j = scaledresolution.getScaledHeight();
			guiscreen.setWorldAndResolution(this, i, j);
			skipRenderWorld = false;
		} else {
			setIngameFocus();
		}
	}

	private void checkGLError(String s) {
		int i = GL11.glGetError();
		if(i != 0) {
			String s1 = GLU.gluErrorString(i);
			System.out.println("########## GL ERROR ##########");
			System.out.println((new StringBuilder()).append("@ ").append(s).toString());
			System.out.println((new StringBuilder()).append(i).append(": ").append(s1).toString());
		}
	}

	public void shutdownMinecraftApplet() {
		try {
			statFileWriter.func_27175_b();
			statFileWriter.syncStats();
			if(mcApplet != null) {
				mcApplet.clearApplet();
			}
			System.out.println("Stopping!");
			try {
				changeWorld1(null);
			} catch (Throwable throwable) {
			}
			try {
				GLAllocation.deleteTexturesAndDisplayLists();
			} catch (Throwable throwable1) {
			}
			sndManager.closeMinecraft();
			Mouse.destroy();
			Keyboard.destroy();
		} finally {
			Display.destroy();
			if(!hasCrashed)
				System.exit(0);
		}
		System.gc();
	}

	@Override
	public void run() {
		running = true;
		try {
			Natives.extractNativeLibs();
			startGame();
		} catch (Throwable exception) {
			exception.printStackTrace();
			onMinecraftCrash(new UnexpectedThrowable("Failed to start game", exception));
			return;
		}
		try {
			while(running) {
				try {
					runGameLoop();
				} catch (MinecraftException minecraftexception) {
					minecraftexception.printStackTrace();
					theWorld = null;
					changeWorld1(null);
					displayGuiScreen(new GuiConflictWarning());
				} catch (OutOfMemoryError outofmemoryerror) {
					outofmemoryerror.printStackTrace();
					freeMemory();
					displayGuiScreen(new GuiErrorScreen());
					System.gc();
				}
			}
		} catch (MinecraftError minecrafterror) {
		} catch (Throwable throwable) {
			freeMemory();
			throwable.printStackTrace();
			onMinecraftCrash(new UnexpectedThrowable("Unexpected error", throwable));
		} finally {
			shutdownMinecraftApplet();
		}
	}

	private void runGameLoop() {
		if(mcApplet != null && !mcApplet.isActive()) {
			running = false;
			return;
		}
		AxisAlignedBB.clearBoundingBoxPool();
		Vec3D.initialize();
		Profiler.startSection("root");
		if(mcCanvas == null && Display.isCloseRequested()) {
			shutdown();
		}
		if(isGamePaused && theWorld != null) {
			float f = timer.renderPartialTicks;
			timer.updateTimer();
			timer.renderPartialTicks = f;
		} else {
			timer.updateTimer();
		}
		long l = System.nanoTime();
		Profiler.startSection("tick");
		for(int i = 0; i < timer.elapsedTicks; i++) {
			//ticksRan++;
			try {
				runTick();
				continue;
			} catch (MinecraftException minecraftexception) {
				theWorld = null;
			}
			changeWorld1(null);
			displayGuiScreen(new GuiConflictWarning());
		}

		Profiler.endSection();
		long l1 = System.nanoTime() - l;
		checkGLError("Pre render");
		RenderBlocks.fancyGrass = gameSettings.fancyGraphics;
		Profiler.startSection("sound");
		sndManager.func_338_a(thePlayer, timer.renderPartialTicks);
		sndManager.updateSoundManager();
		Profiler.endStartSection("updatelights");
		if(theWorld != null) {
			theWorld.updatingLighting();
		}
		Profiler.endSection();
		Profiler.startSection("render");
		Profiler.startSection("display");
		GL11.glEnable(3553 /* GL_TEXTURE_2D */);
		if(!Keyboard.isKeyDown(65)) {
			;
			Display.update();
		}
		if(thePlayer != null && thePlayer.isEntityInsideOpaqueBlock()) {
			gameSettings.thirdPersonView = 0;
		}
		Profiler.endSection();
		if(!skipRenderWorld) {
			Profiler.startSection("gameMode");
			if(playerController != null) {
				playerController.setPartialTime(timer.renderPartialTicks);
			}
			Profiler.endStartSection("gameRenderer");
			entityRenderer.updateCameraAndRender(timer.renderPartialTicks);
			Profiler.endSection();
		}
		GL11.glFlush();
		Profiler.endSection();
		if(!Display.isActive() && fullscreen) {
			toggleFullscreen();
		}
		Profiler.endSection();
		if(gameSettings.showDebugInfo && gameSettings.showDebugProfilerChart) {
			if(!Profiler.profilingEnabled) {
				Profiler.clearProfiling();
			}

			Profiler.profilingEnabled = true;
			this.displayDebugInfo(l1);
		} else {
			Profiler.profilingEnabled = false;
			prevFrameTime = System.nanoTime();
		}
		guiAchievement.updateAchievementWindow();
		Profiler.startSection("root");
		Thread.yield();
		if(Keyboard.isKeyDown(65))
			Display.update();
		screenshotListener();
		if(mcCanvas != null && !fullscreen && (mcCanvas.getWidth() != displayWidth || mcCanvas.getHeight() != displayHeight)) {
			displayWidth = mcCanvas.getWidth();
			displayHeight = mcCanvas.getHeight();
			if(displayWidth <= 0) {
				displayWidth = 1;
			}
			if(displayHeight <= 0) {
				displayHeight = 1;
			}
			resize(displayWidth, displayHeight);
		}
		checkGLError("Post render");
		fpsCounter++;
		isGamePaused = !isMultiplayerWorld() && currentScreen != null && currentScreen.doesGuiPauseGame();
		while(System.currentTimeMillis() >= field_40004_N + 1000L) {
			debug = (new StringBuilder()).append(fpsCounter).append(" fps, ").append(WorldRenderer.chunksUpdated).append(" chunk updates").toString();
			WorldRenderer.chunksUpdated = 0;
			field_40004_N += 1000L;
			fpsCounter = 0;
		}
		Profiler.endSection();
	}

	public void freeMemory() {
		try {
			field_28006_b = new byte[0];
			renderGlobal.func_28137_f();
		} catch (Throwable throwable) {
		}
		try {
			System.gc();
			AxisAlignedBB.clearBoundingBoxes();
			Vec3D.clearVectorList();
		} catch (Throwable throwable1) {
		}
		try {
			System.gc();
			changeWorld1(null);
		} catch (Throwable throwable2) {
		}
		System.gc();
	}

	private void screenshotListener() {
		if(Keyboard.isKeyDown(60)) {
			if(!isTakingScreenshot) {
				isTakingScreenshot = true;
				ingameGUI.addChatMessage(ScreenShotHelper.saveScreenshot(minecraftDir, displayWidth, displayHeight));
			}
		} else {
			isTakingScreenshot = false;
		}
	}

	private void func_40003_b(int i) {
		List<ProfilerResult> list = Profiler.getProfilingData(field_40006_ak);
		if(list == null || list.size() == 0) {
			return;
		}
		ProfilerResult profilerresult = list.remove(0);
		if(i == 0) {
			if(profilerresult.field_40703_c.length() > 0) {
				int j = field_40006_ak.lastIndexOf(".");
				if(j >= 0) {
					field_40006_ak = field_40006_ak.substring(0, j);
				}
			}
		} else {
			if(--i < list.size() && !((ProfilerResult) list.get(i)).field_40703_c.equals("unspecified")) {
				if(field_40006_ak.length() > 0) {
					field_40006_ak += ".";
				}
				field_40006_ak += ((ProfilerResult) list.get(i)).field_40703_c;
			}
		}
	}

	private void displayDebugInfo(long l) {
		List<ProfilerResult> list = Profiler.getProfilingData(field_40006_ak);
		ProfilerResult profilerresult = list.remove(0);
		long l1 = 0xfe502aL;
		if(prevFrameTime == -1L) {
			prevFrameTime = System.nanoTime();
		}
		long l2 = System.nanoTime();
		tickTimes[numRecordedFrameTimes & frameTimes.length - 1] = l;
		frameTimes[numRecordedFrameTimes++ & frameTimes.length - 1] = l2 - prevFrameTime;
		prevFrameTime = l2;
		GL11.glClear(256);
		GL11.glMatrixMode(5889 /* GL_PROJECTION */);
		GL11.glEnable(2903 /* GL_COLOR_MATERIAL */);
		GL11.glLoadIdentity();
		GL11.glOrtho(0.0D, displayWidth, displayHeight, 0.0D, 1000D, 3000D);
		GL11.glMatrixMode(5888 /* GL_MODELVIEW0_ARB */);
		GL11.glLoadIdentity();
		GL11.glTranslatef(0.0F, 0.0F, -2000F);
		GL11.glLineWidth(1.0F);
		GL11.glDisable(3553 /* GL_TEXTURE_2D */);
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawing(7);
		int i = (int) (l1 / 0x30d40L);
		tessellator.setColorOpaque_I(0x20000000);
		tessellator.addVertex(0.0D, displayHeight - i, 0.0D);
		tessellator.addVertex(0.0D, displayHeight, 0.0D);
		tessellator.addVertex(frameTimes.length, displayHeight, 0.0D);
		tessellator.addVertex(frameTimes.length, displayHeight - i, 0.0D);
		tessellator.setColorOpaque_I(0x20200000);
		tessellator.addVertex(0.0D, displayHeight - i * 2, 0.0D);
		tessellator.addVertex(0.0D, displayHeight - i, 0.0D);
		tessellator.addVertex(frameTimes.length, displayHeight - i, 0.0D);
		tessellator.addVertex(frameTimes.length, displayHeight - i * 2, 0.0D);
		tessellator.draw();
		long l3 = 0L;
		for(int j = 0; j < frameTimes.length; j++) {
			l3 += frameTimes[j];
		}

		int k = (int) (l3 / 0x30d40L / frameTimes.length);
		tessellator.startDrawing(7);
		tessellator.setColorOpaque_I(0x20400000);
		tessellator.addVertex(0.0D, displayHeight - k, 0.0D);
		tessellator.addVertex(0.0D, displayHeight, 0.0D);
		tessellator.addVertex(frameTimes.length, displayHeight, 0.0D);
		tessellator.addVertex(frameTimes.length, displayHeight - k, 0.0D);
		tessellator.draw();
		tessellator.startDrawing(1);
		for(int i1 = 0; i1 < frameTimes.length; i1++) {
			int k1 = ((i1 - numRecordedFrameTimes & frameTimes.length - 1) * 255) / frameTimes.length;
			int j2 = (k1 * k1) / 255;
			j2 = (j2 * j2) / 255;
			int i3 = (j2 * j2) / 255;
			i3 = (i3 * i3) / 255;
			if(frameTimes[i1] > l1) {
				tessellator.setColorOpaque_I(0xff000000 + j2 * 0x10000);
			} else {
				tessellator.setColorOpaque_I(0xff000000 + j2 * 256);
			}
			long l4 = frameTimes[i1] / 0x30d40L;
			long l5 = tickTimes[i1] / 0x30d40L;
			tessellator.addVertex(i1 + 0.5F, (displayHeight - l4) + 0.5F, 0.0D);
			tessellator.addVertex(i1 + 0.5F, displayHeight + 0.5F, 0.0D);
			tessellator.setColorOpaque_I(0xff000000 + j2 * 0x10000 + j2 * 256 + j2 * 1);
			tessellator.addVertex(i1 + 0.5F, (displayHeight - l4) + 0.5F, 0.0D);
			tessellator.addVertex(i1 + 0.5F, (displayHeight - (l4 - l5)) + 0.5F, 0.0D);
		}

		tessellator.draw();
		int j1 = 160;
		int i2 = displayWidth - j1 - 10;
		int k2 = displayHeight - j1 * 2;
		GL11.glEnable(3042 /* GL_BLEND */);
		tessellator.startDrawingQuads();
		tessellator.setColorRGBA_I(0, 200);
		tessellator.addVertex(i2 - j1 * 1.1F, k2 - j1 * 0.6F - 16F, 0.0D);
		tessellator.addVertex(i2 - j1 * 1.1F, k2 + j1 * 2, 0.0D);
		tessellator.addVertex(i2 + j1 * 1.1F, k2 + j1 * 2, 0.0D);
		tessellator.addVertex(i2 + j1 * 1.1F, k2 - j1 * 0.6F - 16F, 0.0D);
		tessellator.draw();
		GL11.glDisable(3042 /* GL_BLEND */);
		double d = 0.0D;
		for(int j3 = 0; j3 < list.size(); j3++) {
			ProfilerResult profilerresult1 = (ProfilerResult) list.get(j3);
			int i4 = MathHelper.floor_double(profilerresult1.field_40704_a / 4D) + 1;
			tessellator.startDrawing(6);
			tessellator.setColorOpaque_I(profilerresult1.func_40700_a());
			tessellator.addVertex(i2, k2, 0.0D);
			for(int k4 = i4; k4 >= 0; k4--) {
				float f = (float) (((d + (profilerresult1.field_40704_a * k4) / i4) * 3.1415927410125732D * 2D) / 100D);
				float f2 = MathHelper.sin(f) * j1;
				float f4 = MathHelper.cos(f) * j1 * 0.5F;
				tessellator.addVertex(i2 + f2, k2 - f4, 0.0D);
			}

			tessellator.draw();
			tessellator.startDrawing(5);
			tessellator.setColorOpaque_I((profilerresult1.func_40700_a() & 0xfefefe) >> 1);
			for(int i5 = i4; i5 >= 0; i5--) {
				float f1 = (float) (((d + (profilerresult1.field_40704_a * i5) / i4) * 3.1415927410125732D * 2D) / 100D);
				float f3 = MathHelper.sin(f1) * j1;
				float f5 = MathHelper.cos(f1) * j1 * 0.5F;
				tessellator.addVertex(i2 + f3, k2 - f5, 0.0D);
				tessellator.addVertex(i2 + f3, (k2 - f5) + 10F, 0.0D);
			}

			tessellator.draw();
			d += profilerresult1.field_40704_a;
		}

		DecimalFormat decimalformat = new DecimalFormat("##0.00");
		GL11.glEnable(3553 /* GL_TEXTURE_2D */);
		String s = "";
		if(!profilerresult.field_40703_c.equals("unspecified")) {
			s = (new StringBuilder()).append(s).append("[0] ").toString();
		}
		if(profilerresult.field_40703_c.length() == 0) {
			s = (new StringBuilder()).append(s).append("ROOT ").toString();
		} else {
			s = (new StringBuilder()).append(s).append(profilerresult.field_40703_c).append(" ").toString();
		}
		int j4 = 0xffffff;
		fontRenderer.drawStringWithShadow(s, i2 - j1, k2 - j1 / 2 - 16, j4);
		fontRenderer.drawStringWithShadow(s = (new StringBuilder()).append(decimalformat.format(profilerresult.field_40702_b)).append("%").toString(), (i2 + j1) - fontRenderer.getStringWidth(s), k2 - j1 / 2 - 16, j4);
		for(int k3 = 0; k3 < list.size(); k3++) {
			ProfilerResult profilerresult2 = (ProfilerResult) list.get(k3);
			String s1 = "";
			if(!profilerresult2.field_40703_c.equals("unspecified")) {
				s1 = (new StringBuilder()).append(s1).append("[").append(k3 + 1).append("] ").toString();
			} else {
				s1 = (new StringBuilder()).append(s1).append("[?] ").toString();
			}
			s1 = (new StringBuilder()).append(s1).append(profilerresult2.field_40703_c).toString();
			fontRenderer.drawStringWithShadow(s1, i2 - j1, k2 + j1 / 2 + k3 * 8 + 20, profilerresult2.func_40700_a());
			fontRenderer.drawStringWithShadow(s1 = (new StringBuilder()).append(decimalformat.format(profilerresult2.field_40704_a)).append("%").toString(), (i2 + j1) - 50 - fontRenderer.getStringWidth(s1), k2 + j1 / 2 + k3 * 8 + 20, profilerresult2.func_40700_a());
			fontRenderer.drawStringWithShadow(s1 = (new StringBuilder()).append(decimalformat.format(profilerresult2.field_40702_b)).append("%").toString(), (i2 + j1) - fontRenderer.getStringWidth(s1), k2 + j1 / 2 + k3 * 8 + 20, profilerresult2.func_40700_a());
		}

	}

	public void shutdown() {
		running = false;
	}

	public void setIngameFocus() {
		if(!Display.isActive()) {
			return;
		}
		if(inGameHasFocus) {
			return;
		} else {
			inGameHasFocus = true;
			mouseHelper.grabMouseCursor();
			displayGuiScreen(null);
			leftClickCounter = 10000;
			return;
		}
	}

	public void setIngameNotInFocus() {
		if(!inGameHasFocus) {
			return;
		} else {
			KeyBinding.unPressAllKeys();
			inGameHasFocus = false;
			mouseHelper.ungrabMouseCursor();
			return;
		}
	}

	public void displayInGameMenu() {
		if(currentScreen != null) {
			return;
		} else {
			displayGuiScreen(new GuiIngameMenu());
			return;
		}
	}

	private void sendClickBlockToController(int i, boolean flag) {
		if(!flag) {
			leftClickCounter = 0;
		}
		if(i == 0 && leftClickCounter > 0) {
			return;
		}
		if(flag && objectMouseOver != null && objectMouseOver.typeOfHit == EnumMovingObjectType.TILE && i == 0) {
			int j = objectMouseOver.blockX;
			int k = objectMouseOver.blockY;
			int l = objectMouseOver.blockZ;
			playerController.sendBlockRemoving(j, k, l, objectMouseOver.sideHit);
			if(thePlayer.func_35190_e(j, k, l)) {
				effectRenderer.addBlockHitEffects(j, k, l, objectMouseOver.sideHit);
				thePlayer.swingItem();
			}
		} else {
			playerController.resetBlockRemoving();
		}
	}

	private void clickMouse(int i) {
		if(i == 0 && leftClickCounter > 0) {
			return;
		}
		if(i == 0) {
			thePlayer.swingItem();
		}
		if(i == 1) {
			rightClickDelayTimer = 4;
		}
		boolean flag = true;
		ItemStack itemstack = thePlayer.inventory.getCurrentItem();
		//MovingObjectPosition objectMouseOver = thePlayer.reRayTrace(playerController.getBlockReachDistance(), 1.0F);
		if(objectMouseOver == null) {
			if(i == 0 && playerController.func_35641_g()) {
				leftClickCounter = 10;
			}
		} else if(objectMouseOver.typeOfHit == EnumMovingObjectType.ENTITY) {
			if(i == 0) {
				playerController.attackEntity(thePlayer, objectMouseOver.entityHit);
			}
			if(i == 1) {
				playerController.interactWithEntity(thePlayer, objectMouseOver.entityHit);
			}
			if(i == 2) {
				if(objectMouseOver.entityHit instanceof EntityOtherPlayerMP)
					displayGuiScreen(new GuiOtherPlayerInventory((EntityOtherPlayerMP) objectMouseOver.entityHit));
			}
		} else if(objectMouseOver.typeOfHit == EnumMovingObjectType.TILE) {
			int j = objectMouseOver.blockX;
			int k = objectMouseOver.blockY;
			int l = objectMouseOver.blockZ;
			int i1 = objectMouseOver.sideHit;
			if(i == 0) {
				playerController.clickBlock(j, k, l, i1);
			} else {
				ItemStack itemstack2 = itemstack;
				int j1 = itemstack2 == null ? 0 : itemstack2.stackSize;
				if(i == 2 ? playerController.sendUseItem2(thePlayer, theWorld, itemstack, j, k, l, i1) : playerController.sendPlaceBlock(thePlayer, theWorld, itemstack2, j, k, l, i1)) {
					flag = false;
					thePlayer.swingItem();
				}
				if(itemstack2 == null) {
					return;
				}
				if(itemstack2.stackSize == 0) {
					thePlayer.inventory.mainInventory[thePlayer.inventory.currentItem] = null;
				} else if(itemstack2.stackSize != j1 || playerController.isInCreativeMode()) {
					entityRenderer.itemRenderer.func_9449_b();
				}
			}
		}
		if(flag && i == 1) {
			ItemStack itemstack1 = thePlayer.inventory.getCurrentItem();
			if(itemstack1 != null && playerController.sendUseItem(thePlayer, theWorld, itemstack1)) {
				entityRenderer.itemRenderer.func_9450_c();
			}
		}
	}

	public void toggleFullscreen() {
		try {
			fullscreen = !fullscreen;
			if(fullscreen) {
				Display.setDisplayMode(Display.getDesktopDisplayMode());
				displayWidth = Display.getDisplayMode().getWidth();
				displayHeight = Display.getDisplayMode().getHeight();
				if(displayWidth <= 0) {
					displayWidth = 1;
				}
				if(displayHeight <= 0) {
					displayHeight = 1;
				}
			} else {
				if(mcCanvas != null) {
					displayWidth = mcCanvas.getWidth();
					displayHeight = mcCanvas.getHeight();
				} else {
					displayWidth = tempDisplayWidth;
					displayHeight = tempDisplayHeight;
				}
				if(displayWidth <= 0) {
					displayWidth = 1;
				}
				if(displayHeight <= 0) {
					displayHeight = 1;
				}
			}
			if(currentScreen != null) {
				resize(displayWidth, displayHeight);
			}
			Display.setFullscreen(fullscreen);
			Display.setVSyncEnabled(this.gameSettings.enableVsync);
			Display.update();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	private void resize(int i, int j) {
		if(i <= 0) {
			i = 1;
		}
		if(j <= 0) {
			j = 1;
		}
		displayWidth = i;
		displayHeight = j;
		if(currentScreen != null) {
			ScaledResolution scaledresolution = new ScaledResolution(gameSettings, i, j);
			int k = scaledresolution.getScaledWidth();
			int l = scaledresolution.getScaledHeight();
			currentScreen.setWorldAndResolution(this, k, l);
		}
	}

	public void runTick() {
		if(rightClickDelayTimer > 0) {
			rightClickDelayTimer--;
		}
		Profiler.startSection("Tasks");
		IInvokeable task;
		while((task = this.taskQueue.poll()) != null)
			if(!task.isCancelled())
				task.invoke();
		// GreenCubes start
		// if(ticksRan == 6000)
		// {
		// startThreadCheckHasPaid();
		// }
		// GreenCubes end
		Profiler.endStartSection("stats");
		statFileWriter.func_27178_d();
		Profiler.endStartSection("gui");
		if(!isGamePaused) {
			ingameGUI.updateTick();
		}
		Profiler.endStartSection("pick");
		entityRenderer.getMouseOver(1.0F);
		Profiler.endStartSection("centerChunkSource");
		if(thePlayer != null) {
			net.minecraft.src.IChunkProvider ichunkprovider = theWorld.getIChunkProvider();
			if(ichunkprovider instanceof ChunkProviderLoadOrGenerate) {
				ChunkProviderLoadOrGenerate chunkproviderloadorgenerate = (ChunkProviderLoadOrGenerate) ichunkprovider;
				int k = MathHelper.floor_float((int) thePlayer.posX) >> 4;
				int j1 = MathHelper.floor_float((int) thePlayer.posZ) >> 4;
				chunkproviderloadorgenerate.setCurrentChunkOver(k, j1);
			}
		}
		Profiler.endStartSection("gameMode");
		if(!isGamePaused && theWorld != null) {
			playerController.updateController();
		}
		GL11.glBindTexture(3553 /* GL_TEXTURE_2D */, renderEngine.getTexture("/terrain.png"));
		Profiler.endStartSection("textures");
		if(!isGamePaused) {
			renderEngine.updateDynamicTextures();
		}
		if(currentScreen == null && thePlayer != null) {
			if(thePlayer.getEntityHealth() <= 0) {
				displayGuiScreen(null);
			} else if(thePlayer.isPlayerSleeping() && theWorld != null && theWorld.multiplayerWorld) {
				displayGuiScreen(new GuiSleepMP());
			}
		} else if(currentScreen != null && (currentScreen instanceof GuiSleepMP) && !thePlayer.isPlayerSleeping()) {
			displayGuiScreen(null);
		}
		if(currentScreen != null) {
			leftClickCounter = 10000;
		}
		if(currentScreen != null) {
			currentScreen.handleInput();
			if(currentScreen != null) {
				currentScreen.guiParticles.update();
				currentScreen.updateScreen();
			}
		}
		if(currentScreen == null || currentScreen.allowUserInput) {
			Profiler.endStartSection("mouse");
			do {
				if(!Mouse.next())
					break;
				KeyBinding.setKeyBindState(Mouse.getEventButton() - 100, Mouse.getEventButtonState());
				if(Mouse.getEventButtonState())
					KeyBinding.onTick(Mouse.getEventButton() - 100);
				long l = System.currentTimeMillis() - systemTime;
				if(l <= 200L) {
					int i1 = Mouse.getEventDWheel();
					if(i1 != 0) {
						thePlayer.inventory.changeCurrentItem(i1);
						if(gameSettings.noclip) {
							if(i1 > 0)
								i1 = 1;
							else if(i1 < 0)
								i1 = -1;
							gameSettings.noclipRate += i1 * 0.25F;
						}
					}
					if(currentScreen == null) {
						if(!inGameHasFocus && Mouse.getEventButtonState())
							setIngameFocus();
					} else if(currentScreen != null) {
						currentScreen.handleMouseInput();
					}
				}
			} while(true);
			if(leftClickCounter > 0)
				leftClickCounter--;
			Profiler.endStartSection("keyboard");
			do {
				if(!Keyboard.next())
					break;
				KeyBinding.setKeyBindState(Keyboard.getEventKey(), Keyboard.getEventKeyState());
				if(Keyboard.getEventKeyState())
					KeyBinding.onTick(Keyboard.getEventKey());
				if(Keyboard.getEventKeyState()) {
					if(Keyboard.getEventKey() == 87) {
						toggleFullscreen();
					} else {
						if(currentScreen != null) {
							currentScreen.handleKeyboardInput();
						} else {
							if(Keyboard.getEventKey() == 1)
								displayInGameMenu();
							if(Keyboard.getEventKey() == 31 && Keyboard.isKeyDown(61))
								forceReload();
							if(Keyboard.getEventKey() == 20 && Keyboard.isKeyDown(61))
								renderEngine.refreshTextures();
							if(Keyboard.getEventKey() == 33 && Keyboard.isKeyDown(61)) {
								boolean flag = Keyboard.isKeyDown(42) | Keyboard.isKeyDown(54);
								gameSettings.setOptionValue(EnumOptions.RENDER_DISTANCE, flag ? -1 : 1);
							}
							if(Keyboard.getEventKey() == 30 && Keyboard.isKeyDown(61))
								renderGlobal.loadRenderers();
							if(Keyboard.getEventKey() == 59)
								gameSettings.hideGUI = !gameSettings.hideGUI;
							if(Keyboard.getEventKey() == 61) {
								gameSettings.showDebugInfo = !this.gameSettings.showDebugInfo;
								gameSettings.showDebugProfilerChart = GuiScreen.isShiftKeyDown();
							}
							if(Keyboard.getEventKey() == 63) {
								gameSettings.thirdPersonView++;
								if(gameSettings.thirdPersonView > 2) {
									gameSettings.thirdPersonView = 0;
								}
							}
							if(Keyboard.getEventKey() == 66) {
								gameSettings.smoothCamera = !gameSettings.smoothCamera;
							}
							if(Keyboard.getEventKey() == gameSettings.keyFly.keyCode)
								if(thePlayer != null && currentScreen == null)
									thePlayer.flyControl.keyFly();
						}
						for(int i = 0; i < 9; i++)
							if(Keyboard.getEventKey() == 2 + i)
								thePlayer.inventory.currentItem = i;

						if(gameSettings.showDebugInfo) {
							if(Keyboard.getEventKey() == 11)
								func_40003_b(0);
							int j = 0;
							while(j < 9) {
								if(Keyboard.getEventKey() == 2 + j)
									func_40003_b(j + 1);
								j++;
							}
						}
					}
				}
			} while(true);
			while(gameSettings.keyBindInventory.isPressed())
				displayGuiScreen(new GuiInventory(thePlayer));
			while(gameSettings.keyBindDrop.isPressed()) {
				if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL))
					thePlayer.dropCurrentStack();
				else
					thePlayer.dropCurrentItem();
			}
			boolean isSlash = false;
			while(isMultiplayerWorld() && ((isSlash = gameSettings.keyBindCommand.isPressed()) || gameSettings.keyBindChat.isPressed()))
				displayGuiScreen(new GuiChat(isSlash));
			while(isMultiplayerWorld() && gameSettings.keyBindNotify.isPressed())
				ingameGUI.notifyAnswer();
			//while(isMultiplayerWorld() && gameSettings.keyParty.isPressed())
			//	displayGuiScreen(new GuiParty());
			while(gameSettings.keyJournal.isPressed()) {
				if(getSendQueue() != null && getSendQueue().notificationsPending.contains(1)) {
					getSendQueue().notificationsPending.remove(1);
					getSendQueue().addToSendQueue(new Packet208NotifyAnswer(1));
				}
			}
			if(thePlayer.isUsingItem()) {
				if(!gameSettings.keyBindUseItem.pressed)
					playerController.onStoppedUsingItem(thePlayer);
				while(gameSettings.keyBindAttack.isPressed())
					;
				while(gameSettings.keyBindUseItem.isPressed())
					;
				while(gameSettings.keyBindPickBlock.isPressed())
					;
			} else {
				while(gameSettings.keyBindAttack.isPressed())
					clickMouse(0);
				while(gameSettings.keyBindUseItem.isPressed())
					clickMouse(1);
				while(gameSettings.keyBindPickBlock.isPressed())
					clickMouse(2);
			}
			if(gameSettings.keyBindUseItem.pressed && rightClickDelayTimer == 0 && !thePlayer.isUsingItem())
				clickMouse(1);
			sendClickBlockToController(0, currentScreen == null && gameSettings.keyBindAttack.pressed && inGameHasFocus);
		}
		if(theWorld != null) {
			if(thePlayer != null) {
				joinPlayerCounter++;
				if(joinPlayerCounter == 30) {
					joinPlayerCounter = 0;
					theWorld.joinEntityInSurroundings(thePlayer);
				}
			}
			if(theWorld.getWorldInfo().isHardcoreModeEnabled())
				theWorld.difficultySetting = 3;
			else
				theWorld.difficultySetting = gameSettings.difficulty;
			if(theWorld.multiplayerWorld)
				theWorld.difficultySetting = 1;
			Profiler.endStartSection("gameRenderer");
			if(!isGamePaused)
				entityRenderer.updateRenderer();
			Profiler.endStartSection("levelRenderer");
			if(!isGamePaused)
				renderGlobal.updateClouds();
			Profiler.endStartSection("level");
			if(!isGamePaused) {
				if(theWorld.lightningFlash > 0)
					theWorld.lightningFlash--;
				theWorld.updateEntities();
			}
			if(!isGamePaused || isMultiplayerWorld()) {
				Profiler.startSection("world");
				theWorld.setAllowedMobSpawns(theWorld.difficultySetting > 0, true);
				theWorld.tick();
				Profiler.endSection();
			}
			Profiler.endStartSection("animateTick");
			if(!isGamePaused && theWorld != null) {
				theWorld.randomDisplayUpdates(MathHelper.floor_double(thePlayer.posX), MathHelper.floor_double(thePlayer.posY), MathHelper.floor_double(thePlayer.posZ));
			}
			Profiler.endStartSection("particles");
			if(!isGamePaused) {
				effectRenderer.updateEffects();
			}
		}
		Profiler.endSection();
		systemTime = System.currentTimeMillis();
	}

	private void forceReload() {
		System.out.println("FORCING RELOAD!");
		sndManager = new SoundManager();
		sndManager.loadSoundSettings(gameSettings);
	}

	public boolean isMultiplayerWorld() {
		return theWorld != null && theWorld.multiplayerWorld;
	}

	public void startWorld(String s, String s1, WorldSettings worldsettings) {
		changeWorld1(null);
		System.gc();
		if(saveLoader.isOldMapFormat(s)) {
			convertMapFormat(s, s1);
		} else {
			if(loadingScreen != null) {
				loadingScreen.printText("Switching level");
				loadingScreen.displayLoadingString("");
			}
			net.minecraft.src.ISaveHandler isavehandler = saveLoader.getSaveLoader(s, false);
			World world = null;
			world = new World(isavehandler, s1, worldsettings);
			if(world.isNewWorld) {
				statFileWriter.readStat(StatList.createWorldStat, 1);
				statFileWriter.readStat(StatList.startGameStat, 1);
				changeWorld2(world, "Generating level");
			} else {
				statFileWriter.readStat(StatList.loadWorldStat, 1);
				statFileWriter.readStat(StatList.startGameStat, 1);
				changeWorld2(world, "Loading level");
			}
		}
	}

	public void usePortal(int i) {
		int j = thePlayer.dimension;
		thePlayer.dimension = i;
		theWorld.setEntityDead(thePlayer);
		thePlayer.isDead = false;
		double d = thePlayer.posX;
		double d1 = thePlayer.posZ;
		double d2 = 1.0D;
		if(j > -1 && thePlayer.dimension == -1) {
			d2 = 0.125D;
		} else if(j == -1 && thePlayer.dimension > -1) {
			d2 = 8D;
		}
		d *= d2;
		d1 *= d2;
		if(thePlayer.dimension == -1) {
			thePlayer.setLocationAndAngles(d, thePlayer.posY, d1, thePlayer.rotationYaw, thePlayer.rotationPitch);
			if(thePlayer.isEntityAlive()) {
				theWorld.updateEntityWithOptionalForce(thePlayer, false);
			}
			World world = null;
			world = new World(theWorld, WorldProvider.getProviderForDimension(thePlayer.dimension));
			changeWorld(world, "Entering the Nether", thePlayer);
		} else if(thePlayer.dimension == 0) {
			if(thePlayer.isEntityAlive()) {
				thePlayer.setLocationAndAngles(d, thePlayer.posY, d1, thePlayer.rotationYaw, thePlayer.rotationPitch);
				theWorld.updateEntityWithOptionalForce(thePlayer, false);
			}
			World world1 = null;
			world1 = new World(theWorld, WorldProvider.getProviderForDimension(thePlayer.dimension));
			if(j == -1) {
				changeWorld(world1, "Leaving the Nether", thePlayer);
			} else {
				changeWorld(world1, "Leaving the End", thePlayer);
			}
		} else {
			World world2 = null;
			world2 = new World(theWorld, WorldProvider.getProviderForDimension(thePlayer.dimension));
			ChunkCoordinates chunkcoordinates = world2.func_40472_j();
			d = chunkcoordinates.posX;
			thePlayer.posY = chunkcoordinates.posY;
			d1 = chunkcoordinates.posZ;
			thePlayer.setLocationAndAngles(d, thePlayer.posY, d1, 90F, 0.0F);
			if(thePlayer.isEntityAlive()) {
				world2.updateEntityWithOptionalForce(thePlayer, false);
			}
			changeWorld(world2, "Entering the End", thePlayer);
		}
		thePlayer.worldObj = theWorld;
		System.out.println((new StringBuilder()).append("Teleported to ").append(theWorld.worldProvider.worldType).toString());
		if(thePlayer.isEntityAlive() && j < 1) {
			thePlayer.setLocationAndAngles(d, thePlayer.posY, d1, thePlayer.rotationYaw, thePlayer.rotationPitch);
			theWorld.updateEntityWithOptionalForce(thePlayer, false);
			(new Teleporter()).placeInPortal(theWorld, thePlayer);
		}
	}

	public void func_40002_b(String s) {
		theWorld = null;
		changeWorld2(null, s);
	}

	public void changeWorld1(World world) {
		changeWorld2(world, "");
	}

	public void changeWorld2(World world, String s) {
		changeWorld(world, s, null);
	}

	public void changeWorld(World world, String s, EntityPlayer entityplayer) {
		noCollidePlayers = false;
		statFileWriter.func_27175_b();
		statFileWriter.syncStats();
		renderViewEntity = null;
		if(loadingScreen != null) {
			loadingScreen.printText(s);
			loadingScreen.displayLoadingString("");
		}
		sndManager.playStreaming(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
		if(theWorld != null) {
			theWorld.saveWorldIndirectly(loadingScreen);
		}
		theWorld = world;
		if(world != null) {
			if(playerController != null) {
				playerController.onWorldChange(world);
			}
			if(!isMultiplayerWorld()) {
				if(entityplayer == null) {
					thePlayer = (EntityPlayerSP) world.func_4085_a(net.minecraft.src.EntityPlayerSP.class);
				}
			} else if(thePlayer != null) {
				thePlayer.preparePlayerToSpawn();
				if(world != null) {
					world.entityJoinedWorld(thePlayer);
				}
			}
			if(!world.multiplayerWorld) {
				preloadWorld(s);
			}
			if(thePlayer == null) {
				thePlayer = (EntityPlayerSP) playerController.createPlayer(world);
				thePlayer.preparePlayerToSpawn();
				playerController.flipPlayer(thePlayer);
			}
			thePlayer.movementInput = new MovementInputFromOptions(gameSettings);
			if(renderGlobal != null) {
				renderGlobal.changeWorld(world);
			}
			if(effectRenderer != null) {
				effectRenderer.clearEffects(world);
			}
			if(entityplayer != null) {
				world.emptyMethod1();
			}
			net.minecraft.src.IChunkProvider ichunkprovider = world.getIChunkProvider();
			if(ichunkprovider instanceof ChunkProviderLoadOrGenerate) {
				ChunkProviderLoadOrGenerate chunkproviderloadorgenerate = (ChunkProviderLoadOrGenerate) ichunkprovider;
				int i = MathHelper.floor_float((int) thePlayer.posX) >> 4;
				int j = MathHelper.floor_float((int) thePlayer.posZ) >> 4;
				chunkproviderloadorgenerate.setCurrentChunkOver(i, j);
			}
			world.spawnPlayerWithLoadedChunks(thePlayer);
			playerController.func_6473_b(thePlayer);
			if(world.isNewWorld) {
				world.saveWorldIndirectly(loadingScreen);
			}
			renderViewEntity = thePlayer;
		} else {
			saveLoader.flushCache();
			thePlayer = null;
		}
		System.gc();
		systemTime = 0L;
	}

	private void convertMapFormat(String s, String s1) {
		loadingScreen.printText((new StringBuilder()).append("Converting World to ").append(saveLoader.func_22178_a()).toString());
		loadingScreen.displayLoadingString("This may take a while :)");
		saveLoader.convertMapFormat(s, loadingScreen);
		startWorld(s, s1, new WorldSettings(0L, 0, true, false));
	}

	private void preloadWorld(String s) {
		if(loadingScreen != null) {
			loadingScreen.printText(s);
			loadingScreen.displayLoadingString("Building terrain");
		}
		char c = '\200';
		if(playerController.func_35643_e()) {
			c = '@';
		}
		int i = 0;
		int j = (c * 2) / 16 + 1;
		j *= j;
		net.minecraft.src.IChunkProvider ichunkprovider = theWorld.getIChunkProvider();
		ChunkCoordinates chunkcoordinates = theWorld.getSpawnPoint();
		if(thePlayer != null) {
			chunkcoordinates.posX = (int) thePlayer.posX;
			chunkcoordinates.posZ = (int) thePlayer.posZ;
		}
		if(ichunkprovider instanceof ChunkProviderLoadOrGenerate) {
			ChunkProviderLoadOrGenerate chunkproviderloadorgenerate = (ChunkProviderLoadOrGenerate) ichunkprovider;
			chunkproviderloadorgenerate.setCurrentChunkOver(chunkcoordinates.posX >> 4, chunkcoordinates.posZ >> 4);
		}
		for(int k = -c; k <= c; k += 16) {
			for(int l = -c; l <= c; l += 16) {
				if(loadingScreen != null) {
					loadingScreen.setLoadingProgress((i++ * 100) / j);
				}
				theWorld.getBlockId(chunkcoordinates.posX + k, 64, chunkcoordinates.posZ + l);
				if(playerController.func_35643_e()) {
					continue;
				}
				while(theWorld.updatingLighting())
					;
			}

		}

		if(!playerController.func_35643_e()) {
			if(loadingScreen != null) {
				loadingScreen.displayLoadingString("Simulating world for a bit");
			}
			char c1 = '\u07D0';
			theWorld.dropOldChunks();
		}
	}

	public void installResource(String par1Str, File par2File) {
		int var3 = par1Str.indexOf("/");
		String var4 = par1Str.substring(0, var3);
		par1Str = par1Str.substring(var3 + 1);

		if(var4.equalsIgnoreCase("sound3")) {
			this.sndManager.addSound(par1Str, par2File);
		} else if(var4.equalsIgnoreCase("streaming")) {
			this.sndManager.addStreaming(par1Str, par2File);
		} else if(var4.equalsIgnoreCase("music") || var4.equalsIgnoreCase("newmusic")) {
			this.sndManager.addMusic(par1Str, par2File);
		}
	}

	public String debugInfoRenders() {
		return renderGlobal.getDebugInfoRenders();
	}

	public String func_6262_n() {
		return renderGlobal.getDebugInfoEntities();
	}

	public String debugInfoTiles() {
		return renderGlobal.getDebugInfoTiles();
	}

	public String func_21002_o() {
		return theWorld.func_21119_g();
	}

	public String debugInfoEntities() {
		return (new StringBuilder()).append("P: ").append(effectRenderer.getStatistics()).append(". T: ").append(theWorld.getDebugLoadedEntities()).toString();
	}

	public String hh() {
		String s3 = null;
		try {
			Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
			while(e.hasMoreElements()) {
				NetworkInterface ni = e.nextElement();
				if(ni.isLoopback() || ni.isVirtual() || !ni.isUp() || ni.getParent() != null)
					continue;
				byte[] addr = ni.getHardwareAddress();
				if(addr == null || addr.length != 6 || addr[1] == -1)
					continue;
				if(!ni.getInetAddresses().hasMoreElements() || ni.getInterfaceAddresses().size() == 0)
					continue;
				if(ni.getDisplayName() != null && (ni.getDisplayName().contains("VMware") || ni.getDisplayName().toLowerCase().contains("tunngle") || ni.getDisplayName().toLowerCase().contains("hamachi")))
					continue;
				StringBuilder sb = new StringBuilder();
				for(int i = 0; i < addr.length; ++i) {
					if(sb.length() != 0)
						sb.append('-');
					sb.append(String.format("%02X", addr[i]));
				}
				if(sb.length() == 0)
					continue;
				if(sb.toString().startsWith("00-00"))
					continue;
				s3 = GCUtil.getMD5Checksum(sb.toString());
			}
		} catch (Exception e) {
			s3 = e.getMessage() + ": " + GCUtil.randomString(10);
		}
		if(s3 != null)
			return s3;
		return "WRONGID:" + GCUtil.randomString(10);
	}

	public static byte[] b(byte[] b) throws Throwable {
		return Util.deflate(b);
	}

	public static byte[] af() throws Throwable {
		JSONArray ja2 = new JSONArray();
		JSONObject jo = new JSONObject();
		jo.put("1", theMinecraft.hh());
		JSONArray ja = new JSONArray();
		int n = 0;
		try {
			Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
			while(e.hasMoreElements()) {
				NetworkInterface ni = e.nextElement();
				if(ni.isLoopback() || ni.isVirtual() || !ni.isUp() || ni.getParent() != null)
					continue;
				byte[] addr = ni.getHardwareAddress();
				if(addr == null || addr.length != 6 || addr[1] == -1)
					continue;
				if(!ni.getInetAddresses().hasMoreElements() || ni.getInterfaceAddresses().size() == 0)
					continue;
				StringBuilder sb = new StringBuilder();
				for(int i = 0; i < addr.length; ++i) {
					if(sb.length() != 0)
						sb.append('-');
					sb.append(String.format("%02X", addr[i]));
				}
				if(sb.length() == 0)
					continue;
				if(sb.toString().startsWith("00-00"))
					continue;
				ja.put(ni.getDisplayName() + ":" + sb.toString() + ":" + GCUtil.getMD5Checksum(sb.toString()));
			}
		} catch (Exception e) {
			ja.put("E:" + e.getMessage());
		}
		jo.put("2", ja);
		jo.put("3", System.getProperty("os.name"));
		jo.put("4", getAppDir().getAbsolutePath());
		jo.put("5", System.getProperty("java.class.path"));
		jo.put("6", System.getProperty("user.home"));
		jo.put("7", System.getProperty("user.name"));
		ja = new JSONArray();
		switch(getOs()) {
		case windows:
			ja.put("S:win");
			String[][] arr = new String[][]{{"CMD", "/C", "wmic PATH Win32_PhysicalMedia GET SerialNumber"},
					{"CMD", "/C", "WMIC DISKDRIVE GET Caption"},
					{"CMD", "/C", "WMIC OS GET SerialNumber,InstallDate"},};
			String[] m1 = avh(arr[1]);//avd("Win32_DiskDrive","Caption");
			String[] m2 = avh(arr[0]);//avd("Win32_PhysicalMedia","SerialNumber");
			for(int i = 0; i < m1.length && i < m2.length; ++i) {
				if(m1[i].toLowerCase().contains("usb") || m2[i].toLowerCase().contains("usb") || m1[i].toLowerCase().contains("error") || m2[i].toLowerCase().contains("error") || m1[i].toLowerCase().contains("wmic") || m2[i].toLowerCase().contains("wmic"))
					continue;
				ja2.put(m1[i] + ":" + m2[i]);
				ja.put(m1[i] + "-" + m2[i]);
			}
			m1 = avh(arr[2]);//avd("Win32_OperatingSystem","InstallDate");
			ja.put("W:" + m1[0]);
			ja2.put("W:" + m1[0]);
			break;
		case linux:
		case solaris:
			ja.put("S:lin");
			String[] cs = new String[]{"/sbin/hdparm -i /dev/sda", "/sbin/hdparm -i /dev/sdb", "/sbin/hdparm -i /dev/sdc"};
			boolean a = false;
			for(int i = 0; i < cs.length; ++i) {
				try {
					Process p = Runtime.getRuntime().exec(cs[i]);
					p.getOutputStream().close();
					String s = null;
					BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
					while((s = reader.readLine()) != null) {
						if(s.length() > 0) {
							if(s.toLowerCase().contains("model number")) {
								String[] sp = s.split(":");
								if(sp[1].toLowerCase().contains("usb"))
									continue;
								ja2.put(sp[1]);
								ja.put(sp[1]);
							} else if(s.toLowerCase().contains("serial number")) {
								String[] sp = s.split(":");
								if(sp[1].toLowerCase().contains("usb"))
									continue;
								ja2.put(sp[1]);
								ja.put(sp[1]);
							}
						}
					}
					reader.close();
				} catch (Exception e) {
				}
			}
			break;
		default:
			ja.put("S:unsupported");
			break;
		}
		jo.put("8", ja);
		jo.put("9", ja2);
		jo.put("10", "ruRU;ru_RU");
		jo.put("11", false);
		jo.put("12", GCUtil.dateFormat.format(new Date()));
		String s = "c75039af172fa31c48e1c67db7a0a2cd;" + jo.toString();
		byte[] b = s.getBytes("UTF-8");
		return b(b);
	}

	@SuppressWarnings({"unused", "rawtypes"})
	private static String[] avd(String arg1, String arg2) throws Throwable {
		List<String> s = new ArrayList<String>();
		Class.forName("com.jacob.com.ComThread").getDeclaredMethod("InitMTA").invoke(null);//ComThread.InitMTA();
		try {
			Object o = Class.forName("com.jacob.activeX.ActiveXComponent").getConstructor(arg1.getClass()).newInstance("winmgmts:\\\\.");
			Method m = o.getClass().getMethod("invoke", arg1.getClass(), arg1.getClass());
			Object instances = m.invoke(o, "InstancesOf", arg1); //wmi.invoke("InstancesOf", arg1);
			Object dispatch = instances.getClass().getMethod("getDispatch").invoke(instances);
			o = Class.forName("com.jacob.com.EnumVariant").getConstructor(dispatch.getClass()).newInstance(dispatch);
			//Enumeration<Variant> en = new EnumVariant(((Variant) instances).getDispatch());
			while(((Enumeration) o).hasMoreElements()) {
				Object o2 = ((Enumeration) o).nextElement();
				dispatch = o2.getClass().getMethod("getDispatch").invoke(o2);
				Object bb = Class.forName("com.jacob.activeX.ActiveXComponent").getConstructor(Class.forName("com.jacob.com.Dispatch")).newInstance(dispatch);//new ActiveXComponent(en.nextElement().getDispatch());
				m = bb.getClass().getMethod("getPropertyAsString", arg2.getClass());
				s.add(m.invoke(bb, arg2).toString().trim());
			}
		} finally {
			Class.forName("com.jacob.com.ComThread").getDeclaredMethod("Release").invoke(null);//ComThread.Release();
		}
		return s.toArray(new String[0]);
	}

	private static String[] avh(final String[] cmd) {
		final List<String> out = new ArrayList<String>();
		Thread t = new Thread() {
			@Override
			public void run() {
				try {
					Process process = Runtime.getRuntime().exec(cmd);
					String s = null;
					BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
					boolean f = false;
					while((s = reader.readLine()) != null) {
						if(!f) {
							f = true;
							continue;
						}
						if(!s.isEmpty())
							out.add(s.trim());
					}
					reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
					while((s = reader.readLine()) != null) {
						if(!s.isEmpty())
							out.add(s.trim());
					}
					reader.close();
					process.destroy();
				} catch(Throwable e) {
					out.add(e.getMessage());
				}
			}
		};
		t.start();
		try {
			t.join(2000L);
		} catch (InterruptedException e) {}
		if(t.isAlive()) {
			t.interrupt();
		}
		if(out.size() == 0)
			out.add("Interrupted");
		return out.toArray(new String[0]);
	}

	public void respawn(boolean flag, int i, boolean flag1) {
		if(!theWorld.multiplayerWorld && !theWorld.worldProvider.canRespawnHere()) {
			usePortal(0);
		}
		ChunkCoordinates chunkcoordinates = null;
		ChunkCoordinates chunkcoordinates1 = null;
		boolean flag2 = true;
		if(thePlayer != null && !flag) {
			chunkcoordinates = thePlayer.getPlayerSpawnCoordinate();
			if(chunkcoordinates != null) {
				chunkcoordinates1 = EntityPlayer.verifyRespawnCoordinates(theWorld, chunkcoordinates);
				if(chunkcoordinates1 == null)
					thePlayer.addChatMessage("tile.bed.notValid");
			}
		}
		if(chunkcoordinates1 == null) {
			chunkcoordinates1 = theWorld.getSpawnPoint();
			flag2 = false;
		}
		net.minecraft.src.IChunkProvider ichunkprovider = theWorld.getIChunkProvider();
		if(ichunkprovider instanceof ChunkProviderLoadOrGenerate) {
			ChunkProviderLoadOrGenerate chunkproviderloadorgenerate = (ChunkProviderLoadOrGenerate) ichunkprovider;
			chunkproviderloadorgenerate.setCurrentChunkOver(chunkcoordinates1.posX >> 4, chunkcoordinates1.posZ >> 4);
		}
		theWorld.setSpawnLocation();
		theWorld.updateEntityList();
		int j = 0;
		if(thePlayer != null) {
			j = thePlayer.entityId;
			theWorld.setEntityDead(thePlayer);
		}
		EntityPlayerSP entityplayersp = thePlayer;
		renderViewEntity = null;
		thePlayer = (EntityPlayerSP) playerController.createPlayer(theWorld);
		if(flag1)
			thePlayer.func_41014_d(entityplayersp);
		thePlayer.dimension = i;
		renderViewEntity = thePlayer;
		thePlayer.preparePlayerToSpawn();
		if(flag2) {
			thePlayer.setPlayerSpawnCoordinate(chunkcoordinates);
			thePlayer.setLocationAndAngles(chunkcoordinates1.posX + 0.5F, chunkcoordinates1.posY + 0.1F, chunkcoordinates1.posZ + 0.5F, 0.0F, 0.0F);
		}
		playerController.flipPlayer(thePlayer);
		theWorld.spawnPlayerWithLoadedChunks(thePlayer);
		thePlayer.movementInput = new MovementInputFromOptions(gameSettings);
		thePlayer.entityId = j;
		thePlayer.func_6420_o();
		playerController.func_6473_b(thePlayer);
		preloadWorld("Respawning");
		if(currentScreen instanceof GuiGameOver) {
			displayGuiScreen(null);
		}
	}

	public static void startMainThread1(String s, String s1) {
		startMainThread(s, s1, null);
	}

	public static void startMainThread(String s, String s1, String s2) {
		boolean flag = false;
		String s3 = s;
		Frame frame = new Frame("GreenCubes");
		
		ArrayList<BufferedImage> icons = new ArrayList<BufferedImage>();
		try {
			icons.add(ImageIO.read(Minecraft.class.getResource("/gc_images/gcico32x32.png")));
			icons.add(ImageIO.read(Minecraft.class.getResource("/gc_images/gcico48x48.png")));
			icons.add(ImageIO.read(Minecraft.class.getResource("/gc_images/gcico64x64.png")));
			icons.add(ImageIO.read(Minecraft.class.getResource("/gc_images/gcico128x128.png")));
			icons.add(ImageIO.read(Minecraft.class.getResource("/gc_images/gcico256x256.png")));
		} catch(Exception e) {
			e.printStackTrace();
		}
		if(getOS() == EnumOS2.macos)
			MacOSX.setIcons(icons);
		frame.setIconImages(icons);
		
		Canvas canvas = new Canvas();
		frame.setLayout(new BorderLayout());
		frame.add(canvas, "Center");
		canvas.setPreferredSize(new Dimension(1024, 640));
		frame.pack();
		frame.setLocationRelativeTo(null);
		MinecraftImpl minecraftimpl = new MinecraftImpl(frame, canvas, null, 1024, 640, flag, frame);
		Thread thread = new Thread(minecraftimpl, "Minecraft main thread");
		thread.setPriority(10);
		minecraftimpl.minecraftUri = "www.minecraft.net";
		if(s3 != null && s1 != null) {
			minecraftimpl.session = new Session(s3, s1);
		} else {
			minecraftimpl.session = new Session((new StringBuilder()).append("Player").append(System.currentTimeMillis() % 1000L).toString(), "");
		}
		if(s2 != null) {
			String as[] = s2.split(":");
			minecraftimpl.setServer(as[0], Integer.parseInt(as[1]));
		}
		frame.setVisible(true);
		frame.addWindowListener(new GameWindowListener(minecraftimpl, thread));
		thread.start();
	}

	private static EnumOS2 getOS() {
		// TODO Auto-generated method stub
		return null;
	}

	public NetClientHandler getSendQueue() {
		if(thePlayer instanceof EntityClientPlayerMP) {
			return ((EntityClientPlayerMP) thePlayer).sendQueue;
		} else {
			return null;
		}
	}
	
	public static void start(String[] args) {
		String player = new StringBuilder().append("Player").append(System.currentTimeMillis() % 1000L).toString();
		String session = "-";
		String directory = null;
		String server = null;
		for(int i = 0; i < args.length - 1; ++i) {
			if(args[i].equals("--directory")) {
				directory = args[++i];
			} else if(args[i].equals("--player")) {
				player = args[++i];
			} else if(args[i].equals("--session")) {
				session = args[++i];
			} else if(args[i].equals("--connect")) {
				server = args[++i];
			}
		}
		if(directory != null) {
			minecraftDir = new File(directory);
			if(!minecraftDir.exists())
				throw new RuntimeException("Directory " + minecraftDir + " is not exists!");
		}
		startMainThread(player, session, server);
	}

	public static void main(String args[]) {
		String s = null;
		String s1 = null;
		s = new StringBuilder().append("Player").append(System.currentTimeMillis() % 1000L).toString();
		if(args.length > 0) {
			s = args[0];
		}
		s1 = "-";
		if(args.length > 1) {
			s1 = args[1];
		}
		startMainThread1(s, s1);
	}

	public static boolean isGuiEnabled() {
		return theMinecraft == null || !theMinecraft.gameSettings.hideGUI;
	}

	public static boolean isFancyGraphicsEnabled() {
		return theMinecraft != null && theMinecraft.gameSettings.fancyGraphics;
	}

	public static boolean isAmbientOcclusionEnabled() {
		return theMinecraft != null && theMinecraft.gameSettings.ambientOcclusion;
	}

	public static boolean isDebugInfoEnabled() {
		return theMinecraft != null && theMinecraft.gameSettings.showDebugInfo;
	}

	public boolean lineIsCommand(String s) {
		if(!s.startsWith("/"))
			;
		return false;
	}

	public boolean isFullScreen() {
		return this.fullscreen;
	}

}
