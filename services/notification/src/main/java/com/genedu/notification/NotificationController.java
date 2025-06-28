package com.genedu.notification;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Tag(name = "notifications", description = "Notification API")
public class NotificationController {

    private final NotificationService service;

    @GetMapping
    public List<NotificationEntity> getAll() {
        return service.getAllNotifications();
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get notifications by user ID",
            description = "Fetches all notifications for a specific user based on their user ID.")
    public ResponseEntity<?> getByUserId(@PathVariable String userId) {
        List<NotificationPort.NotificationRes> notifications = service.getNotificationByUserId(userId);
        return ResponseEntity.ok(notifications);
    }


    @PostMapping("")
    @Operation(summary = "Create a new notification")
    public ResponseEntity<?> create(
        @Valid @RequestBody NotificationEntity notificationEntity

    ) {
        service.addNotification(notificationEntity);
        return ResponseEntity.ok("Notification created successfully");
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(@PathVariable Long id) {
        service.markAsRead(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/send")
    public ResponseEntity<?> send() throws Exception {
        String token = "fHbU6NCiTkWw-JqPNWI5Y7:APA91bH4zrWYQrm0xXzputlOCx-2OI8DoA8EntQ9haXGu8aIpBKDpfyoMPRRR7hmjuzU8lFHhDoz_P68KwqgChvD3Hwwyn3A2lde18MnUBgn28SBqTVrGVU"; // 👈 Replace this with your real device token
        service.sendNotification(token);
        return ResponseEntity.ok("Sent!");
    }
}
