package com.eventplatform.eventregistration.repository;

import com.eventplatform.eventregistration.entity.Event;
import com.eventplatform.eventregistration.enums.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByStatusAndIsDeletedFalse(EventStatus status);
    List<Event> findByOrganiserIdAndIsDeletedFalse(Long organiserId);
    List<Event> findByIsDeletedFalse();
}