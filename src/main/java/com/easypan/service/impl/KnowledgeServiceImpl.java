package com.easypan.service.impl;

import com.easypan.entity.po.KnowledgeFiles;
import com.easypan.mappers.KnowledgeFilesMapper;
import com.easypan.service.KnowledgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
//
@Service
public class KnowledgeServiceImpl  implements KnowledgeService {
    @Autowired
    private KnowledgeFilesMapper knowledgeFilesMapper;
    public void save(KnowledgeFiles knowledgeFiles) {
        knowledgeFilesMapper.save(knowledgeFiles);
    }

    @Override
    public List<KnowledgeFiles> getKnowledgeFiles() {
        List<KnowledgeFiles> knowledgeFiles = knowledgeFilesMapper.getKnowledgeFiles();
//        List<KnowledgeFiles>  knowledgeFiles=knowledgeFilesMapper.selectList();
        return knowledgeFiles;
    }

public KnowledgeFiles getFileById(String fileId) {
    return knowledgeFilesMapper.getKnowledgeFileById(fileId);
}

public void deleteKnowledgeFileById(String fileId) {
    knowledgeFilesMapper.deleteKnowledgeFileById(fileId);
}

}
