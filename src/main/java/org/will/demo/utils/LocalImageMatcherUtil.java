/**
 * @author TianLong Liu
 * @date 2026-04-13 14:09:50
 * @description
 */

package org.will.demo.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 *
 *  纯Java 像素级模板匹配
 *  不依赖OpenCV，不依赖dll，SpringBoot直接运行
 *
 * @author TianLong Liu
 * @date 2026-04-13 14:09:50
 */
public class LocalImageMatcherUtil {

    /**
     * 查找 icon 在 bg 中的最左侧 X 坐标
     * @param bgPath   背景图路径
     * @param iconPath 图标路径
     * @return 匹配区域的最小 X 坐标（你要的滑块距离）
     */
    public static int findIconLeftX(String bgPath, String iconPath) throws IOException {
        BufferedImage bg = ImageIO.read(new File(bgPath));
        BufferedImage icon = ImageIO.read(new File(iconPath));

        int bgW = bg.getWidth();
        int bgH = bg.getHeight();
        int w = icon.getWidth();
        int h = icon.getHeight();

        int bestX = -1;
        int bestY = -1;
        int minDiff = Integer.MAX_VALUE;

        // 全图遍历匹配
        for (int x = 0; x <= bgW - w; x++) {
            for (int y = 0; y <= bgH - h; y++) {
                int diff = compare(bg, x, y, icon, w, h);
                if (diff < minDiff) {
                    minDiff = diff;
                    bestX = x;
                    bestY = y;
                }
            }
        }

        return bestX;
    }

    /**
     * 像素级对比
     */
    private static int compare(BufferedImage bg, int x, int y, BufferedImage icon, int w, int h) {
        int diff = 0;
        for (int dx = 0; dx < w; dx++) {
            for (int dy = 0; dy < h; dy++) {
                int rgbBg = bg.getRGB(x + dx, y + dy);
                int rgbIcon = icon.getRGB(dx, dy);
                if (rgbBg != rgbIcon) {
                    diff++;
                }
            }
        }
        return diff;
    }
}
