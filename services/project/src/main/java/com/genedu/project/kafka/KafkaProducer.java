package com.genedu.project.kafka;

import com.genedu.commonlibrary.kafka.dto.SlideNarrationEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {
    private final KafkaTemplate<String, SlideNarrationEvent> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, SlideNarrationEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendSlideNarrationEvent(SlideNarrationEvent event) {
        // Send the event to the Kafka topic
        try {
            kafkaTemplate.send("slide-narration-events", event.getLectureContentId().toString(), event);
            System.out.println("Sent SlideNarrationEvent to Kafka: " + event);
        } catch (Exception e) {
            // Handle the exception
            System.err.println("Error sending SlideNarrationEvent to Kafka: " + e.getMessage());
        }
    }

}
