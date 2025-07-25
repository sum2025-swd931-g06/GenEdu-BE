package com.genedu.lecturecontent.kafka;

import com.genedu.commonlibrary.kafka.dto.SlideNarrationEvent;
import com.genedu.lecturecontent.service.SlideNarrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class KafkaConsumer {
    private final SlideNarrationService slideNarrationService;

    public KafkaConsumer(SlideNarrationService slideNarrationService) {
        this.slideNarrationService = slideNarrationService;
    }

    @KafkaListener(topics = "slide-narration-events", groupId = "lecture-content-service-group")
    public void consumeSlideNarrationEvent(SlideNarrationEvent message) {
        if (message.getSlideNarrations() == null || message.getSlideNarrations().isEmpty()) {
            log.warn("Received empty SlideNarrationEvent: {}", message);
        }else {
            log.info("Received SlideNarrationEvent with {} narrations", message.getSlideNarrations().size());
            slideNarrationService.slideNarration(message);
        }
    }
}
