package com.cloudfile.component.xfyun;

import com.cloudfile.component.xfyun.dto.ResponseMsg;
import com.cloudfile.component.xfyun.dto.StatusResp;
import com.cloudfile.component.xfyun.dto.SummaryResp;
import com.cloudfile.component.xfyun.dto.UploadResp;
import com.cloudfile.component.xfyun.util.ChatDocUtil;
import jakarta.servlet.http.HttpSession;
import okhttp3.WebSocket;

import java.util.List;

public class XfUtil {

    private static final String appId = "6368c7a5";//控制台获取
    private static final String secret = "YjEyMmY1OWZiMGQzNTA1ZmJjZGUwZmUx";//控制台获取
    private static final String uploadUrl = "https://chatdoc.xfyun.cn/openapi/v1/file/upload";
    private static final String chatUrl = "wss://chatdoc.xfyun.cn/openapi/chat";
    private static final String statusUrl = "https://chatdoc.xfyun.cn/openapi/v1/file/status";
    private static final String SummaryStartUrl = "https://chatdoc.xfyun.cn/openapi/v1/file/summary/start";
    private static final String SummaryQueryUrl = "https://chatdoc.xfyun.cn/openapi/v1/file/summary/query";
    private static final  String FileId="fc12274d32ca4e95bdf95a2f45b8908e";
    private  static final ChatDocUtil chatDocUtil = new ChatDocUtil();

    public static UploadResp testUpload(String fileName) {
        // 1、上传。注意：demo采用文件形式上传；如果以文件url形式上传，需要填写fileName
        UploadResp uploadResp = chatDocUtil.upload(fileName, uploadUrl, appId, secret);
        System.out.println("请求sid=" + uploadResp.getSid());
        System.out.println("文件id=" + uploadResp.getData().getFileId());
        if (uploadResp.success()) {
            System.out.println("上传成功");
        } else {
            System.out.println("上传失败");
        }
        return uploadResp;
    }

    public static List<StatusResp.Datas> testChatStatus(String FileId) {
        StatusResp status = chatDocUtil.status(statusUrl, FileId, appId, secret);
        List<StatusResp.Datas> datasList = status.getData();
        for (StatusResp.Datas datas : datasList) {
            System.out.println("文件id=" + datas.getFileId() + " 文件状态=" + datas.getFileStatus());
        }
        return datasList;
    }

//    public static void contextLoads(String FileId, String question, HttpSession session, WebSocket webSocket) {
//        chatDocUtil.chat(chatUrl, FileId, question, appId, secret, (result) -> {
//            try {
//                webSocket.send(result); // 通过WebSocket发送回答结果p
//                session.setAttribute("history", result);
//                System.out.println("回答结果是："+result);
//                webSocket.close(1000, "回答完成"); // 回答完成后关闭连接
//            } catch (Exception e) {
//                System.out.println("发送失败");
//                webSocket.close(1001, "发送失败"); // 异常时关闭连接
//            }
//        });
//    }

    public static  void contextLoads(String FileId, String question,HttpSession session
    ) {
        chatDocUtil.chat3(chatUrl, FileId, question, appId, secret,session);
//        System.out.println("方法contextLoads："+session.getAttribute("answer"));
        ResponseMsg responseMsg = chatDocUtil.start(SummaryStartUrl, FileId, appId, secret);
        while (true) {
            if (responseMsg.getCode() != 0) {
                System.out.println("文档总结发起失败，请重新发起,code=" + responseMsg.getCode() + " sid =" + responseMsg.getSid());
                break;
            }
            SummaryResp summaryResp = chatDocUtil.query(SummaryQueryUrl, FileId, appId, secret);
            if (summaryResp.getData().getSummaryStatus().equals("done")) {
                System.out.println("文档总结最终结果： " + summaryResp.getData().getSummary());
                session.setAttribute("summary", summaryResp.getData().getSummary());
                break;
            } else if (summaryResp.getData().getSummaryStatus().equals("failed")) {
                session.setAttribute("summary", "总结失败");
                System.out.println("总结失败");
                break;
            } else if (summaryResp.getData().getSummaryStatus().equals("illegal")) {
                session.setAttribute("summary", "内容敏感");
                System.out.println("内容敏感");
                break;
            } else {
                System.out.println("总结中");
            }
        }
    }

    public static void testcheckSummary(WebSocket webSocket, String fileId,HttpSession session) { // 新增fileId参数
        ResponseMsg responseMsg = chatDocUtil.start(SummaryStartUrl, fileId, appId, secret);
        if (responseMsg.getCode() != 0) {
            webSocket.send("文档总结发起失败: " + responseMsg.getCode());
            webSocket.close(1001, "文档总结发起失败");
            return;
        }
        while (true) {
            SummaryResp summaryResp = chatDocUtil.query(SummaryQueryUrl, fileId, appId, secret); // 修正URL参数
            String status = summaryResp.getData().getSummaryStatus();
            webSocket.send("当前状态: " + status); // 实时推送状态
            if ("done".equals(status)) {
                webSocket.send("最终结果: " + summaryResp.getData().getSummary());
                session.setAttribute("summary", summaryResp.getData().getSummary());
            } else if ("failed".equals(status)) {
                webSocket.send("总结失败");
            } else if ("illegal".equals(status)) {
                webSocket.send("内容敏感");
            }
            if ("done".equals(status) || "failed".equals(status) || "illegal".equals(status)) {
                webSocket.close(1000,"总结完成"); // 完成后关闭连接
                break;
            }
            try { Thread.sleep(2000); } catch (InterruptedException e) {}
        }
    }
}
