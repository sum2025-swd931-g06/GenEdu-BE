package com.genedu.project.service.impl;

import com.genedu.project.service.SlideContentService;
import com.genedu.project.webclient.LectureMediaWebClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SlideContentServiceImpl implements SlideContentService {
    private final LectureMediaWebClientService lectureMediaWebClientService;

    @Override
    public String getSlideContentNarrationFileUrl(Long fileId) {
        return lectureMediaWebClientService.getSlideNarrationAudioFileUrlByFileId(fileId);
    }
}
