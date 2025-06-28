package com.genedu.notification;

import java.util.List;

public interface NotificationService{

     List<NotificationEntity> getAllNotifications();
     List<NotificationPort.NotificationRes> getNotificationByUserId(String userId);
     void addNotification(NotificationEntity notificationEntity);
     void markAsRead(Long id);
     void sendNotification(String token) throws Exception;

}
