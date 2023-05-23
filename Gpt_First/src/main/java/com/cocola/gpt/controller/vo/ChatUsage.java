package com.cocola.gpt.controller.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ChatUsage {
    private Integer prompt_tokens;

    private Integer completion_tokens;

    private Integer total_tokens;
}