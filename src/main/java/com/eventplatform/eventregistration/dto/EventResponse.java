package com.eventplatform.eventregistration.dto;

import com.eventplatform.eventregistration.enums.EventStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventResponse {
    private Long id;
    private String title;
    private String description;
    private String venue;
    private LocalDateTime eventDate;
    private Integer maxCapacity;
    private BigDecimal price;
    private EventStatus status;
    private String organiserName;
    private LocalDateTime createdAt;
}