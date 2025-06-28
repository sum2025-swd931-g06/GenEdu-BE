package com.genedu.notification.repositories;

import com.genedu.notification.domain.notifications.NotificationEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    List<NotificationEntity> findByUserId(String userId);

}
