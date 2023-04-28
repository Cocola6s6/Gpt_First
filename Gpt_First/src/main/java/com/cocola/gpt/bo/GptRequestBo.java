package com.cocola.gpt.bo;

import com.cocola.gpt.controller.vo.ChatMessage;
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
public class GptRequestBo {
    String model;

    List<ChatMessage> messages;

    Double temperature;

    Integer n;

    Boolean stream;

    List<String> stop;

    Integer max_tokens;

    String user;

    public GptRequestBo() {
        model = "gpt-3.5-turbo-0301";
        temperature = 0.7;
        user = "cocola";
    }
}
