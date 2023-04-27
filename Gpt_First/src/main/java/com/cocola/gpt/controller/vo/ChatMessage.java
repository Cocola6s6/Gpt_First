package com.cocola.gpt.controller.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ChatMessage {
    //消息角色
    private String role;
    //消息内容
    private String content;
}