/**
 * @author TianLong Liu
 * @date 2026-04-13 20:57:33
 * @description
 */

package org.will.demo;

import org.junit.jupiter.api.Test;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @author TianLong Liu
 * @date 2026-04-13 20:57:33
 */
public class Test1 {

    private static final String bgPath = "D:\\work1\\img3\\bg.jpg";
    private static final String iconPath = "D:\\work1\\img3\\icon.png";

    @Test
    public void test1(){
        // 加载 OpenCV 本地库（必须执行）
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // 1. 读取背景图（320x180）
        Mat bg = Imgcodecs.imread(bgPath, Imgcodecs.IMREAD_COLOR);
        // 2. 读取模板图（带透明通道，50x50）
        Mat tpl = Imgcodecs.imread(iconPath, Imgcodecs.IMREAD_UNCHANGED);

        List<Mat> tplChannels = new ArrayList<>();
        Core.split(tpl, tplChannels);

        Mat tplAlpha = tplChannels.get(3); // 透明通道
        Mat tplBgr = new Mat();
        List<Mat> bgrChannels = new ArrayList<>();
        bgrChannels.add(tplChannels.get(0));
        bgrChannels.add(tplChannels.get(1));
        bgrChannels.add(tplChannels.get(2));
        Core.merge(bgrChannels, tplBgr);

        // 转灰度图
        Mat bgGray = new Mat();
        Imgproc.cvtColor(bg, bgGray, Imgproc.COLOR_BGR2GRAY);

        Mat tplGray = new Mat();
        Imgproc.cvtColor(tplBgr, tplGray, Imgproc.COLOR_BGR2GRAY);

        // 模板匹配（带透明掩码，不会再匹配到错误位置）
        Mat result = new Mat();
        Imgproc.matchTemplate(bgGray, tplGray, result, Imgproc.TM_CCOEFF_NORMED, tplAlpha);

        // 获取最佳位置
        Core.MinMaxLocResult mmr = Core.minMaxLoc(result);
        Point bestLoc = mmr.maxLoc;

        // 输出正确的左坐标（231左右）
        System.out.println((int) bestLoc.x);
    }

    @Test
    public void test2() throws IOException {
        BufferedImage background = ImageIO.read(new File(bgPath));
        BufferedImage icon = ImageIO.read(new File(iconPath));

        int bestX = findBestX(background, icon);
//        AtomicStampedReference<Integer> stampedRef = new AtomicStampedReference<>(0, 0);
        System.out.println(bestX); // 输出正确左坐标
    }

    public static int findBestX(BufferedImage bg, BufferedImage icon) {
        int bgW = bg.getWidth();
        int bgH = bg.getHeight();
        int iconW = icon.getWidth();
        int iconH = icon.getHeight();

        int bestX = 0;
        int bestDiff = Integer.MAX_VALUE;

        for (int x = 0; x <= bgW - iconW; x++) {
            int diff = calculateDifference(bg, icon, x, 0);

            if (diff < bestDiff) {
                bestDiff = diff;
                bestX = x;
            }
        }
        return bestX;
    }

    private static int calculateDifference(BufferedImage bg, BufferedImage icon, int x, int y) {
        int total = 0;
        int w = icon.getWidth();
        int h = icon.getHeight();

        for (int dx = 0; dx < w; dx++) {
            for (int dy = 0; dy < h; dy++) {
                int bgPixel = bg.getRGB(x + dx, dy);
                int iconPixel = icon.getRGB(dx, dy);

                // 跳过透明像素（关键！！！）
                if ((iconPixel >> 24) == 0x00) {
                    continue;
                }

                total += Math.abs((bgPixel & 0xFF) - (iconPixel & 0xFF));
                total += Math.abs(((bgPixel >> 8) & 0xFF) - ((iconPixel >> 8) & 0xFF));
                total += Math.abs(((bgPixel >> 16) & 0xFF) - ((iconPixel >> 16) & 0xFF));
            }
        }
        return total;
    }
}
