package com.cocola.gpt.controller;


import com.alibaba.cola.dto.Response;
import com.alibaba.cola.dto.SingleResponse;
import com.cocola.gpt.controller.vo.GptRequestVo;
import com.cocola.gpt.service.GptService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/v1/gpt")
@RequiredArgsConstructor
public class GptController {

    private final GptService gptService;

    @PostMapping("/chatGpt")
    public Response chatGpt(@Valid @RequestBody GptRequestVo requestVo) {
        return SingleResponse.of(gptService.chatGpt(requestVo));
    }
}