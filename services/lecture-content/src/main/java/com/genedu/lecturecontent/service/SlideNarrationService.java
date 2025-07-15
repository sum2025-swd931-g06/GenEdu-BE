package com.genedu.lecturecontent.service;

import com.genedu.commonlibrary.kafka.dto.SlideNarrationEvent;
import com.genedu.commonlibrary.webclient.CustomMultipartFile;
import com.genedu.commonlibrary.webclient.dto.SlideNarrationAudioDownloadDTO;
import com.genedu.commonlibrary.webclient.dto.SlideNarrationAudioUploadDTO;
import com.genedu.lecturecontent.controller.SlideContentController;
import com.genedu.lecturecontent.webclient.ProjectWebClientService;
import com.genedu.lecturecontent.webclient.SlideNarrationMediaWebClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.audio.speech.SpeechPrompt;
import org.springframework.ai.openai.audio.speech.SpeechResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class SlideNarrationService {
    private final SlideNarrationMediaWebClientService slideNarrationMediaWebClientService;

    private static final Logger log = LoggerFactory.getLogger(SlideNarrationService.class);
    private final OpenAiAudioSpeechModel textToSpeechModel;
    private final ProjectWebClientService projectWebClientService;

    public SlideNarrationService(SlideNarrationMediaWebClientService slideNarrationMediaWebClientService, OpenAiAudioSpeechModel textToSpeechModel, SlideContentController slideContentController, ProjectWebClientService projectWebClientService) {
        this.slideNarrationMediaWebClientService = slideNarrationMediaWebClientService;
        this.textToSpeechModel = textToSpeechModel;
        this.projectWebClientService = projectWebClientService;
    }

    public void slideNarration(SlideNarrationEvent slideNarrationEvent) {
        List<SlideNarrationEvent.SlideNarration> slideNarrations = slideNarrationEvent.getSlideNarrations();
        for (SlideNarrationEvent.SlideNarration slideNarration : slideNarrations) {
            try {
                log.info("Generating audio for slideId: {}", slideNarration.getSlideId());
                String narrationScript = slideNarration.getNarrationScript();

                SpeechPrompt speechPrompt = new SpeechPrompt(narrationScript);
                SpeechResponse speechResponse = textToSpeechModel.call(speechPrompt);
                byte[] audioBytes = speechResponse.getResult().getOutput();

                if (audioBytes != null && audioBytes.length > 0) {
                    log.info("Successfully generated {} bytes of audio for slideId: {}", audioBytes.length, slideNarration.getSlideId());



                    // Upload the generated audio directly to the media service.
                    SlideNarrationAudioUploadDTO narrationAudioUploadDTO = new SlideNarrationAudioUploadDTO(
                            slideNarrationEvent.getProjectId().toString(),
                            slideNarrationEvent.getLectureContentId().toString(),
                            slideNarration.getSlideId().toString(),
                            audioBytes
                    );

                    SlideNarrationAudioDownloadDTO slideNarrationAudioDownloadDTO = slideNarrationMediaWebClientService.uploadNarrationFile(narrationAudioUploadDTO, slideNarrationEvent.getJwtToken());

                    projectWebClientService.updateSlideContentNarration(
                            slideNarration.getSlideId(),
                            slideNarrationAudioDownloadDTO.getId(),
                            slideNarrationEvent.getJwtToken()
                    );

                    log.info("Successfully uploaded narration for slideId: {}", slideNarration.getSlideId());
                } else {
                    log.warn("Failed to generate audio for slideId: {}", slideNarration.getSlideId());
                }
            } catch (Exception e) {
                // This ensures that a failure for one slide doesn't stop the others.
                log.error("An unexpected error occurred while processing narration for slideId: {}", slideNarration.getSlideId(), e);
            }
        }
    }

}
