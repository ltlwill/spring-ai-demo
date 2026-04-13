package org.will.demo.utils;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import java.util.ArrayList;
import java.util.List;

public class OpenCVTemplateV1Matcher {

    // 静态加载：自动导入dll，无需任何配置
    static {
        // 这行是关键！自动加载 opencv 库，100%不报错
        nu.pattern.OpenCV.loadLocally();
    }

    /**
     * 精准查找图标在背景图的 X 坐标
     */
    public static int findMatchX(String bgPath, String iconPath) {
        // 1. 读取图片
        Mat bg = Imgcodecs.imread(bgPath, Imgcodecs.IMREAD_COLOR);
        Mat icon = Imgcodecs.imread(iconPath, Imgcodecs.IMREAD_UNCHANGED); // 支持透明PNG

        Mat mask = new Mat();

        // 2. 处理透明通道（非常重要！你的图标是透明背景，必须用）
        if (icon.channels() == 4) {
            List<Mat> channels = new ArrayList<>();
            Core.split(icon, channels);
            mask = channels.get(3); // 透明通道作为掩码
            Imgproc.cvtColor(icon, icon, Imgproc.COLOR_BGRA2BGR);
        }

        // 3. 像素级精准匹配（最适合滑块验证码）
        Mat result = new Mat();
        Imgproc.matchTemplate(bg, icon, result, Imgproc.TM_SQDIFF_NORMED, mask);

        // 4. 获取最佳匹配位置
        Core.MinMaxLocResult mmr = Core.minMaxLoc(result);
        int bestX = (int) mmr.minLoc.x;

        // 释放资源
        bg.release();
        icon.release();
        result.release();
        if (!mask.empty()) mask.release();

        return bestX;
    }
}
