package com.cocola.gpt.controller.vo;

import lombok.Data;

import java.util.List;

@Data
public class GptResponseVo {

    private String id;

    private String object;

    private Long created;

    private String model;

    private ChatUsage usage;

    private List<ChatChoice> choices;

    private GptError error;
}
