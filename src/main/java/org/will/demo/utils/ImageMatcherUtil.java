package org.will.demo.utils;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.List;

public class ImageMatcherUtil {

    // 加载 OpenCV 库
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    /**
     * 精准计算图标在背景图中的 X 轴偏移（最左边距离）
     * @param bgPath 背景图路径
     * @param iconPath 图标路径
     * @return 滑动距离（像素）
     */
    public static int getSlideDistance(String bgPath, String iconPath) {
        // 1. 读取图片
        Mat bg = Imgcodecs.imread(bgPath, Imgcodecs.IMREAD_COLOR);
        Mat icon = Imgcodecs.imread(iconPath, Imgcodecs.IMREAD_UNCHANGED);

        // 2. 处理透明通道（PNG）
        Mat mask = new Mat();
        if (icon.channels() == 4) {
            List<Mat> channels = new java.util.ArrayList<>();
            Core.split(icon, channels);
            mask = channels.get(3); // Alpha 通道作为掩码
            Imgproc.cvtColor(icon, icon, Imgproc.COLOR_BGRA2BGR);
        }

        // 3. 执行像素级模板匹配
        Mat result = new Mat();
        int method = Imgproc.TM_SQDIFF_NORMED;
        Imgproc.matchTemplate(bg, icon, result, method, mask);

        // 4. 获取最佳匹配坐标
        Core.MinMaxLocResult mmr = Core.minMaxLoc(result);
        Point topLeft = mmr.minLoc;

        // 5. 释放资源
        bg.release();
        icon.release();
        result.release();
        if (!mask.empty()) mask.release();

        return (int) topLeft.x; // X 轴偏移 = 滑动距离
    }
}
