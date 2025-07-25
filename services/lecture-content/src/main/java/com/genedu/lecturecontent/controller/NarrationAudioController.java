package com.genedu.lecturecontent.controller;

import com.genedu.commonlibrary.kafka.dto.SlideNarrationEvent;
import com.genedu.lecturecontent.service.SlideNarrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/lecture-contents")
public class NarrationAudioController {
    private static final Logger log = LoggerFactory.getLogger(NarrationAudioController.class);
    private final OpenAiAudioSpeechModel textToSpeechModel;
    private final SlideNarrationService slideNarrationService;

    public NarrationAudioController(
            @Qualifier("openAiAudioSpeechModel")
            OpenAiAudioSpeechModel textToSpeechModel, SlideNarrationService slideNarrationService) {
        this.textToSpeechModel = textToSpeechModel;
        this.slideNarrationService = slideNarrationService;
    }

    @PostMapping("/narration-audio")
    public ResponseEntity<SlideNarrationEvent> generateNarrationAudio(
            SlideNarrationEvent slideNarrationEvent
    ) {
        log.info("Received slide narration event for project ID: {}", slideNarrationEvent.getProjectId());

        // Call the service to handle the narration generation
        slideNarrationService.slideNarration(slideNarrationEvent);
        log.info("Successfully processed slide narration event for project ID: {}", slideNarrationEvent.getProjectId());
        return ResponseEntity.ok(slideNarrationEvent);
    }
}
