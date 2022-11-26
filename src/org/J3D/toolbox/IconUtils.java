package org.J3D.toolbox;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;

public final class IconUtils {
    public static ByteBuffer[] getFavicon() {
        try {
            return new ByteBuffer[] {readImageToBuffer("iconx16"), readImageToBuffer("iconx32")};
        }catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static ByteBuffer readImageToBuffer(String fileName) throws IOException {
        final BufferedImage bufferedImage = ImageIO.read(new FileInputStream("res/"+fileName+".png"));
        if(bufferedImage == null)
            return null;
        final int[] rgb = bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null, 0, bufferedImage.getWidth());
        final ByteBuffer byteBuffer = ByteBuffer.allocate(4 * rgb.length);
        for(int i : rgb)
            byteBuffer.putInt(i << 8 | i >> 24 & 255);
        byteBuffer.flip();
        return byteBuffer;
    }
}