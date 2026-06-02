package com.eventplatform.eventregistration.dto;

import com.eventplatform.eventregistration.enums.RegistrationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationResponse {
    private Long id;
    private String eventTitle;
    private String userName;
    private RegistrationStatus status;
    private LocalDateTime registeredAt;
}