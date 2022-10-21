package renderEngine;

import java.awt.image.*;
import java.io.*;
import java.nio.*;
import javax.imageio.*;
import org.lwjgl.*;
import org.lwjgl.input.*;

public class CursorChanger {
	
	public CursorChanger() {
		
	}
	
	public void loadCursor(BufferedImage img) throws LWJGLException
	{
	    final int w = img.getWidth();
	    final int h = img.getHeight();

	    int rgbData[] = new int[w * h];

	    for (int i = 0; i < rgbData.length; i++)
	    {
	        int x = i % w;
	        int y = h - 1 - i / w; // this will also flip the image vertically

	        rgbData[i] = img.getRGB(x, y);
	    }

	    IntBuffer buffer = BufferUtils.createIntBuffer(w * h);
	    buffer.put(rgbData);
	    buffer.rewind();

	    Cursor cursor = new Cursor(w, h, 2, h - 2, 1, buffer, null);

	    Mouse.setNativeCursor(cursor);
	}
	
	public BufferedImage load(String imageName) {
		BufferedImage image = null;

		try {
			image = ImageIO.read(new FileInputStream("res/" + imageName + ".png"));

		} catch (IOException e) {
			e.printStackTrace();
		}

		return image;
	}
	
}
