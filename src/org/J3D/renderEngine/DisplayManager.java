package org.J3D.renderEngine;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import org.lwjgl.*;
import org.lwjgl.opengl.*;

import org.J3D.toolbox.*;

public class DisplayManager {
	private static int WIDTH = 1280;
	private static int HEIGHT = 720;
	private static int FPS_CAP = 120;
	
	private static long lastFrameTime;
	private static float delta;
	static long startTime = System.nanoTime();
    static int frames = 0;
    
	public static void createDisplay() throws LWJGLException {
		ContextAttribs attribs = new ContextAttribs(3,2);
		attribs.withForwardCompatible(true).withProfileCore(true);
		final ByteBuffer[] windowsFavicon = IconUtils.getFavicon();
		try {
			Display.setIcon(windowsFavicon);
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.create(new PixelFormat(), attribs);
			Display.setTitle("J3D WINDOW");
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
		
		//CURSOR (PLACE AFTER CREATION)
		CursorChanger curChange = new CursorChanger();
		BufferedImage img = curChange.load("/cursor/default");
		curChange.loadCursor(img);
		
		lastFrameTime = getCurrentTime();
	}
	
	public static void updateDisplay() {
		Display.sync(FPS_CAP);
		Display.update();
		long currentFrameTime = getCurrentTime();
		delta = (currentFrameTime - lastFrameTime)/1000f;
		lastFrameTime = currentFrameTime;
		
	}
	
	
    public static int logFrame() {
        frames++;
        if(System.nanoTime() - startTime >= 1000000000) {
            System.out.println("fps: " + frames);
            frames = 0;
            startTime = System.nanoTime();
        }
        
        return frames;
    }
	
	public static float getFrameTimeSeconds() {
		return delta;
	}
	
	public static void closeDisplay() {
		Display.destroy();
	}
	
	private static long getCurrentTime() {
		return Sys.getTime()*1000/Sys.getTimerResolution();
	}
	
}
