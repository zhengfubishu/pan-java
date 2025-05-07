package com.cloudfile.component.xfyun.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SummaryResp extends ResponseMsg{
    private Datas data;

    @Data
    public static class Datas{
        private String summaryStatus;
        private String summary;
    }
}
