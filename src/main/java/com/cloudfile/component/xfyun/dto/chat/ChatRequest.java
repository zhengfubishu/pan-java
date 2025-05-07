package com.cloudfile.component.xfyun.dto.chat;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * ChatRequest
 *
 *
 **/
@Getter
@Setter
@Builder
public class ChatRequest {

    private List<String> fileIds;

    private List<ChatMessage> messages;

    private Integer topN;

    private ChatExtends chatExtends;
}