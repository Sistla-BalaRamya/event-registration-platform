package com.eventplatform.eventregistration.service;

import com.eventplatform.eventregistration.dto.RegistrationResponse;
import com.eventplatform.eventregistration.entity.Event;
import com.eventplatform.eventregistration.entity.Registration;
import com.eventplatform.eventregistration.entity.User;
import com.eventplatform.eventregistration.enums.EventStatus;
import com.eventplatform.eventregistration.enums.RegistrationStatus;
import com.eventplatform.eventregistration.repository.EventRepository;
import com.eventplatform.eventregistration.repository.RegistrationRepository;
import com.eventplatform.eventregistration.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Transactional
    public RegistrationResponse registerForEvent(Long eventId, String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if (event.getStatus() != EventStatus.PUBLISHED) {
            throw new RuntimeException("Event is not open for registration");
        }

        boolean alreadyRegistered = registrationRepository
                .existsByUserAndEventAndStatusNot(
                        user, event, RegistrationStatus.CANCELLED);

        if (alreadyRegistered) {
            throw new RuntimeException("Already registered for this event");
        }

        long confirmedCount = registrationRepository
                .countByEventAndStatus(event, RegistrationStatus.CONFIRMED);

        RegistrationStatus status;
        if (confirmedCount < event.getMaxCapacity()) {
            status = RegistrationStatus.CONFIRMED;
        } else {
            status = RegistrationStatus.WAITLISTED;
        }

        Registration registration = Registration.builder()
                .user(user)
                .event(event)
                .status(status)
                .build();

        Registration saved = registrationRepository.save(registration);

        return mapToResponse(saved);
    }

    @Transactional
    public RegistrationResponse cancelRegistration(
            Long registrationId, String userEmail) {

        Registration registration = registrationRepository
                .findById(registrationId)
                .orElseThrow(() -> new RuntimeException("Registration not found"));

        if (!registration.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Not authorized");
        }

        if (registration.getStatus() == RegistrationStatus.CANCELLED) {
            throw new RuntimeException("Already cancelled");
        }

        boolean wasConfirmed = registration.getStatus()
                == RegistrationStatus.CONFIRMED;

        registration.setStatus(RegistrationStatus.CANCELLED);
        registrationRepository.save(registration);

        if (wasConfirmed) {
            promoteFromWaitlist(registration.getEvent());
        }

        return mapToResponse(registration);
    }

    private void promoteFromWaitlist(Event event) {
        List<Registration> waitlisted = registrationRepository
                .findByEventAndStatusOrderByRegisteredAtAsc(
                        event, RegistrationStatus.WAITLISTED);

        if (!waitlisted.isEmpty()) {
            Registration first = waitlisted.get(0);
            first.setStatus(RegistrationStatus.CONFIRMED);
            registrationRepository.save(first);
        }
    }

    private RegistrationResponse mapToResponse(Registration r) {
        return RegistrationResponse.builder()
                .id(r.getId())
                .eventTitle(r.getEvent().getTitle())
                .userName(r.getUser().getName())
                .status(r.getStatus())
                .registeredAt(r.getRegisteredAt())
                .build();
    }
}