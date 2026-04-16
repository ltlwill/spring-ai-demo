/**
 * @author TianLong Liu
 * @date 2026-03-26 14:06:15
 * @description
 */

package org.will.demo.web;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author TianLong Liu
 * @date 2026-03-26 14:06:15
 */
@RestController
@RequestMapping("/chat-client")
public class ChatClientController {

    @Resource
    private ChatClient chatClient;

    /**
     * 向用户消息添加元数据
     * @return
     */
    @RequestMapping("/chat1")
    public String chat1(String message){
        return chatClient.prompt()
                .user(u -> u.text(message)
                    .metadata("userId", "10001")
                    .metadata("userName", "Test")
                    .metadata("priority", "high"))
                .call()
                .content();
    }

    @RequestMapping("/chat2")
    public String chat2(String message){
        final Map<String,Object> metadata = Map.of("userId","10001","userName","Test","priority","high");
        return chatClient.prompt()
                .user(u -> u.text(message)
                        .metadata(metadata))
                .call()
                .content();
    }

    /**
     * 添加系统元数据，且带参数的默认系统文本
     * @param message
     * @return
     */
    @RequestMapping("/chat3")
    public String chat3(String message){
        final Map<String,Object> metadata = Map.of("userId","10001","userName","Test","priority","high");
        return chatClient.prompt()
                .system(s -> s.param("userName", "test"))
                .user(u -> u.text(message)
                        .metadata(metadata))
                .call()
                .content();
    }
}
