package com.genedu.notification.domain.token;


import com.genedu.notification.domain.token.FcmTokenPort.CreateUserDeviceTokenReq;
import com.genedu.notification.domain.token.FcmTokenPort.UpdateUserDeviceTokenReq;
import com.genedu.notification.domain.token.FcmTokenPort.UserDeviceTokenDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FcmTokenService {
    Page<UserDeviceTokenDto> getAll(Pageable pageable);
    UserDeviceTokenDto getById(Long id);
    UserDeviceTokenDto create(CreateUserDeviceTokenReq req);
    UserDeviceTokenDto update(Long id, UpdateUserDeviceTokenReq req);
}
