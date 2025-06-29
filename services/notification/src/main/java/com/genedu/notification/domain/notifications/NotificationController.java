package com.genedu.notification.domain.notifications;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/page")
    public Page<NotificationEntity> getAllPageable(
        @ParameterObject
        @Parameter(
            description = "Pagination information",
            schema = @Schema(
                defaultValue = "{\"page\":0,\"size\":2,\"sort\":[\"id,desc\"]}"
            )
        )
        @PageableDefault(size = 20) Pageable pageable
    ) {
        return service.getAllNotifications(pageable);
    }

    @GetMapping("/{email}")
    @Operation(summary = "Get notifications by user ID",
        description = "Fetches all notifications for a specific user based on their user ID.")
    public ResponseEntity<?> getByUserId(
        @PathVariable @Schema(defaultValue = "hoangclw@gmail.com") String email) {
        List<NotificationPort.NotificationRes> notifications = service.getNotificationByEmail(
            email);
        return ResponseEntity.ok(notifications);
    }


    @PostMapping("")
    @Operation(summary = "[DEBUG] Add a new notification",
        description = "Creates a new notification and saves it to the database.")
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
    @Deprecated
    public ResponseEntity<?> send() throws Exception {
        String token = "fHbU6NCiTkWw-JqPNWI5Y7:APA91bH4zrWYQrm0xXzputlOCx-2OI8DoA8EntQ9haXGu8aIpBKDpfyoMPRRR7hmjuzU8lFHhDoz_P68KwqgChvD3Hwwyn3A2lde18MnUBgn28SBqTVrGVU"; // 👈 Replace this with your real device token
        service.sendNotification(token);
        return ResponseEntity.ok("Sent!");
    }

    @PostMapping("/send-to-user")
    @Operation(summary = "Send notification to all user devices",
        description = "Send push notification to all registered devices of a user and save to database")
    public ResponseEntity<?> sendToUser(
        @RequestParam @Schema(defaultValue = "hoangclw@gmail.com") String email,
        @RequestParam @Schema(defaultValue = "Test Notification") String title,
        @RequestParam @Schema(defaultValue = "This is a test notification sent to all your devices") String body,
        @RequestParam(defaultValue = "INFO") NotificationEntity.NotificationType type
    ) throws Exception {
        service.sendNotificationToUser(email, title, body, type);
        return ResponseEntity.ok("Notification sent to all devices for user: " + email);
    }
}
