/*
package com.easypan.controller;


import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("ai")
public class chatController {
    @Autowired
    private OpenAiChatModel chatModel;

//    @Autowired
//    private PromptTemplate promptTemplate;
    */
/**
     *
     * @param msg 提的问题
     * @return
     *//*

    @RequestMapping("chat")
    public String chat(@RequestParam(value = "msg")String msg) {
        String called=chatModel.call(msg);
        return "高州你知道嘛："+called;
    }

    @RequestMapping("chat2")
    public String chat2(@RequestParam(value = "msg")String msg) {
//        PromptTemplate
//        Message()
        ChatResponse chatResponse = chatModel.call(new Prompt(msg));
//        new UserMessage()
        Generation generation = chatResponse.getResults().get(0);
        AssistantMessage output = generation.getOutput();
        String content= output
                .getContent();
//      chatResponse.getResult().g
            return content;
//        return chatResponse.toString();
    }

    @RequestMapping("chat3")
    public String chat3(@RequestParam(value = "msg")String msg) {
        ChatResponse chatResponse = chatModel.call(new Prompt(msg));
        getUsageDetail(chatResponse.getMetadata().getUsage());
        Generation generation = chatResponse.getResults().get(0);
        AssistantMessage output = generation.getOutput();
        String content= output.getContent();
        return content;
//        return chatResponse.toString();
    }

    @RequestMapping("chat4")
    public String chat4() {
        // 创建用户消息
        UserMessage userMessage1 = new UserMessage("请告诉我如何制作巧克力蛋糕。");
        AssistantMessage assistantMessage1 = new AssistantMessage("要制作巧克力蛋糕，你需要以下材料：面粉、糖、可可粉、鸡蛋和黄油。首先，将烤箱预热至180摄氏度。");

        UserMessage userMessage2 = new UserMessage("繼續");
//        AssistantMessage assistantMessage2 = new AssistantMessage("好的！你可以使用代糖或香蕉泥来替代糖。以下是一个无糖巧克力蛋糕的配方：\n" +
//                "1. 1杯面粉\n" +
//                "2. 1/2杯可可粉\n" +
//                "3. 1/2杯代糖\n" +
//                "4. 2个鸡蛋\n" +
//                "5. 1/2杯香蕉泥\n" +
//                "6. 1/2杯黄油\n" +
//                "将所有材料混合后，放入烤箱，烘烤约25分钟。");
//
//        UserMessage userMessage3 = new UserMessage("谢谢！我会试试这个配方。");

        // 创建消息列表
        List<Message> messages = new ArrayList<>();
        messages.add(userMessage1);
        messages.add(assistantMessage1);
        messages.add(userMessage2);
//        messages.add(assistantMessage2);
//        messages.add(userMessage3);

        // 创建 Prompt 实例
        Prompt prompt = new Prompt(messages);

        ChatResponse chatResponse = chatModel.call(prompt);
        String content1 = chatResponse
                .getResults().get(0)
                .getOutput()
                .getContent();
        return content1;
    }

    public String getUsageDetail(Usage usage){
//        Long generationTokens = usage.getGenerationTokens();//生成消费的token
//        Long promptTokens = usage.getPromptTokens();
//        Long totalTokens = usage.getTotalTokens();

        String  message="generationTokens="+usage.getGenerationTokens()
                +"\ngenerationTokens=" +usage.getPromptTokens()
                +"\ngenerationTokens=" +usage.getTotalTokens();
        System.out.println(message);
        return message;
    }
}
*/
