package com.genedu.media.kafka;

import com.genedu.commonlibrary.kafka.dto.LectureVideoGenerateEvent;
import com.genedu.commonlibrary.kafka.dto.NotificationEvent;
import com.genedu.commonlibrary.utils.AuthenticationUtils;
import com.genedu.media.service.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class KafkaConsumer {
    private final VideoService videoService;
    private final KafkaProducer kafkaProducer;

    public KafkaConsumer(VideoService videoService, KafkaProducer kafkaProducer) {
        this.videoService = videoService;
        this.kafkaProducer = kafkaProducer;
    }

    @KafkaListener(topics = "lecture-video-generate-events", groupId = "lecture-content-service-group")
    public void consumeLectureVideoGenerateEvent( LectureVideoGenerateEvent message) {
        if (message.getSlideNarrationAudios() == null || message.getSlideNarrationAudios().isEmpty()) {
            log.warn("Received empty LectureVideoGenerateEvent {}", message);
            return;
        }
        log.info("Received LectureVideoGenerateEvent for lecture content ID: {}", message.getLectureContentId());
        boolean isSuccess = false;
        try {

            // The core logic is wrapped in a try-catch block.
            videoService.generateLectureVideo(
                    message.getProjectId().toString(),
                    message.getLectureContentId().toString(),
                    message.getFinalizeLectureId().toString(),
                    message.getSlideFileId(),
                    message.getSlideNarrationAudios(),
                    message.getJwtToken()
            );
            log.info("Successfully handed off video generation for lecture content ID: {} to async service.", message.getLectureContentId());
            isSuccess = true;
        } catch (IllegalArgumentException e) {
            // The offset will be committed because we are not re-throwing the exception.
            log.error(
                    "Invalid data in LectureVideoGenerateEvent for lecture content ID: {}. The message will not be retried. Error: {}",
                    message.getLectureContentId(),
                    e.getMessage()
            );
            // In a more advanced setup, you would send this message to a Dead-Letter Topic (DLT) for inspection.
        } catch (Exception e) {
            // By re-throwing the exception, we tell Spring Kafka to NOT commit the offset,
            // so the message will be retried on the next poll.
            log.error(
                    "A transient error occurred while processing lecture content ID: {}. The message will be retried.",
                    message.getLectureContentId(),
                    e
            );
            throw new RuntimeException("Failed to process LectureVideoGenerateEvent, triggering retry.", e);
        }
    }
}
