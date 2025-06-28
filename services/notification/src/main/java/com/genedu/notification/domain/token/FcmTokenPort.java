package com.genedu.notification.domain.token;

public interface FcmTokenPort {

    record UserDeviceTokenDto(
        Long id,
        String userId,
        String deviceId,
        String fcmToken,
        String deviceName,
        String platform
    ) {}

    public record CreateUserDeviceTokenReq(
        String userId,
        String deviceId,
        String fcmToken,
        String deviceName,
        String platform
    ) {}

    public record UpdateUserDeviceTokenReq(
        String fcmToken,
        String deviceName,
        String platform
    ) {}

}
