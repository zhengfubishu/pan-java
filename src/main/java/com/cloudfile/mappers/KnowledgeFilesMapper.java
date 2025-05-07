package com.cloudfile.mappers;

import com.cloudfile.entity.po.KnowledgeFiles;

import java.util.List;


public interface KnowledgeFilesMapper {
    void save(KnowledgeFiles knowledgeFile);

    List<KnowledgeFiles> getKnowledgeFiles();

    KnowledgeFiles getKnowledgeFileById(String fileId);

    void deleteKnowledgeFileById(String fileId);


//
//    List<KnowledgeFiles> selectList();
}