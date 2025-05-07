//package com.cloudfile;
//
//import com.cloudfile.component.xfyun.Main;
//import com.cloudfile.component.xfyun.dto.ResponseMsg;
//import com.cloudfile.component.xfyun.dto.StatusResp;
//import com.cloudfile.component.xfyun.dto.SummaryResp;
//import com.cloudfile.component.xfyun.dto.UploadResp;
//import com.cloudfile.component.xfyun.util.ChatDocUtil;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.junit.jupiter.api.Test;
//
//import java.util.List;
//
//@SpringBootTest
//class EasyPanTests {
//
//    private static final String appId = "6368c7a5";//控制台获取
//    private static final String secret = "YjEyMmY1OWZiMGQzNTA1ZmJjZGUwZmUx";//控制台获取
//    private static final String uploadUrl = "https://chatdoc.xfyun.cn/openapi/v1/file/upload";
//    private static final String chatUrl = "wss://chatdoc.xfyun.cn/openapi/chat";
//    private static final String statusUrl = "https://chatdoc.xfyun.cn/openapi/v1/file/status";
//    private static final String SummaryStartUrl = "https://chatdoc.xfyun.cn/openapi/v1/file/summary/start";
//    private static final String SummaryQueryUrl = "https://chatdoc.xfyun.cn/openapi/v1/file/summary/query";
//    private static final  String FileId="fc12274d32ca4e95bdf95a2f45b8908e";
//    private  static final ChatDocUtil chatDocUtil = new ChatDocUtil();
//    @Test
//    void testUpload() {
//        // 1、上传。注意：demo采用文件形式上传；如果以文件url形式上传，需要填写fileName
//        UploadResp uploadResp = chatDocUtil.upload(Main.class.getResource("/").getPath() + "test.txt", uploadUrl, appId, secret);
//        System.out.println("请求sid=" + uploadResp.getSid());
//        System.out.println("文件id=" + uploadResp.getData().getFileId());
//    }
//    @Test
//    void testChatStatus() {
//        StatusResp status = chatDocUtil.status(statusUrl, FileId, appId, secret);
//        List<StatusResp.Datas> datasList = status.getData();
//        for (StatusResp.Datas datas : datasList) {
//            System.out.println("文件id=" + datas.getFileId() + " 文件状态=" + datas.getFileStatus());
//        }
//    }
//
//    @Test
//    void contextLoads() {
//        StatusResp status = chatDocUtil.status(statusUrl, FileId, appId, secret);
//        List<StatusResp.Datas> datasList = status.getData();
//        for (StatusResp.Datas datas : datasList) {
//            System.out.println("文件id=" + datas.getFileId() + " 文件状态=" + datas.getFileStatus());
//            // 3、问答。上传文件状态为vectored时才可以问答，文件状态可以调用上面的接口查询
//            //        String chatFileId = "xxxxxxxxxxxxxxxxxx";
//            if(datas.getFileStatus().equals("vectored")){
//                String question = "故事一讲了什么内容";
//                chatDocUtil.chat(chatUrl, FileId, question, appId, secret);
//            }
//        }
//    }
//
//    @Test
//    void testcheckSummary(){
//
//        //4、文档总结 + 5、获取文档总结/概要信息。这里采用轮询的方式，文档总结完成后获取总结内容
////                String SummaryFileId = uploadResp.getData().getFileId();
//        ResponseMsg responseMsg = chatDocUtil.start(SummaryStartUrl, FileId, appId, secret);
//        while (true) {
//            if (responseMsg.getCode() != 0) {
//                System.out.println("文档总结发起失败，请重新发起,code=" + responseMsg.getCode() + " sid =" + responseMsg.getSid());
//                break;
//            }
//            SummaryResp summaryResp = chatDocUtil.query(SummaryQueryUrl, FileId, appId, secret);
//            if (summaryResp.getData().getSummaryStatus().equals("done")) {
//                System.out.println("文档总结最终结果： " + summaryResp.getData().getSummary());
//                break;
//            } else if (summaryResp.getData().getSummaryStatus().equals("failed")) {
//                System.out.println("总结失败");
//                break;
//            } else if (summaryResp.getData().getSummaryStatus().equals("illegal")) {
//                System.out.println("内容敏感");
//                break;
//            } else {
//                System.out.println("总结中");
//            }
//        }
//    }
//
//}