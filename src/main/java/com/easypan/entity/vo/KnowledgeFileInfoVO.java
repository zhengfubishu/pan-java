package com.easypan.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class KnowledgeFileInfoVO {
    private String fileId;
//    private String storagePath;
    private String fileName;
    private LocalDateTime uploadTime;
}