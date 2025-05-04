package com.easypan.entity.dto;

import lombok.Data;

import java.util.List;
@Data
public class QuestionDTO {
    private String question;
    private List<String> fileIds;
}