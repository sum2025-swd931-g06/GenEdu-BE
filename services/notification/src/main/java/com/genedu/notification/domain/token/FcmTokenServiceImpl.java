package com.genedu.notification.domain.token;

import com.genedu.notification.domain.token.FcmTokenPort.CreateUserDeviceTokenReq;
import com.genedu.notification.domain.token.FcmTokenPort.UpdateUserDeviceTokenReq;
import com.genedu.notification.domain.token.FcmTokenPort.UserDeviceTokenDto;
import com.genedu.notification.repositories.UserDeviceTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public UserDeviceTokenDto createOrUpdate(CreateUserDeviceTokenReq req) {
        // Try to find existing token for this user and device
        Optional<UserDeviceTokenEntity> existingToken = 
            repository.findByUserIdAndDeviceId(req.userId(), req.deviceId());
        
        if (existingToken.isPresent()) {
            // Update existing token
            var entity = existingToken.get();
            entity.setFcmToken(req.fcmToken());
            entity.setDeviceName(req.deviceName());
            entity.setPlatform(req.platform());
            return toDto(repository.save(entity));
        } else {
            // Create new token
            return create(req);
        }
    }

    @Override
    public List<String> getFcmTokensByUserId(String userId) {
        return repository.findAllByUserId(userId)
            .stream()
            .map(UserDeviceTokenEntity::getFcmToken)
            .collect(Collectors.toList());
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
