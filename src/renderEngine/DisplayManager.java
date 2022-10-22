package renderEngine;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;

import toolbox.CursorChanger;
import toolbox.IconUtils;

public class DisplayManager {
	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	private static final int FPS_CAP = 120;
	
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
	}
	
	public static void updateDisplay() {
		Display.sync(FPS_CAP);
		Display.update();
	}
	
	public static void closeDisplay() {
		Display.destroy();
	}
	
}
