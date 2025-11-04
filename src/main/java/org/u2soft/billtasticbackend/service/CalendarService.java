package org.u2soft.billtasticbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.u2soft.billtasticbackend.dto.CalendarDto;
import org.u2soft.billtasticbackend.entity.Calendar;
import org.u2soft.billtasticbackend.entity.User;
import org.u2soft.billtasticbackend.mapper.CalendarMapper;
import org.u2soft.billtasticbackend.repository.CalendarRepository;
import org.u2soft.billtasticbackend.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CalendarService {

    private final CalendarRepository repo;
    private final UserRepository userRepo;

    /* =====================================================
       1️⃣ TÜM KULLANICILAR (admin içindir)
       ===================================================== */
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

    /* =====================================================
       2️⃣ KULLANICIYA GÖRE TAKVİM METOTLARI
       ===================================================== */

    public List<CalendarDto> getAllCalendarsByUser(String email) {
        User user = Optional.ofNullable(userRepo.findByEmail(email))
                .orElseThrow(() -> new RuntimeException("User not found"));

        return repo.findByUser(user)
                .stream()
                .map(CalendarMapper::toDto)
                .toList();
    }

    public Optional<CalendarDto> findByIdAndUser(Long id, String email) {
        User user = Optional.ofNullable(userRepo.findByEmail(email))
                .orElseThrow(() -> new RuntimeException("User not found"));

        return repo.findByIdAndUser(id, user)
                .map(CalendarMapper::toDto);
    }

    public CalendarDto createCalendarForUser(CalendarDto dto, String email) {
        User user = Optional.ofNullable(userRepo.findByEmail(email))
                .orElseThrow(() -> new RuntimeException("User not found"));

        Calendar entity = CalendarMapper.toEntity(dto);
        entity.setUser(user);

        Calendar saved = repo.save(entity);
        return CalendarMapper.toDto(saved);
    }

    public Optional<CalendarDto> updateCalendarForUser(CalendarDto dto, String email) {
        User user = Optional.ofNullable(userRepo.findByEmail(email))
                .orElseThrow(() -> new RuntimeException("User not found"));

        return repo.findByIdAndUser(dto.getId(), user)
                .map(existing -> {
                    existing.setTitle(dto.getTitle());
                    existing.setNotes(dto.getNotes());
                    existing.setEventDate(dto.getEventDate());
                    return CalendarMapper.toDto(repo.save(existing));
                });
    }

    public boolean deleteCalendarForUser(Long id, String email) {
        User user = Optional.ofNullable(userRepo.findByEmail(email))
                .orElseThrow(() -> new RuntimeException("User not found"));

        return repo.findByIdAndUser(id, user)
                .map(event -> {
                    repo.delete(event);
                    return true;
                })
                .orElse(false);
    }
}
