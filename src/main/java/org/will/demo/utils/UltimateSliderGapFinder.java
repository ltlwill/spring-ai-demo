package org.will.demo.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

/**
 * Java 17 专用 · 滑块缺口精准识别终极版
 * 解决：复杂背景误判、结果偏右、超出图片宽度等所有问题
 */
public class UltimateSliderGapFinder {


    public static int findGapXByBase64(String base64) throws IOException {
        try(ByteArrayInputStream bis = new ByteArrayInputStream(Base64.getDecoder().decode(base64))){
            BufferedImage image = ImageIO.read(bis);
            return findGapXByBufferedImage(image);
        }
    }

    public static int findGapXByPath(String path) throws IOException {
        return findGapXByBufferedImage(ImageIO.read(new File(path)));
    }

    /**
     * 核心方法：精准识别缺口左边缘 X 坐标
     * @param img 背景图绝对路径
     * @return 缺口左边缘 X 坐标（滑块需要滑动的距离）
     */
    public static int findGapXByBufferedImage(BufferedImage img) throws IOException {
        int w = img.getWidth();
        int h = img.getHeight();

        // ==============================================
        // 🔥 第一步：限定滑块搜索区域（只搜下半部分，排除上半部分背景干扰）
        // 滑块永远在图片下半部分，上半部分全是树叶，直接跳过！
        // ==============================================
        int searchYStart = (int) (h * 0.5);  // 从图片高度的 50% 开始搜
        int searchYEnd = (int) (h * 0.9);    // 搜到 90% 结束，排除底部边框
        int searchXStart = 20;              // 从 X=20 开始，跳过左边框
        int searchXEnd = (int) (w * 0.7);    // 最多搜到 70% 宽度，绝对不会跑到最右侧

        // ==============================================
        // 🔥 第二步：二值化增强边缘，放大缺口和背景的差异
        // ==============================================
        int[] edgeScore = new int[w];
        int threshold = 30; // 颜色差值阈值，只统计明显的边缘

        for (int x = searchXStart; x < searchXEnd; x++) {
            int score = 0;
            for (int y = searchYStart; y < searchYEnd - 1; y++) {
                // 直接提取 RGB 分量（Java 17 兼容，无需 Color 类）
                int rgb1 = img.getRGB(x, y);
                int rgb2 = img.getRGB(x, y + 1);

                int r1 = (rgb1 >> 16) & 0xFF;
                int g1 = (rgb1 >> 8) & 0xFF;
                int b1 = rgb1 & 0xFF;

                int r2 = (rgb2 >> 16) & 0xFF;
                int g2 = (rgb2 >> 8) & 0xFF;
                int b2 = rgb2 & 0xFF;

                // 计算颜色差值
                int diff = Math.abs(r1 - r2) + Math.abs(g1 - g2) + Math.abs(b1 - b2);

                // 只统计超过阈值的强边缘，过滤背景的弱纹理
                if (diff > threshold) {
                    score += diff;
                }
            }
            edgeScore[x] = score;
        }

        // ==============================================
        // 第三步：找边缘得分最高的列（缺口左边缘）
        // ==============================================
        int maxScore = 0;
        int gapX = searchXStart;
        for (int x = searchXStart; x < searchXEnd; x++) {
            if (edgeScore[x] > maxScore) {
                maxScore = edgeScore[x];
                gapX = x;
            }
        }

        // ==============================================
        // 第四步：最终微调（根据你的验证码校准，+5~+10 即可）
        // ==============================================
        int finalGapX = gapX + 8;

        // 安全校验：绝对不允许超出图片宽度
        return Math.min(finalGapX, w - 10);
    }
}
