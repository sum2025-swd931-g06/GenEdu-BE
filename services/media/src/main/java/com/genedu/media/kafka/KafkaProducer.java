package com.genedu.media.kafka;

import com.genedu.commonlibrary.kafka.dto.LectureVideoGenerateEvent;
import com.genedu.commonlibrary.kafka.dto.NotificationEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {
    private final KafkaTemplate<String, NotificationEvent> notificationEventKafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, NotificationEvent> notificationEventKafkaTemplate) {
        this.notificationEventKafkaTemplate = notificationEventKafkaTemplate;
    }

    public void sendNotificationEvent(NotificationEvent event) {
        try {
            notificationEventKafkaTemplate.send("notification-events", event.getUserId(), event);
            System.out.println("Sent NotificationEvent to Kafka: " + event);
        } catch (Exception e) {
            // Handle the exception
            System.err.println("Error sending NotificationEvent to Kafka: " + e.getMessage());
        }
    }
}
