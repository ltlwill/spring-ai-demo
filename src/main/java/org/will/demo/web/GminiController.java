/**
 * @author TianLong Liu
 * @date 2026-05-11 20:10:03
 * @description
 */

package org.will.demo.web;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author TianLong Liu
 * @date 2026-05-11 20:10:03
 */
@RestController
@RequestMapping("/gmini")
public class GminiController {

    private static final String MODEL_NAME = "gemini-3-flash-preview";

    @RequestMapping("/chat")
    public String chat(String message,String apiKey){
        Client client = Client.builder().apiKey(apiKey).build();
        GenerateContentConfig config = GenerateContentConfig.builder()
//                .temperature(0.7f)
//                .maxOutputTokens(1024)
                .build();
        GenerateContentResponse response =
                client.models.generateContent(
                        MODEL_NAME,
                        message,
                        config);
        return response.text();
    }
}
