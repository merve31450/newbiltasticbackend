package org.u2soft.billtasticbackend.repository;

import org.u2soft.billtasticbackend.entity.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.u2soft.billtasticbackend.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Long> {
    List<Calendar> findByEventDateBetween(LocalDateTime start, LocalDateTime end);
    List<Calendar>findByUserId(Long userId);
    Optional<Calendar> findByIdAndUserId(Long id, Long userId);
    List<Calendar> findByUser(User user);
    Optional<Calendar> findByIdAndUser(Long id, User user);

}
