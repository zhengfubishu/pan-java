package com.cloudfile.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
// 用于给前端展示的知识库文件信息VO类
public class KnowledgeFileVO {
    // 文件的唯一标识
    private String fileId;
    // 文件的名称
    private String fileName;
//    // 文件的上传时间
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    private Date uploadTime;
}