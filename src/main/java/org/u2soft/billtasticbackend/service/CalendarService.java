package org.u2soft.billtasticbackend.service;

import org.u2soft.billtasticbackend.entity.Calendar;
import org.u2soft.billtasticbackend.repository.CalendarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalendarService {

    private final CalendarRepository calendarRepository;

    @Autowired
    public CalendarService(CalendarRepository calendarRepository) {
        this.calendarRepository = calendarRepository;
    }

    public List<Calendar> getAllCalendars() {
        return calendarRepository.findAll();
    }

    public Calendar createCalendar(Calendar calendar) {
        return calendarRepository.save(calendar);
    }

    public Calendar updateCalendar(Long id, Calendar calendar) {
        return calendarRepository.findById(id)
                .map(existingCalendar -> {
                    calendar.setId(id);
                    return calendarRepository.save(calendar);
                })
                .orElseThrow(() -> new IllegalArgumentException("Calendar not found with ID: " + id));
    }

    public void deleteCalendar(Long id) {
        if (!calendarRepository.existsById(id)) {
            throw new IllegalArgumentException("Calendar not found with ID: " + id);
        }
        calendarRepository.deleteById(id);
    }
}
