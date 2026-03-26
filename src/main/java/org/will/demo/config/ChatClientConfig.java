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
}
