package org.will.demo.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

public class SliderGapDetector {

    /**
     * 支持任意形状缺口：三角形、正方形、长方形、动物、不规则图形
     * 入参：图片base64
     * 返回：缺口左边缘 X 坐标
     */
    public static int findGapX(String base64) throws IOException {
        // 1. 解析 base64
        if (base64 == null) throw new IOException("base64 为空");
        if (base64.contains(",")) {
            base64 = base64.split(",")[1];
        }

        byte[] bytes = Base64.getDecoder().decode(base64);
        BufferedImage img = ImageIO.read(new ByteArrayInputStream(bytes));
        int w = img.getWidth();
        int h = img.getHeight();

        // 只检测图片中间区域，避开上下边框干扰
        int yFrom = h / 4;
        int yTo   = h * 3 / 4;

        // 每一列的“梯度突变强度”
        int[] gradient = new int[w];

        for (int x = 20; x < w - 20; x++) {
            int total = 0;
            for (int y = yFrom; y < yTo - 1; y++) {
                // 当前像素
                int rgb1 = img.getRGB(x, y);
                int r1 = (rgb1 >> 16) & 0xFF;
                int g1 = (rgb1 >> 8) & 0xFF;
                int b1 = rgb1 & 0xFF;

                // 下一个像素
                int rgb2 = img.getRGB(x, y + 1);
                int r2 = (rgb2 >> 16) & 0xFF;
                int g2 = (rgb2 >> 8) & 0xFF;
                int b2 = rgb2 & 0xFF;

                // 颜色差值越大，边缘越强
                int dr = Math.abs(r1 - r2);
                int dg = Math.abs(g1 - g2);
                int db = Math.abs(b1 - b2);

                total += dr + dg + db;
            }
            gradient[x] = total;
        }

        // ==============================
        // 关键：找【局部最大突变】
        // 缺口无论什么形状，边缘一定是局部最陡的地方
        // ==============================
        int maxGrad = 0;
        int gapX = 80;

        for (int x = 30; x < w - 30; x++) {
            // 取局部区域平均值，过滤背景噪点纹理
            int local = (gradient[x-2] + gradient[x-1] + gradient[x] + gradient[x+1] + gradient[x+2]) / 5;

            if (local > maxGrad) {
                maxGrad = local;
                gapX = x;
            }
        }

        // 轻微校准，对齐缺口左边缘
        return gapX + 6;
    }
}
