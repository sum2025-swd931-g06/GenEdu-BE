package com.genedu.notification.domain.token;

import com.genedu.commonlibrary.model.AbstractTimeAuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "user_device_tokens")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDeviceTokenEntity extends AbstractTimeAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "deviceId", nullable = false)
    private String deviceId; // e.g. Android ID, or Firebase Installation ID

    @Column(name = "fcmToken", nullable = false, unique = true, length = 512)
    private String fcmToken;

    @Column(name = "deviceName")
    private String deviceName; // optional (Samsung Galaxy A52, etc.)

    @Column(name = "platform") // optional (ANDROID, IOS)
    private String platform;
}

