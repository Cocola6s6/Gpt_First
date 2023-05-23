package com.cocola.gpt.controller.vo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: yangshiyuan
 * @Date: 2022/9/5
 * @Description:
 */
@Data
@Accessors(chain = true)
public class GptRequestVo {
    @NotBlank(message = "model can not be blank")
    private String model;

    @NotNull(message = "messages can not be null")
    private List<ChatMessage> messages;

    private Double temperature;

    private Integer n;

    private Boolean stream;

    private List<String> stop;

    private Integer max_tokens;

    private String user;
}
