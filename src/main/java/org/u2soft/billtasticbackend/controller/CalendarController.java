package org.u2soft.billtasticbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.u2soft.billtasticbackend.dto.CalendarDto;
import org.u2soft.billtasticbackend.entity.Calendar;
import org.u2soft.billtasticbackend.mapper.CalendarMapper;
import org.u2soft.billtasticbackend.service.CalendarService;

import java.util.List;

@RestController
@RequestMapping("/api/calendars")
public class CalendarController {

    private final CalendarService calendarService;

    @Autowired
    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    // Tüm takvimleri getiren metod
    @GetMapping
    public List<Calendar> getAllCalendars() {
        return calendarService.getAllCalendars();
    }



    // Yeni takvim ekleme metodu
    @PostMapping
    public ResponseEntity<CalendarDto> createCalendar(@RequestBody CalendarDto calendarDto) {
        Calendar calendar = CalendarMapper.toEntity(calendarDto);
        Calendar savedCalendar = calendarService.createCalendar(calendar);
        return new ResponseEntity<>(CalendarMapper.toDto(savedCalendar), HttpStatus.CREATED);
    }

    // Takvim güncelleme metodu
    @PutMapping("{id}")
    public ResponseEntity<CalendarDto> updateCalendar(@PathVariable Long id, @RequestBody CalendarDto calendarDto) {
        // DTO'yu Entity'ye dönüştür
        Calendar calendar = CalendarMapper.toEntity(calendarDto);

        // Takvimi güncelle
        Calendar updatedCalendar = calendarService.updateCalendar(id, calendar);

        // Güncellenmiş takvimi DTO'ya dönüştürüp geri döndür
        if (updatedCalendar != null) {
            return new ResponseEntity<>(CalendarMapper.toDto(updatedCalendar), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Eğer takvim bulunamazsa 404 döner
    }

    // Takvim silme metodu
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCalendar(@PathVariable Long id) {
        try {
            calendarService.deleteCalendar(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Başarılı olursa 204 döner
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Eğer takvim bulunamazsa 404 döner
        }
    } }