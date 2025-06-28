package com.genedu.notification.domain.notifications;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationService{

     List<NotificationEntity> getAllNotifications();
     Page<NotificationEntity> getAllNotifications(Pageable pageable);
     List<NotificationPort.NotificationRes> getNotificationByUserId(String userId);
     void addNotification(NotificationEntity notificationEntity);
     void markAsRead(Long id);
     void sendNotification(String token) throws Exception;

}
