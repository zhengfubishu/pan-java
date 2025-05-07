//package com.easypan.controller;
//
//import com.easypan.component.xfyun.XfUtil;
//import com.easypan.component.xfyun.dto.ResponseMsg;
//import com.easypan.component.xfyun.dto.SummaryResp;
//
//import com.easypan.service.KnowledgeService;
//import jakarta.servlet.http.HttpSession;
//import okhttp3.WebSocket;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//
//// 新增Spring Boot控制器处理前端请求
//@RestController("/ai")
//public class ChatDocController {
//
//    @Autowired
//    private KnowledgeService knowledgeService;
//    @PostMapping("/chat")
//    public void handleChat(
////            WebSocket webSocket,
//                           HttpSession session,
//                           @RequestParam("fileId") String fileId,
//                           @RequestParam("question") String question) {
////        String cloud_file_id=knowledgeService.getFileById(fileId).getCloudFileId();
//       String cloud_file_id= "fc12274d32ca4e95bdf95a2f45b8908e";
//       question="总结以下文章";
//        if(cloud_file_id.equals(null)||cloud_file_id.isEmpty()){
//            session.setAttribute("answer","文件在知识库中不存在");
////            webSocket.send("文件在知识库中不存在");
////            return null;
//        }else{
//            String fileStatus = XfUtil.testChatStatus(cloud_file_id).get(0).getFileStatus();
//            System.out.println(fileStatus);
//             XfUtil.contextLoads(cloud_file_id, question,session);
////            System.out.println(session);
//             System.out.println("============================");
//////            return chat;
////            System.out.println("方法handleChat answer："+session.getAttribute("answer"));// 传递参数
////            System.out.println("方法handleChat summary："+session.getAttribute("summary"));// 传递参数
//
//        }
//    }
//
////    @PostMapping("/summary")
////    public void handleSummary(WebSocket webSocket,
////                             HttpSession session,
////                             @RequestParam("fileId") String fileId) {
////        XfUtil.testcheckSummary(webSocket, fileId,session); // 传递fileId参数
////    }
//    @PostMapping("/summary")
//    public void handleSummary(WebSocket webSocket,
//                             HttpSession session,
//                             @RequestParam("fileId") String fileId) {
//        //4、文档总结 + 5、获取文档总结/概要信息。这里采用轮询的方式，文档总结完成后获取总结内容
//
//
//    }
//}