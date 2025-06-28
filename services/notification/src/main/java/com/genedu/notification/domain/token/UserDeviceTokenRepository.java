package com.genedu.notification.domain.token;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDeviceTokenRepository extends JpaRepository<UserDeviceTokenEntity, Long> {
    Optional<UserDeviceTokenEntity> findByDeviceId(String deviceId);
}
