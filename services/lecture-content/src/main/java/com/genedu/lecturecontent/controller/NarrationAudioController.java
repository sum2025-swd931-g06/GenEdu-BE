package com.genedu.lecturecontent.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/lecture-contents")
public class NarrationAudioController {
    private static final Logger log = LoggerFactory.getLogger(NarrationAudioController.class);
    private final OpenAiAudioSpeechModel textToSpeechModel;

    public NarrationAudioController(
            @Qualifier("openAiAudioSpeechModel")
            OpenAiAudioSpeechModel textToSpeechModel) {
        this.textToSpeechModel = textToSpeechModel;
    }

    @PostMapping("/narration-audio")
    public String generateNarrationAudio() {
        return "Narration audio generation is not yet implemented.";
    }
}
