package com.cloudfile.service;

import com.cloudfile.entity.dto.QuestionDTO;

import java.util.List;

public interface AIService {
    String processQuestion(QuestionDTO questionDTO);

    String getFileContent(String fileId);

    String buildPrompt(String question, List<String> contexts);
}
