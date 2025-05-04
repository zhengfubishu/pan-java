package com.easypan.entity.po;

//import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;


import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@TableName("knowledge_files")
public class KnowledgeFiles {
    // 文件的唯一标识
    private String fileId;
    // 云系统返回的文件唯一 ID
    private String cloudFileId;
    // 文件的名称
    private String fileName;
    // 文件的大小
    private Long fileSize;
    //文件路径
    private  String filePath;
    // 文件的上传时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date uploadTime;
//    // 文件的版本号
//    private Integer version;
}    