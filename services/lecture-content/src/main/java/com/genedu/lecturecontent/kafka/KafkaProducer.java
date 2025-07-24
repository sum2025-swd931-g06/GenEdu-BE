package com.genedu.lecturecontent.kafka;

import com.genedu.commonlibrary.kafka.dto.LectureVideoGenerateEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {
    private final KafkaTemplate<String, LectureVideoGenerateEvent> lectureVideoGenerateTemplate;


    public KafkaProducer(KafkaTemplate<String, LectureVideoGenerateEvent> lectureVideoGenerateTemplate) {
        this.lectureVideoGenerateTemplate = lectureVideoGenerateTemplate;
    }

    public void sendLectureVideoGenerateEvent(LectureVideoGenerateEvent event) {
        try {
            lectureVideoGenerateTemplate.send("lecture-video-generate-events", event.getLectureContentId().toString(), event);
            System.out.println("Sent LectureVideoGenerateEvent to Kafka: " + event);
        } catch (Exception e) {
            // Handle the exception
            System.err.println("Error sending LectureVideoGenerateEvent to Kafka: " + e.getMessage());
        }
    }
}
