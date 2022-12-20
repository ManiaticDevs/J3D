package org.J3D.renderEngine;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.*;
import org.J3D.toolbox.*;

public class DisplayManager  {
	private static int WIDTH = 1280;
	private static int HEIGHT = 720;
	private static int FPS_CAP = 120;
	private static int balls;
	
	private static long lastFrameTime;
	private static float delta;
	static long startTime = System.nanoTime();
    static int frames = 0;
    
	public static void createDisplay() throws Exception, LWJGLException {
		ContextAttribs attribs = new ContextAttribs(3,2);
		attribs.withForwardCompatible(true).withProfileCore(true);
		final ByteBuffer[] windowsFavicon = IconUtils.getFavicon();
		try {
			Display.create(new PixelFormat(), attribs);
			//Display.setFullscreen(false);
			Display.setIcon(windowsFavicon);
			Display.setResizable(true);
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.setVSyncEnabled(false);
			Display.makeCurrent();
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
	
	public static void saveScreenshot() throws Exception {
	    System.out.println("Saving screenshot!");
	    Rectangle screenRect = new Rectangle(Display.getX(), Display.getY(), Display.getWidth(), Display.getHeight());
	    BufferedImage capture = new Robot().createScreenCapture(screenRect);
	    ImageIO.write(capture, "png", new File("screenshot.png"));
	}
	
	public static void updateDisplay() throws LWJGLException {
		if (Display.wasResized() || Display.isFullscreen() || !Display.isFullscreen()) { 
			GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
		}
		Display.sync(FPS_CAP);
		Display.update();
		if(Keyboard.isKeyDown(Keyboard.KEY_F11)) {
			if(Display.isFullscreen()) {
				Display.setDisplayMode(new DisplayMode(1280, 720));
				Display.setFullscreen(false);
			} else {
				Display.setDisplayModeAndFullscreen(Display.getDesktopDisplayMode());
			}
			
		}
		
		long currentFrameTime = getCurrentTime();
		delta = (currentFrameTime - lastFrameTime)/1000f;
		lastFrameTime = currentFrameTime;
	}
	
	
    public static int logFrame() {
    	
        frames++;
        if(System.nanoTime() - startTime >= 1000000000) {
        	if(frames != 0) {
        		balls = frames;
        	}
            
            frames = 0;
            startTime = System.nanoTime();
        }
        return balls;
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
