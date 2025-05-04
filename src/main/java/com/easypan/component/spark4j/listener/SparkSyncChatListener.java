package com.easypan.component.spark4j.listener;


import com.easypan.component.spark4j.model.SparkSyncChatResponse;
import com.easypan.component.spark4j.model.request.SparkRequest;
import com.easypan.component.spark4j.model.response.SparkResponse;
import com.easypan.component.spark4j.model.response.SparkResponseFunctionCall;
import com.easypan.component.spark4j.model.response.SparkResponseUsage;
import okhttp3.Response;
import okhttp3.WebSocket;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SparkSyncChatListener

 */
public class SparkSyncChatListener extends SparkBaseListener {
    private static final Logger logger = LoggerFactory.getLogger(SparkSyncChatListener.class);

    private final StringBuilder stringBuilder = new StringBuilder();

    private final SparkSyncChatResponse sparkSyncChatResponse;

    public SparkSyncChatListener(SparkSyncChatResponse sparkSyncChatResponse) {
        this.sparkSyncChatResponse = sparkSyncChatResponse;
    }

    @Override
    public void onMessage(String content, SparkResponseUsage usage, Integer status, SparkRequest sparkRequest, SparkResponse sparkResponse, WebSocket webSocket) {
        stringBuilder.append(content);
        if (2 == status) {
            sparkSyncChatResponse.setContent(stringBuilder.toString());
            sparkSyncChatResponse.setTextUsage(usage.getText());
            sparkSyncChatResponse.setOk(true);
        }
    }

    @Override
    public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, Response response) {
        logger.error("讯飞星火api发生异常", t);
        sparkSyncChatResponse.setOk(true);
        sparkSyncChatResponse.setException(t);
    }

    /**
     * 收到functionCall调用此方法
     *
     * @param functionCall  functionCall
     * @param sparkRequest  本次会话的请求参数
     * @param sparkResponse 本次回调的响应数据
     * @param webSocket     本次会话的webSocket连接
     */
    @Override
    public void onFunctionCall(SparkResponseFunctionCall functionCall, SparkResponseUsage usage, Integer status, SparkRequest sparkRequest, SparkResponse sparkResponse, WebSocket webSocket) {
        if (2 == status) {
            sparkSyncChatResponse.setContent(stringBuilder.toString());
            sparkSyncChatResponse.setTextUsage(usage.getText());
            sparkSyncChatResponse.setFunctionCall(functionCall);
            sparkSyncChatResponse.setOk(true);
        }
    }
}
