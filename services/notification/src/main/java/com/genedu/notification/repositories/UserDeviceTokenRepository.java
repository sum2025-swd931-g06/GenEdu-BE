package com.genedu.notification.repositories;

import com.genedu.notification.domain.token.UserDeviceTokenEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserDeviceTokenRepository extends JpaRepository<UserDeviceTokenEntity, Long> {
    Optional<UserDeviceTokenEntity> findByDeviceId(String deviceId);
    
    @Query("SELECT u FROM user_device_tokens u WHERE u.email = :email")
    List<UserDeviceTokenEntity> findAllByEmail(@Param("email") String email);
    
    Optional<UserDeviceTokenEntity> findByEmailAndDeviceId(String email, String deviceId);
}
