package com.genedu.project.kafka;

import com.genedu.commonlibrary.kafka.dto.LectureVideoGenerateEvent;
import com.genedu.commonlibrary.kafka.dto.SlideNarrationEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {
    private final KafkaTemplate<String, SlideNarrationEvent> slideNarrationTemplate;
    private final KafkaTemplate<String, LectureVideoGenerateEvent> lectureVideoGenerateTemplate;

    public KafkaProducer(KafkaTemplate<String, SlideNarrationEvent> slideNarrationTemplate, KafkaTemplate<String, LectureVideoGenerateEvent> lectureVideoGenerateTemplate) {
        this.slideNarrationTemplate = slideNarrationTemplate;
        this.lectureVideoGenerateTemplate = lectureVideoGenerateTemplate;
    }

    public void sendSlideNarrationEvent(SlideNarrationEvent event) {
        // Send the event to the Kafka topic
        try {
            slideNarrationTemplate.send("slide-narration-events", event.getLectureContentId().toString(), event);
            System.out.println("Sent SlideNarrationEvent to Kafka: " + event);
        } catch (Exception e) {
            // Handle the exception
            System.err.println("Error sending SlideNarrationEvent to Kafka: " + e.getMessage());
        }
    }

    public void sendLectureVideoGenerateEvent(LectureVideoGenerateEvent event) {
        // Send the event to the Kafka topic
        try {
            lectureVideoGenerateTemplate.send("lecture-video-generate-events", event.getLectureContentId().toString(), event);
            System.out.println("Sent LectureVideoGenerateEvent to Kafka: " + event);
        } catch (Exception e) {
            // Handle the exception
            System.err.println("Error sending LectureVideoGenerateEvent to Kafka: " + e.getMessage());
        }
    }
}
