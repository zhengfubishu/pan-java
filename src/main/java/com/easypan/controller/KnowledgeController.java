package com.easypan.controller;

import cn.hutool.core.date.DateTime;
import com.alibaba.nacos.api.model.v2.Result;
import com.easypan.component.xfyun.XfUtil;
import com.easypan.component.xfyun.dto.UploadResp;
import com.easypan.entity.config.AppConfig;
import com.easypan.entity.constants.Constants;
import com.easypan.entity.po.KnowledgeFiles;
import com.easypan.entity.vo.KnowledgeFileInfoVO;
import com.easypan.entity.vo.KnowledgeFileVO;
import com.easypan.entity.vo.ResponseVO;
import com.easypan.mappers.KnowledgeFilesMapper;
import com.easypan.service.KnowledgeService;
import com.easypan.utils.StringTools;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/knowledge")
public class KnowledgeController extends  ABaseController{

//    @Autowired
//    private KnowledgeService knowledgeService;
    @Autowired
    private AppConfig appConfig;
    @Autowired
    private KnowledgeService knowledgeService;
    private final Logger logger=LoggerFactory.getLogger(KnowledgeController.class);

    @PostMapping("/upload2")
    public ResponseVO uploadFile2(@RequestParam("file") MultipartFile file) {
        try {
           KnowledgeFiles knowledgeFile =new KnowledgeFiles();
            // 生成唯一文件ID`
            String fileId = StringTools.getRandomString(Constants.LENGTH_10);

            // 存储文件到指定目录
            String storagePath = appConfig.getProjectFolder() + Constants.FILE_FOLDER_KNOWLEDGE;
//            Files.createDirectories(storagePath);
            File tempFileFolder = new File(storagePath);
            if (!tempFileFolder.exists()) {
                tempFileFolder.mkdirs();
            }
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String storedFileName = fileId + fileExtension;
            DateTime now =DateTime.now();
            String storageFilePathName=tempFileFolder.getPath()+"\\"+storedFileName;
            // 保存文件
            file.transferTo(new File(storageFilePathName));
            UploadResp uploadResp =XfUtil.testUpload(storageFilePathName);
            knowledgeFile.setFileId(fileId);
            knowledgeFile.setCloudFileId(uploadResp.getData().getFileId());
            knowledgeFile.setFileName(originalFilename);
            knowledgeFile.setFilePath(storedFileName);
            knowledgeFile.setFileSize(file.getSize());
            knowledgeFile.setUploadTime(now);
            knowledgeService.save(knowledgeFile);
            // 返回文件信息
            return getSuccessResponseVO(new KnowledgeFileVO(
                    fileId,
                    originalFilename
            ));

        } catch (Exception e) {
            return  getSuccessResponseVO("文件上传失败: " + e.getMessage());
        }
    }

@GetMapping("loadFileList")
    public ResponseVO loadFileList() {
    List<KnowledgeFiles> knowledgeFiles = knowledgeService.getKnowledgeFiles();
    List<KnowledgeFileVO> knowledgeFileVOList = new ArrayList<>();
    for (KnowledgeFiles knowledgeFile : knowledgeFiles) {
        knowledgeFileVOList.add(new KnowledgeFileVO(knowledgeFile.getFileId(), knowledgeFile.getFileName()));
    }
    return getSuccessResponseVO(knowledgeFileVOList) ;
//    return  Result.failure("未定义");
}

    @PostMapping("loadFileList")
    public ResponseVO loadFileList2() {
        List<KnowledgeFiles> knowledgeFiles = knowledgeService.getKnowledgeFiles();
        List<KnowledgeFileVO> knowledgeFileVOList = new ArrayList<>();
        for (KnowledgeFiles knowledgeFile : knowledgeFiles) {
            knowledgeFileVOList.add(new KnowledgeFileVO(knowledgeFile.getFileId(), knowledgeFile.getFileName()));
        }
        return getSuccessResponseVO(knowledgeFileVOList);
    }

    @GetMapping("readfile")
    public void readFile(HttpServletResponse response, String filePath) {
        filePath = appConfig.getProjectFolder() + Constants.FILE_FOLDER_KNOWLEDGE + filePath;
        if (!StringTools.pathIsOk(filePath)) {
            return;
        }
//        if(!filePath.endsWith(".pdf")&&!filePath.endsWith(".docx")&&!filePath.endsWith(".doc")){
//            response.setContentType("text/plain; charset=utf-8");
//        }

        OutputStream out = null;
        FileInputStream in = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return;
            }
            in = new FileInputStream(file);
            byte[] byteData = new byte[1024];
            out = response.getOutputStream();
            int len = 0;
            while ((len = in.read(byteData)) != -1) {
                out.write(byteData, 0, len);
            }
            out.flush();
        } catch (Exception e) {
            logger.error("读取文件异常", e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    logger.error("IO异常", e);
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error("IO异常", e);
                }
            }
        }
    }

    @RequestMapping("/chat")
    public ResponseVO handleChat(
//            WebSocket webSocket,
            HttpSession session,
            @RequestParam("fileId") String fileId,
            @RequestParam("question") String question) throws InterruptedException {
        KnowledgeFiles knowledgeFiles=knowledgeService.getFileById(fileId);
        String cloud_file_id=knowledgeService.getFileById(fileId).getCloudFileId();
//        String cloud_file_id= "fc12274d32ca4e95bdf95a2f45b8908e";
//        question="总结以下文章";
        Map<String, String> response = new HashMap<>();
        if(cloud_file_id.equals(null)||cloud_file_id.isEmpty()){
            session.setAttribute("summary","文件在知识库中不存在");
//            webSocket.send("文件在知识库中不存在");
        }else{
            String fileStatus = XfUtil.testChatStatus(cloud_file_id).get(0).getFileStatus();
            System.out.println(fileStatus);
            XfUtil.contextLoads(cloud_file_id, question,session);

//            System.out.println(session);
            System.out.println("============================");
            // 新增等待逻辑（示例）
            int maxWait = 5000; // 最大等待5秒
            int waited = 0;
            Thread.sleep(5000);
            while (session.getAttribute("answer") == null && waited < maxWait) {
                try {
                    Thread.sleep(100);
                    waited += 100;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
//        String summary=(String) session.getAttribute("summary");
        response.put("summary", (String) session.getAttribute("summary"));
        response.put("answer", (String) session.getAttribute("answer"));
        return  getSuccessResponseVO(response);
    }

    @RequestMapping("/check")
    public ResponseVO handleCheck(
//            WebSocket webSocket,
            HttpSession session,
            @RequestParam("fileId") String fileId,
            @RequestParam("question") String question) {
        KnowledgeFiles knowledgeFile = knowledgeService.getFileById(fileId);
//        String cloud_file_id=knowledgeFile.getCloudFileId()!=null?knowledgeFile.getCloudFileId():"fc12274d32ca4e95bdf95a2f45b8908e";
        String cloud_file_id=knowledgeFile.getCloudFileId()!=null?knowledgeFile.getCloudFileId():"";
        logger.info("cloud_file_id{}",cloud_file_id);
        logger.info("cloud_file_id{}",cloud_file_id);
        return  getSuccessResponseVO(cloud_file_id);

    }


//    @RequestMapping("test3")
//   public ResponseVO handleChatTest(
////            WebSocket webSocket,
//            HttpSession session,
//            @RequestParam("fileId") String fileId,
//            @RequestParam("question") String question) {
//        Map<String, String> response = new HashMap<>();
//        String cloud_file_id= "fc12274d32ca4e95bdf95a2f45b8908e";
//        question="总结以下文章";
//        if(cloud_file_id.equals(null)||cloud_file_id.isEmpty()){
//            session.setAttribute("summary","文件在知识库中不存在");
////            webSocket.send("文件在知识库中不存在");
//        }else{
//            String fileStatus = XfUtil.testChatStatus(cloud_file_id).get(0).getFileStatus();
//            System.out.println(fileStatus);
//            XfUtil.contextLoads(cloud_file_id, question,session);
//
////            System.out.println(session);
//            System.out.println("============================");
//        }
//        String summary=(String) session.getAttribute("summary");
//        response.put("summary", "测试环境下："+(String) session.getAttribute("summary"));
//        return  getSuccessResponseVO(response);
//    }
//    // 文件上传
//    @PostMapping("/upload")
//    public Result uploadFile(@RequestParam("file") MultipartFile file) {
//        try {
//            KnowledgeFileVO fileInfo = knowledgeService.uploadFile(file);
//            return Result.success(fileInfo);
//        } catch (Exception e) {
//            return Result.failure("文件上传失败: " + e.getMessage());
//        }
//    }
//
//    @RequestMapping("/uploadFile")
//    @GlobalInterceptor(checkParams = true)
//    public ResponseVO uploadFile1(HttpSession session,
//                                  String fileId,
//                                  MultipartFile file,
//                                  @VerifyParam(required = true) String fileName,
//                                  @VerifyParam(required = true) String filePid,
//                                  @VerifyParam(required = true) String fileMd5,
//                                  @VerifyParam(required = true) Integer chunkIndex,
//                                  @VerifyParam(required = true) Integer chunks) {
//
//        SessionWebUserDto webUserDto = getUserInfoFromSession(session);
//        UploadResultDto resultDto = knowledgeService.uploadFile1(webUserDto, fileId, file, fileName, filePid, fileMd5, chunkIndex, chunks);
//        return getSuccessResponseVO(resultDto);
//    }
//
    // 文件删除
    @RequestMapping("/delete/{fileId}")
    public ResponseVO deleteFile(@PathVariable String fileId) {
        knowledgeService.deleteKnowledgeFileById(fileId);
        return  getSuccessResponseVO("删除成功");
    }
}