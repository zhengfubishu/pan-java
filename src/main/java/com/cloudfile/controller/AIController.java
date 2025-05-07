//package com.cloudfile.controller;
//
//import com.alibaba.nacos.api.model.v2.Result;
//import com.cloudfile.entity.dto.QuestionDTO;
//import com.cloudfile.service.AIService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Collections;
//
//@RestController
//@RequestMapping("/ai")
//public class AIController {
//
//    @Autowired
//    private AIService aiService;
//
//    @PostMapping("/query")
//    public Result query(@RequestBody QuestionDTO questionDTO) {
//        try {
//            String answer = aiService.processQuestion(questionDTO);
//            return Result.success(Collections.singletonMap("answer", answer));
//        } catch (Exception e) {
//            return Result.failure("问答服务异常: " + e.getMessage());
//        }
//    }
//}