package com.genedu.notification.domain.token;

import com.genedu.notification.domain.token.FcmTokenPort.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user-device-tokens")
@RequiredArgsConstructor
public class UserDeviceTokenController {

    private final FcmTokenServiceImpl service;

    @GetMapping
    public Page<UserDeviceTokenDto> getAll(@PageableDefault(size = 20) Pageable pageable) {
        return service.getAll(pageable);
    }

    @GetMapping("/{id}")
    public UserDeviceTokenDto getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public ResponseEntity<UserDeviceTokenDto> create(@RequestBody CreateUserDeviceTokenReq req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(req));
    }

    @PatchMapping("/{id}")
    public UserDeviceTokenDto update(@PathVariable Long id, @RequestBody UpdateUserDeviceTokenReq req) {
        return service.update(id, req);
    }

}
