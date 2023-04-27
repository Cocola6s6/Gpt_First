package com.cocola.gpt.service;


import com.alibaba.fastjson.JSON;
import com.cocola.gpt.constant.PathConstant;
import com.cocola.gpt.controller.vo.ChatMessage;
import com.cocola.gpt.controller.vo.GptRequestVo;
import com.cocola.gpt.controller.vo.GptResponseVo;
import com.cocola.gpt.utils.HttpClientUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: yangshiyuan
 * @Date: 2023/3/14
 * @Description:
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GptService {

    @Value("${open.ai.ur:https://api.openai.com}")
    private String url;

    @Value("${open.ai.token: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}")
    private String token;

    public List<GptResponseVo> chatGpt(GptRequestVo requestVo) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setRole("cocola").setContent("test");
        requestVo.setMessages(Arrays.asList(chatMessage)).setMax_tokens(500).setTemperature(1.0);
        Map<String, Object> header = new HashMap<>();
        header.put("Authorization", "Bearer " + token);
        log.info("====================>url={}, header={}", url + PathConstant.COMPLETIONS.CREATE_CHAT_COMPLETION, header.get("Authorization"));
        HttpClientUtil.HttpCommonResposne response =
                HttpClientUtil.postJsonForString(url + PathConstant.COMPLETIONS.CREATE_CHAT_COMPLETION, JSON.toJSONString(requestVo), header);
        log.info("====================>response={}", response);
        return null;
    }

}
