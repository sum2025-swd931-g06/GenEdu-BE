package com.genedu.notification;

import com.genedu.notification.NotificationPort.NotificationRes;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repository;

    @Override
    public List<NotificationEntity> getAllNotifications() {
        return repository.findAll();
    }

    @Override
    public List<NotificationRes> getNotificationByUserId(String userId) {
        return repository.findByUserId(userId).stream()
            .map(NotificationRes::fromWithoutUserId)
            .toList();
    }

    @Override
    public void addNotification(NotificationEntity notification) {

        var existingUserId = "3f77c248-042e-4824-9d8f-c8b9ee17db17";

        var newNotification = NotificationEntity.builder()
            .title(notification.getTitle())
            .type(notification.getType())
            .description(notification.getDescription())
            .time(LocalDateTime.now())
            .userId(existingUserId)
            .iconName(notification.getIconName())
            .iconColorHex(notification.getIconColorHex())
            .build();

        repository.save(newNotification);
    }

    @Override
    public void markAsRead(Long id) {
        var notification = repository.findById(id).orElseThrow();
        if (!notification.isRead()) {
            notification.setRead(true);
            repository.save(notification);
        }
    }

    @Override
    public void sendNotification(String token) throws Exception {
        Notification notification = Notification.builder()
            .setTitle("Hello")
            .setBody("This is a test notification")
            .build();

        Message message = Message.builder()
            .setToken(token)
            .setNotification(notification)
            .build();

        var existingUserId = "3f77c248-042e-4824-9d8f-c8b9ee17db17";

        var newNotification = NotificationEntity.builder()
            .title(notification.getTitle())
            .type(notification.getType())
            .description(notification.getDescription())
            .time(LocalDateTime.now())
            .userId(existingUserId)
            .iconName(notification.getIconName())
            .iconColorHex(notification.getIconColorHex())
            .build();

        repository.save(newNotification);

        String response = FirebaseMessaging.getInstance().send(message);
        System.out.println("Sent! Response: " + response);
    }

}
