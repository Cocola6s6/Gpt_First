package com.cocola.gpt.controller.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ChatCompletionChoice {

    private Integer index;

    private ChatMessage message;

    private String finishReason;
}