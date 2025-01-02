package org.u2soft.billtasticbackend.repository;

import org.u2soft.billtasticbackend.entity.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.time.LocalDate;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Long> {
    // Başlık ile takvim arama
    Calendar findByTitle(String title);

    // Tarihe göre takvim arama
    List<Calendar> findByEventDate(LocalDateTime eventDate);

    // Parent takvimi ile arama
    List<Calendar> findByParentCalendar(Calendar parentCalendar);
}
