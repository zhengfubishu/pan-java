package com.easypan.component.xfyun.dto;

import lombok.Data;

/**
 * ResponseMsg
 *
 **/
@Data
public class ResponseMsg {
    private boolean flag;
    private int code;
    private String desc;
    private String sid;

    public boolean success() {
        return code == 0;
    }
}