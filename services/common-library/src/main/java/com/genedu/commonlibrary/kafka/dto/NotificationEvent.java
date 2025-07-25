package com.genedu.commonlibrary.kafka.dto;

import lombok.*;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEvent implements Serializable {
    private String userId;
    private String mail;
    private String title;
    private String body;
    private String type;
    private String jwtToken;
}
