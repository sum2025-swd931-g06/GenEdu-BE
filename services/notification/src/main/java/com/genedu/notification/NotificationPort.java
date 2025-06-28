package com.genedu.notification;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.genedu.notification.NotificationEntity.NotificationColor;
import com.genedu.notification.NotificationEntity.NotificationIcon;
import com.genedu.notification.NotificationEntity.NotificationType;
import java.time.LocalDateTime;

public interface NotificationPort {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    record NotificationRes(
        Long id,
        NotificationType type,
        String title,
        String description,
        LocalDateTime time,
        boolean isRead,
        NotificationIcon iconName,
        NotificationColor iconColorHex,
        String userId
    ){
        public static NotificationRes from(NotificationEntity notificationEntity) {
            return new NotificationRes(
                notificationEntity.getId(),
                notificationEntity.getType(),
                notificationEntity.getTitle(),
                notificationEntity.getDescription(),
                notificationEntity.getTime(),
                notificationEntity.isRead(),
                notificationEntity.getIconName(),
                notificationEntity.getIconColorHex(),
                notificationEntity.getUserId()
            );
        }

        public static NotificationRes fromWithoutUserId(NotificationEntity notificationEntity) {
            return new NotificationRes(
                notificationEntity.getId(),
                notificationEntity.getType(),
                notificationEntity.getTitle(),
                notificationEntity.getDescription(),
                notificationEntity.getTime(),
                notificationEntity.isRead(),
                notificationEntity.getIconName(),
                notificationEntity.getIconColorHex(),
                null // explicitly null for userId
            );
        }

    }

}
