package org.will.demo.utils;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import java.util.ArrayList;
import java.util.List;

public class OpenCVTemplateV2Matcher {

    static {
        try {
            nu.pattern.OpenCV.loadLocally();
        } catch (Exception e) {
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        }
    }

    public static int findMatchX(String bgPath, String iconPath) {

        // ====================== 【关键修复：防止图片读取失败】 ======================
        Mat bg = Imgcodecs.imread(bgPath);
        Mat icon = Imgcodecs.imread(iconPath, Imgcodecs.IMREAD_UNCHANGED);

        // 如果图片读取失败，直接抛出异常
        if (bg.empty()) throw new RuntimeException("背景图加载失败：" + bgPath);
        if (icon.empty()) throw new RuntimeException("图标加载失败：" + iconPath);

        // ====================== 【关键修复：自动处理透明通道】 ======================
        Mat mask = new Mat();
        if (icon.channels() == 4) {
            List<Mat> channels = new ArrayList<>();
            Core.split(icon, channels);
            mask = channels.get(3); // Alpha透明通道作为掩码
            Imgproc.cvtColor(icon, icon, Imgproc.COLOR_BGRA2BGR);
        }

        // ====================== 执行匹配 ======================
        Mat result = new Mat();
        Imgproc.matchTemplate(bg, icon, result, Imgproc.TM_SQDIFF_NORMED, mask);

        Core.MinMaxLocResult mmr = Core.minMaxLoc(result);
        int x = (int) mmr.minLoc.x;

        // 释放资源
        bg.release();
        icon.release();
        result.release();
        if (!mask.empty()) mask.release();

        return x;
    }
}
