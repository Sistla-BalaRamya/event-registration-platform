package com.eventplatform.eventregistration.controller;

import com.eventplatform.eventregistration.dto.RegistrationResponse;
import com.eventplatform.eventregistration.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/registrations")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping("/event/{eventId}")
    public ResponseEntity<RegistrationResponse> register(
            @PathVariable Long eventId,
            @AuthenticationPrincipal String email) {
        RegistrationResponse response =
                registrationService.registerForEvent(eventId, email);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{registrationId}")
    public ResponseEntity<RegistrationResponse> cancel(
            @PathVariable Long registrationId,
            @AuthenticationPrincipal String email) {
        RegistrationResponse response =
                registrationService.cancelRegistration(registrationId, email);
        return ResponseEntity.ok(response);
    }
}