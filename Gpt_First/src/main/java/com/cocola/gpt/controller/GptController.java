package com.cocola.gpt.controller;


import com.alibaba.cola.dto.Response;
import com.alibaba.cola.dto.SingleResponse;
import com.cocola.gpt.controller.vo.GptRequestVo;
import com.cocola.gpt.service.GptService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/v1/gpt")
public class GptController {

    private final GptService gptService;

    public GptController(GptService gptService) {
        this.gptService = gptService;
    }


    @PostMapping("/chatGpt")
    public Response chatGpt(@RequestBody GptRequestVo requestVo) {
        return SingleResponse.of(gptService.chatGpt(requestVo));
    }
}