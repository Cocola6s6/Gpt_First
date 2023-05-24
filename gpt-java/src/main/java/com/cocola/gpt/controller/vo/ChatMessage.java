package com.cocola.gpt.controller.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
public class ChatMessage {
    /**
     * 消息角色
     */
    @NotBlank(message = "role can not be blank")
    private String role;

    /**
     * 消息内容
     */
    @NotBlank(message = "content can not be blank")
    private String content;
}