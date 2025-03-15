package com.easypan.component.spark4j.model.response;

import java.io.Serializable;

/**
 * SparkResponse

 */
public class SparkResponse implements Serializable {
    private static final long serialVersionUID = 886720558849587945L;

    private SparkResponseHeader header;

    private SparkResponsePayload payload;

    public SparkResponseHeader getHeader() {
        return header;
    }

    public void setHeader(SparkResponseHeader header) {
        this.header = header;
    }

    public SparkResponsePayload getPayload() {
        return payload;
    }

    public void setPayload(SparkResponsePayload payload) {
        this.payload = payload;
    }
}
