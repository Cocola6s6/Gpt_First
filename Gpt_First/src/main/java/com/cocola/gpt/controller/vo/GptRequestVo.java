package com.cocola.gpt.controller.vo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @Author: yangshiyuan
 * @Date: 2022/9/5
 * @Description:
 */
@Data
@Accessors(chain = true)
public class GptRequestVo {
    String model;

    List<ChatMessage> messages;

    Double temperature;

    Integer n;

    Boolean stream;

    List<String> stop;

    Integer max_tokens;

    String user;
}
