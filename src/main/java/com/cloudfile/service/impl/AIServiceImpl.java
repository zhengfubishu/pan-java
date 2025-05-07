package com.cloudfile.service.impl;

import com.cloudfile.entity.dto.QuestionDTO;
import com.cloudfile.service.AIService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AIServiceImpl implements AIService {

//    @Autowired
//    private FileContentExtractor contentExtractor;

//    @Autowired
//    private LLMService llmService;

    @Override
    public String processQuestion(QuestionDTO questionDTO) {
        // 1. 获取关联文件内容
        List<String> contents = questionDTO.getFileIds().stream()
            .map(this::getFileContent)
            .collect(Collectors.toList());

        // 2. 构造LLM提示词
        String prompt = buildPrompt(questionDTO.getQuestion(), contents);

//        // 3. 调用大模型服务
//        return llmService.queryLLM(prompt);
        return null;
    }

    @Override
    public String getFileContent(String fileId) {
        // 实现文件内容提取逻辑
//        return contentExtractor.extractText(fileId);
        //todo
        return null;
    }

    @Override
    public String buildPrompt(String question, List<String> contexts) {
        // 构造包含上下文的提示词模板
        return String.format("基于以下知识库内容：\n%s\n\n请回答：%s",
            String.join("\n", contexts),
            question);
    }
}