package com.genedu.notification;

import com.genedu.notification.domain.notifications.NotificationEntity;
import com.genedu.notification.domain.notifications.NotificationEntity.NotificationColor;
import com.genedu.notification.domain.notifications.NotificationEntity.NotificationIcon;
import com.genedu.notification.domain.notifications.NotificationEntity.NotificationType;
import com.genedu.notification.repositories.NotificationRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@RequiredArgsConstructor
@ComponentScan(basePackages = {"com.genedu.notification", "com.genedu.commonlibrary"})
@EnableJpaRepositories(basePackages = "com.genedu.notification.repositories")
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
                .read(true)
                .email("hoangclw@gmail.com")
                .iconName(NotificationIcon.MESSAGE)
                .iconColorHex(NotificationColor.BLUE)
                .build();

            NotificationEntity notificationEntity2 = NotificationEntity.builder()
                .type(NotificationType.WARNING)
                .title("Account Inactivity")
                .description("You haven’t logged in for 7 days.")
                .time(LocalDateTime.now().minusDays(1))
                .read(false)
                .email("hoangclw@gmail.com")
                .iconName(NotificationIcon.ALERT)
                .iconColorHex(NotificationColor.YELLOW)
                .build();

            notificationRepository.saveAll(List.of(notificationEntity1, notificationEntity2));
        }
    }
}
