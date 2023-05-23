package com.cocola.gpt.service;

import com.alibaba.fastjson.JSON;
import com.cocola.gpt.bo.GptRequestBo;
import com.cocola.gpt.config.OpenAiConfig;
import com.cocola.gpt.constant.PathConstant;
import com.cocola.gpt.controller.vo.GptRequestVo;
import com.cocola.gpt.controller.vo.GptResponseVo;
import com.cocola.gpt.utils.HttpClientUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

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
    private final OpenAiConfig openAiConfig;

    public GptResponseVo chatGpt(GptRequestVo requestVo) {
        GptRequestBo bo = new GptRequestBo();
        BeanUtils.copyProperties(requestVo, bo);

        Map<String, Object> header = new HashMap<>();
        header.put("Authorization", "Bearer " + openAiConfig.getToken());
        log.info("====================>url={}, header={}", openAiConfig.getUrl() + PathConstant.COMPLETIONS.CREATE_CHAT_COMPLETION, header.get("Authorization"));
        HttpClientUtil.HttpCommonResposne response =
                HttpClientUtil.postJsonForString(openAiConfig.getUrl() + PathConstant.COMPLETIONS.CREATE_CHAT_COMPLETION, JSON.toJSONString(bo), header);
        log.info("====================>response={}", response);
        return JSON.parseObject(response.getBody(), GptResponseVo.class);
    }
}
