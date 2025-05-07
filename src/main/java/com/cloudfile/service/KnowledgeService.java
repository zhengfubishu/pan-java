package com.cloudfile.service;

import com.cloudfile.entity.po.KnowledgeFiles;

import java.util.List;

public interface KnowledgeService {
    void save(KnowledgeFiles knowledgeFile);

    List<KnowledgeFiles> getKnowledgeFiles();

   KnowledgeFiles getFileById(String fileId);

    void deleteKnowledgeFileById(String fileId);
}
