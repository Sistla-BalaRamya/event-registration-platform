package com.eventplatform.eventregistration.service;

import com.eventplatform.eventregistration.dto.EventRequest;
import com.eventplatform.eventregistration.dto.EventResponse;
import com.eventplatform.eventregistration.entity.Event;
import com.eventplatform.eventregistration.entity.User;
import com.eventplatform.eventregistration.enums.EventStatus;
import com.eventplatform.eventregistration.repository.EventRepository;
import com.eventplatform.eventregistration.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public EventResponse createEvent(EventRequest request, String organiserEmail) {
        User organiser = userRepository.findByEmail(organiserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Event event = Event.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .venue(request.getVenue())
                .eventDate(request.getEventDate())
                .maxCapacity(request.getMaxCapacity())
                .price(request.getPrice())
                .status(EventStatus.DRAFT)
                .organiser(organiser)
                .build();

        Event saved = eventRepository.save(event);
        return mapToResponse(saved);
    }

    public List<EventResponse> getAllPublishedEvents() {
        return eventRepository.findByStatusAndIsDeletedFalse(EventStatus.PUBLISHED)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<EventResponse> getAllEvents() {
        return eventRepository.findByIsDeletedFalse()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public EventResponse publishEvent(Long eventId, String organiserEmail) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if (!event.getOrganiser().getEmail().equals(organiserEmail)) {
            throw new RuntimeException("Not authorized");
        }

        event.setStatus(EventStatus.PUBLISHED);
        return mapToResponse(eventRepository.save(event));
    }

    private EventResponse mapToResponse(Event event) {
        return EventResponse.builder()
                .id(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .venue(event.getVenue())
                .eventDate(event.getEventDate())
                .maxCapacity(event.getMaxCapacity())
                .price(event.getPrice())
                .status(event.getStatus())
                .organiserName(event.getOrganiser().getName())
                .createdAt(event.getCreatedAt())
                .build();
    }
}