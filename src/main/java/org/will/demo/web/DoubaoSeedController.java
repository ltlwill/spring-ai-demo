/**
 * @author TianLong Liu
 * @date 2026-04-14 10:15:42
 * @description
 */

package org.will.demo.web;

import com.alibaba.fastjson2.JSON;
import com.volcengine.ark.runtime.model.responses.constant.ResponsesConstants;
import com.volcengine.ark.runtime.model.responses.content.InputContentItemImage;
import com.volcengine.ark.runtime.model.responses.content.InputContentItemText;
import com.volcengine.ark.runtime.model.responses.item.ItemEasyMessage;
import com.volcengine.ark.runtime.model.responses.item.MessageContent;
import com.volcengine.ark.runtime.model.responses.request.CreateResponsesRequest;
import com.volcengine.ark.runtime.model.responses.request.ResponsesInput;
import com.volcengine.ark.runtime.model.responses.response.ResponseObject;
import com.volcengine.ark.runtime.service.ArkService;
import org.springframework.web.bind.annotation.*;

/**
 * @author TianLong Liu
 * @date 2026-04-14 10:15:42
 */
@RestController
@RequestMapping("/doubao")
public class DoubaoSeedController {

    private static final String BASE_URL = "https://ark.cn-beijing.volces.com/api/v3";
    private static final String MODEL_NAME = "doubao-seed-2-0-lite-260215";

    @GetMapping("/chat")
    public String chat(@RequestHeader String apiKey, @RequestParam String bgUrl, @RequestParam String iconUrl, @RequestParam String prompt) {
//        String apiKey = System.getenv("ARK_API_KEY");
        ArkService arkService = ArkService.builder().apiKey(apiKey).baseUrl(BASE_URL).build();
        CreateResponsesRequest request = CreateResponsesRequest.builder()
                .model(MODEL_NAME)
                .input(ResponsesInput.builder().addListItem(
                        ItemEasyMessage.builder().role(ResponsesConstants.MESSAGE_ROLE_USER).content(
                                MessageContent.builder()
                                    .addListItem(InputContentItemImage.builder()
                                        .imageUrl("https://ark-project.tos-cn-beijing.volces.com/doc_image/ark_demo_img_1.png").build())
//                                  .addListItem(InputContentItemText.builder().text("支持输入图片的模型系列是哪个？").build())
                                    .addListItem(InputContentItemText.builder().text(prompt).build()).build())
                            .build())
                        .build())
                .build();
        ResponseObject resp = arkService.createResponse(request);
        System.out.println(resp);

        arkService.shutdownExecutor();
        return JSON.toJSONString(resp.getOutput());
    }
}
