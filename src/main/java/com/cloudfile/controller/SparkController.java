package com.cloudfile.controller;




import com.cloudfile.component.spark4j.SparkClient;
import com.cloudfile.component.spark4j.constant.SparkApiVersion;
import com.cloudfile.component.spark4j.exception.SparkException;
import com.cloudfile.component.spark4j.listener.SparkBaseListener;
import com.cloudfile.component.spark4j.listener.SparkConsoleListener;
import com.cloudfile.component.spark4j.model.SparkMessage;
import com.cloudfile.component.spark4j.model.SparkSyncChatResponse;
import com.cloudfile.component.spark4j.model.request.SparkRequest;
import com.cloudfile.component.spark4j.model.request.function.SparkFunctionBuilder;
import com.cloudfile.component.spark4j.model.response.SparkResponse;
import com.cloudfile.component.spark4j.model.response.SparkResponseFunctionCall;
import com.cloudfile.component.spark4j.model.response.SparkResponseUsage;
import com.cloudfile.component.spark4j.model.response.SparkTextUsage;
import com.cloudfile.entity.vo.ResponseVO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import okhttp3.WebSocket;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 * 讯飞sse流式输出
 */

@RestController
@RequestMapping("/spark")
public class SparkController extends ABaseController {


    /**
     * 客户端实例，线程安全
     */
    private final static SparkClient sparkClient = new SparkClient();
    static {
        sparkClient.appid = "6368c7a5";
        sparkClient.apiKey = "9b7522ed87593d8912be355bc89d11d1";
        sparkClient.apiSecret = "YjEyMmY1OWZiMGQzNTA1ZmJjZGUwZmUx";
    }


    /***
     * 测试前后端
     * @param question
     * @param session
     * @return
     */
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamChat(@RequestParam("question") String question, HttpSession session) {  // 添加HttpSession参数
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        // 从session获取历史对话（线程安全处理）
        List<SparkMessage> messages = (List<SparkMessage>) session.getAttribute("chatHistory");
        if (messages == null) {
            messages = new ArrayList<>();
            // 初始化系统消息（只需一次）
            messages.add(SparkMessage.systemContent("<h3>在线文件智存系统AI小助手</h3>" +
                "<p>您好！欢迎使用在线文件智存系统！</p>" +
                    "<p> 我是您的智能助手，可以为您提供以下帮助：</p>" +
                "<p><ul>" +
                "  <li>解答各类知识问题</li>" +
                "  <li>提供写作和创意建议</li>" +
                "  <li>协助代码编写与调试</li>" +
                "  <li>多语言翻译服务</li>" +
                "  <li>数据分析与可视化支持</li>" +
                "</ul></p>" +
                "<p>请在输入框中提出您的需求，我会尽力以清晰、简洁的方式为您解答。</p>"));
        }

        // 添加用户新提问
        messages.add(SparkMessage.userContent(question));

        // 自定义监听器
        List<SparkMessage> finalMessages = messages;
        SparkBaseListener listener = new SparkBaseListener() {
            private final StringBuilder fullResponse = new StringBuilder();

            @Override
            public void onMessage(String content, SparkResponseUsage usage,
                                  Integer status, SparkRequest sparkRequest,
                                  SparkResponse sparkResponse, WebSocket webSocket) {
                try {
                    emitter.send(content);
                    fullResponse.append(content);

                    if (status == 2) {
                        // 添加助手回复到历史记录
                        synchronized(session) {  // 保证线程安全
                            finalMessages.add(SparkMessage.assistantContent(fullResponse.toString()));
                            session.setAttribute("chatHistory", finalMessages);
                        }
                        emitter.complete();
                    }
                } catch (IOException e) {
                    emitter.completeWithError(e);
                }
            }
        };

        List<SparkMessage> finalMessages1 = messages;
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                SparkRequest sparkRequest = SparkRequest.builder()
                        .messages(finalMessages1)
                        .maxTokens(2048)
                        .temperature(0.2)
                        .apiVersion(SparkApiVersion.V1_5)
                        .build();

                sparkClient.chatStream(sparkRequest, listener);
            } catch (SparkException e) {
                emitter.completeWithError(e);
            }
        });
        return emitter;
    }

    @PostMapping("/reset")
    public ResponseVO resetChat(HttpSession session) {
        session.removeAttribute("chatHistory");
        return getSuccessResponseVO("对话历史已重置");
    }
//以上
    @GetMapping("history")
    public String HistoryTest() {

        // 消息列表，可以在此列表添加历史对话记录
        List<SparkMessage> messages = new ArrayList<>();
        messages.add(SparkMessage.systemContent("请你扮演我的语文老师李老师，问我讲解问题问题，希望你可以保证知识准确，逻辑严谨。"));
        messages.add(SparkMessage.userContent("鲁迅和周树人小时候打过架吗？"));
        // 构造请求
        SparkRequest sparkRequest = SparkRequest.builder()
                // 消息列表
                .messages(messages)
                // 模型回答的tokens的最大长度,非必传，默认为2048。
                // V1.5取值为[1,4096]
                // V2.0取值为[1,8192]
                // V3.0取值为[1,8192]
                .maxTokens(2048)
                // 核采样阈值。用于决定结果随机性,取值越高随机性越强即相同的问题得到的不同答案的可能性越高 非必传,取值为[0,1],默认为0.5
                .temperature(0.2)
                .apiVersion(SparkApiVersion.V1_5)
                .build();

        try {
            // 同步调用
            SparkSyncChatResponse chatResponse = sparkClient.chatSync(sparkRequest);
            SparkTextUsage textUsage = chatResponse.getTextUsage();
            System.out.println("\n回答：" + chatResponse.getContent());
            messages.add(SparkMessage.assistantContent(chatResponse.getContent()));
            System.out.println("\n提问tokens：" + textUsage.getPromptTokens()
                    + "，回答tokens：" + textUsage.getCompletionTokens()
                    + "，总消耗tokens：" + textUsage.getTotalTokens());

        } catch (SparkException e) {
            System.out.println("发生异常了：" + e.getMessage());
        }
        return messages.toString();
    }

    @GetMapping("chatStream")
    String chatStreamTest() throws InterruptedException {
        // 消息列表，可以在此列表添加历史对话记录
        List<SparkMessage> messages = new ArrayList<>();
        messages.add(SparkMessage.userContent("我今天买了三个苹果，昨天吃了一个，现在我还剩几个呢？"));

        // 构造请求
        SparkRequest sparkRequest = SparkRequest.builder()
                // 消息列表
                .messages(messages)
                // 模型回答的tokens的最大长度,非必传，默认为2048。
                // V1.5取值为[1,4096]
                // V2.0取值为[1,8192]
                // V3.0取值为[1,8192]
                .maxTokens(2048)
                // 核采样阈值。用于决定结果随机性,取值越高随机性越强即相同的问题得到的不同答案的可能性越高 非必传,取值为[0,1],默认为0.5
                .temperature(0.2)
                // 指定请求版本，默认使用3.0版本
//                .apiVersion(SparkApiVersion.V4_0)
                .apiVersion(SparkApiVersion.V1_5)
                .build();

        // 使用默认的控制台监听器，流式调用；
        // 实际使用时请继承SparkBaseListener自定义监听器实现
        sparkClient.chatStream(sparkRequest, new SparkConsoleListener());

        Thread.sleep(60000);
        return messages.toString();
    }

    @GetMapping("chatSync")
    String chatSyncTest() throws JsonProcessingException {
        // 消息列表，可以在此列表添加历史对话记录
        List<SparkMessage> messages = new ArrayList<>();
        messages.add(SparkMessage.userContent("我今天买了三个苹果，昨天吃了一个，现在我还剩几个呢？"));

        // 构造请求
        SparkRequest sparkRequest = SparkRequest.builder()
                // 消息列表
                .messages(messages)
                // 模型回答的tokens的最大长度,非必传，默认为2048。
                // V1.5取值为[1,4096]
                // V2.0取值为[1,8192]
                // V3.0取值为[1,8192]
                .maxTokens(2048)
                // 核采样阈值。用于决定结果随机性,取值越高随机性越强即相同的问题得到的不同答案的可能性越高 非必传,取值为[0,1],默认为0.5
                .temperature(0.2)
//                .apiVersion(SparkApiVersion.V4_0)
                .apiVersion(SparkApiVersion.V1_5)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        System.out.println("提问：" + objectMapper.writeValueAsString(messages));

        try {
            // 同步调用
            SparkSyncChatResponse chatResponse = sparkClient.chatSync(sparkRequest);
            SparkTextUsage textUsage = chatResponse.getTextUsage();

            System.out.println("\n回答：\n" + chatResponse.getContent());
            System.out.println("\n提问tokens：" + textUsage.getPromptTokens()
                    + "，回答tokens：" + textUsage.getCompletionTokens()
                    + "，总消耗tokens：" + textUsage.getTotalTokens());
        } catch (SparkException e) {
            System.out.println("发生异常了：" + e.getMessage());
        }
        return messages.toString();
    }

    @GetMapping("functionCall")
    String functionCallTest() throws JsonProcessingException {
        // 消息列表，可以在此列表添加历史对话记录
        List<SparkMessage> messages = new ArrayList<>();
        messages.add(SparkMessage.userContent("科大讯飞的最新股票价格是多少"));

        // 构造请求
        SparkRequest sparkRequest = SparkRequest.builder()
                // 消息列表
                .messages(messages)
                // 使用functionCall功能版本需要大于等于3.0
                .apiVersion(SparkApiVersion.V4_0)
                // 添加方法，可多次调用添加多个方法
                .addFunction(
                        // 回调时回传的方法名
                        SparkFunctionBuilder.functionName("stockPrice")
                                // 让大模型理解方法意图 方法描述
                                .description("根据公司名称查询最新股票价格")
                                // 方法需要的参数。可多次调用添加多个参数
                                .addParameterProperty("companyName", "string", "公司名称")
                                // 指定以上的参数哪些是必传的
                                .addParameterRequired("companyName").build()
                ).build();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        System.out.println("request：" + objectMapper.writeValueAsString(sparkRequest));

        // 同步调用
        SparkSyncChatResponse chatResponse = sparkClient.chatSync(sparkRequest);
        SparkTextUsage textUsage = chatResponse.getTextUsage();
        SparkResponseFunctionCall functionCall = chatResponse.getFunctionCall();

        if (null != functionCall) {
            String functionName = functionCall.getName();
            Map<String, Object> arguments = functionCall.getMapArguments();

            System.out.println("\n收到functionCall：方法名称：" + functionName + "，参数：" + objectMapper.writeValueAsString(arguments));

            // 在这里根据方法名和参数自行调用方法实现
            SparkSyncChatResponse chatResponse2 = sparkClient.chatSync(sparkRequest);
            System.out.println("\n回答：" + chatResponse2.getContent());
        } else {
            System.out.println("\n回答：" + chatResponse.getContent());
        }

        System.out.println("\n提问tokens：" + textUsage.getPromptTokens()
                + "，回答tokens：" + textUsage.getCompletionTokens()
                + "，总消耗tokens：" + textUsage.getTotalTokens());
        return messages.toString();
    }



}
