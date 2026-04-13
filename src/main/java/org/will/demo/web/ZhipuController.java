/**
 * @author TianLong Liu
 * @date 2026-03-25 09:47:16
 * @description
 */

package org.will.demo.web;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.zhipuai.ZhiPuAiChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @author TianLong Liu
 * @date 2026-03-25 09:47:16
 */
@RestController
@RequestMapping("/zhipu")
public class ZhipuController {

    @Resource
    private ZhiPuAiChatModel zhiPuAiChatModel;

    private final ChatClient chatClient;

//    public ZhipuController(ChatClient.Builder builder) {
//        this.chatClient = builder.build();
//    }

    public ZhipuController(ZhiPuAiChatModel zhiPuAiChatModel) {
        this.chatClient = ChatClient.create(zhiPuAiChatModel);
    }

    @GetMapping(value = "/chat")
    public String zhipuChat(String input){
        return chatClient.prompt().user(input).call().content();
    }

    @GetMapping(value = "/chat2")
    public String zhipuChat2(String input){
        return zhiPuAiChatModel.call(input);
    }

    @GetMapping(value = "/chat3")
    public String zhipuChat3(String input){
        return zhiPuAiChatModel.call(Prompt.builder().content(input).build()).getResult().toString();
    }

    @GetMapping(value = "/stream/chat")
    public Flux<String> zhipuStreamChat(String input){
        return zhiPuAiChatModel.stream(input);
    }
}
