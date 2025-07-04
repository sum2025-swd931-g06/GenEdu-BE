package com.genedu.notification.domain.token;

public interface FcmTokenPort {

    record UserDeviceTokenDto(
        Long id,
        String email,
        String deviceId,
        String fcmToken,
        String deviceName,
        String platform
    ) {}

    record CreateUserDeviceTokenReq(
        String email,
        String deviceId,
        String fcmToken,
        String deviceName,
        String platform
    ) {}

    record UpdateUserDeviceTokenReq(
        String fcmToken,
        String deviceName,
        String platform
    ) {}

}
