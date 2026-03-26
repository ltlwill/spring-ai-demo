/**
 * @author TianLong Liu
 * @date 2026-03-25 10:24:59
 * @description
 */

package org.will.demo.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.zhipuai.ZhiPuAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author TianLong Liu
 * @date 2026-03-25 10:24:59
 */
@Configuration
public class ChatClientConfig {

    @Bean
    public ChatClient zhipuAiChatClient(ZhiPuAiChatModel chatModel) {
        return ChatClient.create(chatModel);
    }

//    @Bean
//    public ChatClient openAiChatClient(OpenAiChatModel chatModel) {
//        return ChatClient.create(chatModel);
//    }
//
//    @Bean
//    public ChatClient anthropicChatClient(AnthropicChatModel chatModel) {
//        return ChatClient.create(chatModel);
//    }

    /**
     * 默认元数据
     * @param builder
     * @return
     */
//    public ChatClient chatClient(ChatClient.Builder builder){
//        return builder
//                .defaultSystem(s -> s.text("You are a helpful assistant.")
//                        .metadata("role", "assistant")
//                        .metadata("version", "1.0"))
//                .defaultUser(u -> u.text("Who are you?")
//                        .metadata("role", "user"))
//                .build();
//    }

    /**
     * 默认元数据，占位符的默认系统文本
     * @param builder
     * @return
     */
//    public ChatClient chatClient(ChatClient.Builder builder){
//        return builder
//                .defaultSystem(s -> s.text("You are a helpful assistant. your name is {userName}")
//                        .metadata("role", "assistant")
//                        .metadata("version", "1.0"))
//                .defaultUser(u -> u.text("Who are you?")
//                        .metadata("role", "user"))
//                .build();
//    }
}
