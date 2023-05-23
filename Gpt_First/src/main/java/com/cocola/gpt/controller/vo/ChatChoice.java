package com.cocola.gpt.controller.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ChatChoice {

    private Integer index;

    private ChatMessage message;

    private String finishReason;
}