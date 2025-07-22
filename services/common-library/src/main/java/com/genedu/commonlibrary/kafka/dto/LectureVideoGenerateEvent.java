package com.genedu.commonlibrary.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LectureVideoGenerateEvent implements Serializable {
    private UUID projectId;
    private UUID lectureContentId;
    private UUID finalizeLectureId;
    private Long slideFileId;
    private Map<Integer, Long> slideNarrationAudios;
    private String jwtToken;
}
