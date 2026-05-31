package com.eventplatform.eventregistration.controller;

import com.eventplatform.eventregistration.dto.EventRequest;
import com.eventplatform.eventregistration.dto.EventResponse;
import com.eventplatform.eventregistration.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<EventResponse> createEvent(
            @Valid @RequestBody EventRequest request,
            @AuthenticationPrincipal String email) {
        EventResponse response = eventService.createEvent(request, email);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<EventResponse>> getAllEvents() {
        List<EventResponse> events = eventService.getAllPublishedEvents();
        return ResponseEntity.ok(events);
    }

    @PatchMapping("/{id}/publish")
    public ResponseEntity<EventResponse> publishEvent(
            @PathVariable Long id,
            @AuthenticationPrincipal String email) {
        EventResponse response = eventService.publishEvent(id, email);
        return ResponseEntity.ok(response);
    }
}