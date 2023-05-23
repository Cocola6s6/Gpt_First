package com.cocola.gpt.controller.vo;

import lombok.Data;

@Data
public class GptError {

    private String message;

    private String type;

    private String param;

    private String code;
}
