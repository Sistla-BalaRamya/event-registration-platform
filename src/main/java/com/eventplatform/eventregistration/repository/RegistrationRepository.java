package com.eventplatform.eventregistration.repository;

import com.eventplatform.eventregistration.entity.Event;
import com.eventplatform.eventregistration.entity.Registration;
import com.eventplatform.eventregistration.entity.User;
import com.eventplatform.eventregistration.enums.RegistrationStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    boolean existsByUserAndEventAndStatusNot(
            User user, Event event, RegistrationStatus status);

    long countByEventAndStatus(Event event, RegistrationStatus status);

    List<Registration> findByEventAndStatusOrderByRegisteredAtAsc(
            Event event, RegistrationStatus status);

    Optional<Registration> findByUserAndEventAndStatus(
            User user, Event event, RegistrationStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM Registration r WHERE r.event = :event AND r.status = :status")
    List<Registration> findByEventAndStatusWithLock(
            Event event, RegistrationStatus status);
}