/**
 * @author TianLong Liu
 * @date 2026-04-13 14:01:54
 * @description
 */

package org.will.demo.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.will.demo.utils.*;

/**
 * @author TianLong Liu
 * @date 2026-04-13 14:01:54
 */
@Slf4j
@RestController
@RequestMapping("/image")
public class ImageMatchController {

    @GetMapping("/match")
    public int match(String bgPath, String iconPath) throws Exception{
//        String bgPath = "C:\\Users\\XH-AI-011\\Desktop\\work\\日常工作\\slider-poj\\ali-slider\\ali-slider\\img2\\bg.jpg";
//        String iconPath = "C:\\Users\\XH-AI-011\\Desktop\\work\\日常工作\\slider-poj\\ali-slider\\ali-slider\\img2\\icon.png";
//        int distance = ImageMatcherUtil.getSlideDistance(bgPath, iconPath);
//        int distance = LocalImageMatcherUtil.findIconLeftX(bgPath, iconPath);
        int distance = OpenCVTemplateV2Matcher.findMatchX(bgPath, iconPath);
//        int distance = SliderGapFinder.findGapX(bgPath);
        log.info("滑动距离：{}", distance);
        return distance;
    }

    @GetMapping("/match2")
    public int match2(String bgPath, String iconPath) throws Exception{
        int distance = ExactTemplateMatcher.match(bgPath, iconPath);
        log.info("滑动距离：{}", distance);
        return distance;
    }

    @GetMapping("/gap")
    public int gap(String bgPath, String iconPath) throws Exception{
        int distance = SliderGapFinderV1.findGapXByPath(bgPath);
        log.info("缺口距离：{}", distance);
        return distance;
    }

    @PostMapping("/gap2")
    public int gapBase64(@RequestBody String base64) throws Exception{
        int distance1 = SliderGapFinderV1.findGapXByBase64(base64);
        int distance2 = SliderGapFinderV2.findGapXByBase64(base64);
        log.info("缺口距离v1：{}", distance1);
        log.info("缺口距离v2：{}", distance2);
        return distance2;
    }

    @PostMapping("/gap3")
    public int gapBase64V3(@RequestBody String base64) throws Exception{
        int distance = UltimateSliderGapFinder.findGapXByBase64(base64);
        log.info("缺口距离：{}", distance);
        return distance;
    }

    @PostMapping("/gap4")
    public int gapBase64V4(@RequestBody String base64) throws Exception{
        int distance = UniversalGapFinder.findGapByBase64(base64);
        log.info("缺口距离：{}", distance);
        return distance;
    }

    @PostMapping("/gap5")
    public int gapBase64V5(@RequestBody String base64) throws Exception{
        int distance = SliderGapDetector.findGapX(base64);
        log.info("缺口距离：{}", distance);
        return distance;
    }

}
