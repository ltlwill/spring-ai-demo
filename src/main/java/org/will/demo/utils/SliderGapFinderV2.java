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
public class SliderGapFinderV2 {

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
        if (image == null) return -1;

        int width = image.getWidth();
        int height = image.getHeight();
        int startX = 1; // 跳过左边安全区

        int[] columnDiff = new int[width];

        // 🔥 关键修复：按列扫描，只对比“非透明区域”的上下渐变
        for (int x = startX; x < width; x++) {
            int edgeStrength = 0;

            for (int y = 10; y < height - 10; y++) {
                int rgb = image.getRGB(x, y);
                int alpha = (rgb >> 24) & 0xFF; // 获取透明度 (Java 17 必加)

                // 跳过透明/半透明像素（排除背景干扰）
                if (alpha < 50) continue;

                // 对比下一行
                int rgbNext = image.getRGB(x, y + 1);
                int alphaNext = (rgbNext >> 24) & 0xFF;
                if (alphaNext < 50) continue;

                // 提取 RGB 分量（忽略 Alpha，只对比颜色）
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;

                int rNext = (rgbNext >> 16) & 0xFF;
                int gNext = (rgbNext >> 8) & 0xFF;
                int bNext = (rgbNext & 0xFF);

                // 计算 RGB 差值（缺口边缘变化最大）
                int diff = Math.abs(r - rNext) + Math.abs(g - gNext) + Math.abs(b - bNext);
                edgeStrength += diff;
            }
            columnDiff[x] = edgeStrength;
        }

        // 🔥 找突变最大的位置（缺口左边缘）
        int maxDiff = 0;
        int gapX = startX;
        for (int x = startX; x < width; x++) {
            if (columnDiff[x] > maxDiff) {
                maxDiff = columnDiff[x];
                gapX = x;
            }
        }

        // 🔧 最终微调：根据你的场景，偏移 +5~+15 即可（这里按中间位置校准）
//        return gapX + 8;
        return gapX;
    }
}
