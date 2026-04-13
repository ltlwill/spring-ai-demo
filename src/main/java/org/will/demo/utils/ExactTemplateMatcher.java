package org.will.demo.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

public class ExactTemplateMatcher {

    // ------------------------------
    // 方法1：接收 本地文件路径（你要的重载）
    // ------------------------------
    public static int match(String bgImagePath, String iconImagePath) throws IOException {
        BufferedImage bg = ImageIO.read(new File(bgImagePath));
        BufferedImage icon = ImageIO.read(new File(iconImagePath));
        return matchInternal(bg, icon);
    }

    // ------------------------------
    // 方法2：接收 Base64（原来的保留）
    // ------------------------------
    public static int matchBase64(String bgBase64, String iconBase64) throws IOException {
        BufferedImage bg = base64ToImage(bgBase64);
        BufferedImage icon = base64ToImage(iconBase64);
        return matchInternal(bg, icon);
    }

    // ------------------------------
    // 核心匹配逻辑（统一调用）
    // ------------------------------
    private static int matchInternal(BufferedImage bg, BufferedImage icon) {
        int bgW = bg.getWidth();
        int bgH = bg.getHeight();
        int w = icon.getWidth();
        int h = icon.getHeight();

        long bestDiff = Long.MAX_VALUE;
        int bestX = 0;

        // 遍历全图找最相似的位置
        for (int x = 0; x <= bgW - w; x++) {
            for (int y = 0; y <= bgH - h; y++) {

                long diff = calculateDiff(bg, icon, x, y, w, h);

                if (diff < bestDiff) {
                    bestDiff = diff;
                    bestX = x;
                }
            }
        }
        return bestX;
    }

    // ------------------------------
    // 像素对比：透明像素自动跳过
    // ------------------------------
    private static long calculateDiff(
            BufferedImage bg,
            BufferedImage icon,
            int x, int y,
            int w, int h
    ) {
        long diff = 0;
        for (int dx = 0; dx < w; dx++) {
            for (int dy = 0; dy < h; dy++) {
                int rgbBg = bg.getRGB(x + dx, y + dy);
                int rgbIcon = icon.getRGB(dx, dy);

                // 透明部分不参与匹配（关键！）
                int alpha = (rgbIcon >> 24) & 0xFF;
                if (alpha < 128) continue;

                int r1 = (rgbBg >> 16) & 0xFF;
                int g1 = (rgbBg >> 8) & 0xFF;
                int b1 = rgbBg & 0xFF;

                int r2 = (rgbIcon >> 16) & 0xFF;
                int g2 = (rgbIcon >> 8) & 0xFF;
                int b2 = rgbIcon & 0xFF;

                long dr = r1 - r2;
                long dg = g1 - g2;
                long db = b1 - b2;
                diff += dr * dr + dg * dg + db * db;
            }
        }
        return diff;
    }

    // ------------------------------
    // Base64 转图片
    // ------------------------------
    private static BufferedImage base64ToImage(String base64) throws IOException {
        if (base64.contains(",")) {
            base64 = base64.split(",")[1];
        }
        byte[] bytes = Base64.getDecoder().decode(base64);
        return ImageIO.read(new ByteArrayInputStream(bytes));
    }
}
