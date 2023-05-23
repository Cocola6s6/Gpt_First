package com.cocola.gpt.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: yangshiyuan
 * @Date: 2022/9/2
 * @Description:
 */
@Data
@Configuration
public class OpenAiConfig {
    @Value("${openai.url}")
    private String url;

    @Value("${openai.token}")
    private String token;
}
