package org.will.demo.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

/**
 * Java 17 兼容
 * 纯JDK实现，无需OpenCV，无需DLL
 * 功能：自动识别滑块验证码缺口 X 坐标
 */
public class SliderGapFinderV1 {

    public static int findGapXByBase64(String base64) throws IOException{
        try(ByteArrayInputStream bis = new ByteArrayInputStream(Base64.getDecoder().decode(base64))){
            BufferedImage image = ImageIO.read(bis);
            return findGapByBufferedImage(image);
        }
    }

    public static int findGapXByPath(String imagePath) throws IOException{
        return findGapByBufferedImage(ImageIO.read(new File(imagePath)));
    }

    /**
     * 自动找背景图中的缺口左边缘 X 坐标
     */
    public static int findGapByBufferedImage(BufferedImage image){
        int w = image.getWidth();
        int h = image.getHeight();

        int[] edgeScore = new int[w];
        int startX = 60; // 左边安全区不扫描

        // 按列扫描：找颜色突变最剧烈的位置 = 缺口边缘
        for (int x = startX; x < w; x++) {
            int sum = 0;
            for (int y = 5; y < h - 5; y++) {
                Color c1 = new Color(image.getRGB(x, y));
                Color c2 = new Color(image.getRGB(x, y + 1));

                int dr = Math.abs(c1.getRed() - c2.getRed());
                int dg = Math.abs(c1.getGreen() - c2.getGreen());
                int db = Math.abs(c1.getBlue() - c2.getBlue());

                sum += dr + dg + db;
            }
            edgeScore[x] = sum;
        }

        // 找突变最大的X
        int max = 0;
        int gapX = startX;
        for (int x = startX; x < w; x++) {
            if (edgeScore[x] > max) {
                max = edgeScore[x];
                gapX = x;
            }
        }

        // 微调（根据你的验证码调整 +5 ~ +15 之间）
        return gapX;
//        return gapX + 10;
//        return gapX + (50 / 2);
    }
}
