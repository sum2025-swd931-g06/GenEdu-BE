package com.genedu.notification.domain.notifications;

import com.genedu.commonlibrary.kafka.dto.NotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumer {
    NotificationService notificationService;

    @KafkaListener(topics = "notification-events", groupId = "notification-event-group")
    public void consumeSlideNarrationEvent(NotificationEvent message) {

        String email = message.getMail();
        String title = message.getTitle();
        String body = message.getBody();
        String type = message.getType();
        String jwtToken = message.getJwtToken();
        try {
            notificationService.sendNotificationToUser(email, title, body, NotificationEntity.NotificationType.valueOf(type));
            System.out.println("Consumed NotificationEvent from Kafka: " + message);
        } catch (Exception e) {
            // Handle the exception
            System.err.println("Error consuming NotificationEvent from Kafka: " + e.getMessage());
        }
    }
}
