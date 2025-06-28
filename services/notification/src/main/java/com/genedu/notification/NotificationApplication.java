package com.genedu.notification;

import com.genedu.notification.NotificationEntity.NotificationColor;
import com.genedu.notification.NotificationEntity.NotificationIcon;
import com.genedu.notification.NotificationEntity.NotificationType;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@RequiredArgsConstructor
@ComponentScan(basePackages = {"com.genedu.notification", "com.genedu.commonlibrary"})
public class NotificationApplication implements CommandLineRunner {

    private final NotificationRepository notificationRepository;

    public static void main(String[] args) {
        SpringApplication.run(NotificationApplication.class, args);

//        JavaBrowserLauncher.openHomePage("http://localhost:8095/swagger-ui.html");

    }

    @Override
    public void run(String... args) {
        if (notificationRepository.findAll().isEmpty()) {
            NotificationEntity notificationEntity1 = NotificationEntity.builder()
                .type(NotificationType.INFO)
                .title("Welcome to the App!")
                .description("Thank you for joining GenEdu.")
                .time(LocalDateTime.now())
                .userId("3f77c248-042e-4824-9d8f-c8b9ee17db17")
                .isRead(false)
                .iconName(NotificationIcon.MESSAGE)
                .iconColorHex(NotificationColor.BLUE)
                .build();

            NotificationEntity notificationEntity2 = NotificationEntity.builder()
                .type(NotificationType.WARNING)
                .title("Account Inactivity")
                .description("You haven’t logged in for 7 days.")
                .time(LocalDateTime.now().minusDays(1))
                .isRead(false)
                .userId("3f77c248-042e-4824-9d8f-c8b9ee17db17")
                .iconName(NotificationIcon.ALERT)
                .iconColorHex(NotificationColor.YELLOW)
                .build();

            notificationRepository.saveAll(List.of(notificationEntity1, notificationEntity2));
        }
    }
}
