package com.genedu.commonlibrary.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SlideNarrationEvent implements Serializable {
    private UUID projectId;
    private UUID lectureContentId;
    private UUID finalizeLectureId;
    private Long slideFileId;
    private List<SlideNarration> slideNarrations;
    private String jwtToken;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SlideNarration implements Serializable {
        private UUID slideId;
        private Integer orderNumber;
        private String narrationScript;
    }
}
