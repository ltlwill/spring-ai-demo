package org.will.demo.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

public class UniversalGapFinder {

    /**
     * 传入图片base64，自动识别缺口 X 坐标
     * @param base64 图片base64字符串（可带 data:image/xxx;base64, 前缀）
     * @return 缺口X坐标（完全自适应：左/中/右）
     */
    public static int findGapByBase64(String base64) throws IOException {
        // 1. 清理base64前缀
        if (base64.contains(",")) {
            base64 = base64.split(",")[1];
        }

        // 2. base64 → 图片
        byte[] imageBytes = Base64.getDecoder().decode(base64);
        BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageBytes));

        int w = img.getWidth();
        int h = img.getHeight();

        int[] darkScore = new int[w];
        int yTop = h / 3;
        int yBottom = h * 2 / 3;

        // 3. 计算每一列的暗色值（缺口阴影）
        for (int x = 20; x < w - 20; x++) {
            int darkCount = 0;
            for (int y = yTop; y < yBottom; y++) {
                int rgb = img.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;
                int gray = (r + g + b) / 3;

                if (gray < 100) {
                    darkCount++;
                }
            }
            darkScore[x] = darkCount;
        }

        // 4. 滑动窗口找最暗区域 = 缺口
        int maxSum = 0;
        int gapX = 80;
        int targetWidth = 40; // 缺口宽度

        for (int x = 20; x < w - targetWidth; x++) {
            int sum = 0;
            for (int i = 0; i < targetWidth; i++) {
                sum += darkScore[x + i];
            }
            if (sum > maxSum) {
                maxSum = sum;
                gapX = x;
            }
        }

        // 最终偏移
//        return gapX + 10;
        return gapX;
    }
}
