package org.u2soft.billtasticbackend.repository;

import org.u2soft.billtasticbackend.entity.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Long> {
    List<Calendar> findByEventDateBetween(LocalDateTime start, LocalDateTime end);
}
