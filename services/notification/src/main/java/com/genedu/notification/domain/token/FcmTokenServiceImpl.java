package com.genedu.notification.domain.token;

import com.genedu.notification.domain.token.FcmTokenPort.CreateUserDeviceTokenReq;
import com.genedu.notification.domain.token.FcmTokenPort.UpdateUserDeviceTokenReq;
import com.genedu.notification.domain.token.FcmTokenPort.UserDeviceTokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FcmTokenServiceImpl implements FcmTokenService {


    private final UserDeviceTokenRepository repository;

    @Override
    public Page<UserDeviceTokenDto> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toDto);
    }

    @Override
    public UserDeviceTokenDto getById(Long id) {
        return toDto(repository.findById(id).orElseThrow(() -> new RuntimeException("Not found")));
    }

    @Override
    public UserDeviceTokenDto create(CreateUserDeviceTokenReq req) {
        var entity = UserDeviceTokenEntity.builder()
            .userId(req.userId())
            .deviceId(req.deviceId())
            .fcmToken(req.fcmToken())
            .deviceName(req.deviceName())
            .platform(req.platform())
            .build();
        return toDto(repository.save(entity));
    }

    @Override
    public UserDeviceTokenDto update(Long id, UpdateUserDeviceTokenReq req) {
        var entity = repository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));

        if (req.fcmToken() != null) entity.setFcmToken(req.fcmToken());
        if (req.deviceName() != null) entity.setDeviceName(req.deviceName());
        if (req.platform() != null) entity.setPlatform(req.platform());

        return toDto(repository.save(entity));
    }

    private UserDeviceTokenDto toDto(UserDeviceTokenEntity entity) {
        return new UserDeviceTokenDto(
            entity.getId(),
            entity.getUserId(),
            entity.getDeviceId(),
            entity.getFcmToken(),
            entity.getDeviceName(),
            entity.getPlatform()
        );
    }
}
