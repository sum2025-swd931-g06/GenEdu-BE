package com.genedu.project.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StreamingSlideResponse {
    private int slideId;
    private String slideType;
    private String content;

    public StreamingSlideResponse(int slideId, String slideType, String content) {
        this.slideId = slideId;
        this.slideType = slideType;
        this.content = content;
    }
}
