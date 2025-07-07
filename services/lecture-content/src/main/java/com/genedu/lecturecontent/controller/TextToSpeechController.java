package com.genedu.lecturecontent.controller;

import com.genedu.lecturecontent.dto.TextToSpeechRequestDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.audio.speech.SpeechPrompt;
import org.springframework.ai.openai.audio.speech.SpeechResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@RequestMapping("/api/v1/lecture-contents")
public class TextToSpeechController {

    private static final Logger log = LoggerFactory.getLogger(TextToSpeechController.class);
    private final OpenAiAudioSpeechModel textToSpeechModel;

    public TextToSpeechController(
            @Qualifier("openAiAudioSpeechModel")
            OpenAiAudioSpeechModel textToSpeechModel) {
        this.textToSpeechModel = textToSpeechModel;
    }

    /**
     * Converts the provided text into an MP3 audio stream.
     * This endpoint is non-blocking and streams the audio data directly to the client.
     *
     * @param request The request body containing the text to convert.
     * @return A Mono emitting a ResponseEntity with the audio/mpeg content.
     */
    @PostMapping("/text-to-speech")
    public Mono<ResponseEntity<byte[]>> convertTextToSpeech(@RequestBody TextToSpeechRequestDTO request) {
        log.info("Received text-to-speech request.");

        // Wrap the blocking AI call in a Mono and run it on a dedicated thread pool.
        return Mono.fromCallable(() -> {
                    log.info("Calling text-to-speech model on a worker thread for text: '{}...'",
                            request.text().substring(0, Math.min(request.text().length(), 50)));

                    SpeechPrompt speechPrompt = new SpeechPrompt(request.text());
                    SpeechResponse response = textToSpeechModel.call(speechPrompt);
                    return response.getResult().getOutput();
                })
                .map(audioBytes -> {
                    // Build the HTTP response with the correct headers for an audio file.
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.valueOf("audio/mpeg"));
                    headers.setContentDispositionFormData("attachment", "speech.mp3");
                    headers.setContentLength(audioBytes.length);

                    log.info("Successfully generated {} bytes of audio data.", audioBytes.length);
                    return ResponseEntity.ok().headers(headers).body(audioBytes);
                })
                .subscribeOn(Schedulers.boundedElastic()) // Ensures the blocking call doesn't clog the main threads.
                .contextCapture(); // Propagates security context if needed.
    }
}
