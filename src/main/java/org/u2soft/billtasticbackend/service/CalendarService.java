package org.u2soft.billtasticbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.u2soft.billtasticbackend.entity.Calendar;
import org.u2soft.billtasticbackend.repository.CalendarRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor    // repo'yu ctor-enject eder
public class CalendarService {

    private final CalendarRepository repo;


    public List<Calendar> getAllCalendars() {
        return repo.findAll();
    }

    public Optional<Calendar> findById(Long id) {
        return repo.findById(id);
    }


    public Calendar save(Calendar calendar) {
        return repo.save(calendar);
    }

    public boolean exists(Long id) {
        return repo.existsById(id);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
