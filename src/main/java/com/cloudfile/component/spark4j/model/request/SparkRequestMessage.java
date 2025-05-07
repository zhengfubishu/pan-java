package com.cloudfile.component.spark4j.model.request;

import com.cloudfile.component.spark4j.model.SparkMessage;

import java.io.Serializable;
import java.util.List;

/**
 * $.payload.message

 */
public class SparkRequestMessage implements Serializable {
    private static final long serialVersionUID = 6725091574720504980L;

    private List<SparkMessage> text;

    public SparkRequestMessage() {
    }

    public SparkRequestMessage(List<SparkMessage> text) {
        this.text = text;
    }

    public List<SparkMessage> getText() {
        return text;
    }

    public void setText(List<SparkMessage> text) {
        this.text = text;
    }
}
