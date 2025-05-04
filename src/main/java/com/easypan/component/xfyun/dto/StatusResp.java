package com.easypan.component.xfyun.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StatusResp extends ResponseMsg{
    private List<Datas> data;

    @Data
    public static class Datas{
        private String fileId;
        private String fileStatus;
    }
}