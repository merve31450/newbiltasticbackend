package org.u2soft.billtasticbackend.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.u2soft.billtasticbackend.dto.CalendarDto;
import org.u2soft.billtasticbackend.mapper.CalendarMapper;
import org.u2soft.billtasticbackend.service.CalendarService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/calendars")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class CalendarController {

    private final CalendarService service;

    /* ---------- LISTE ---------- */
    @GetMapping
    public List<CalendarDto> findAll() {
        return service.getAllCalendars()
                .stream()
                .map(CalendarMapper::toDto)
                .toList();
    }

    /* ---------- TEK KAYIT ---------- */
    @GetMapping("/{id}")
    public ResponseEntity<CalendarDto> findOne(@PathVariable Long id) {
        return service.findById(id)
                .map(CalendarMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /* ---------- EKLE ---------- */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CalendarDto add(@RequestBody @Valid CalendarDto dto) {
        return CalendarMapper.toDto(service.save(CalendarMapper.toEntity(dto)));
    }

    /* ---------- GÜNCELLE ---------- */
    @PutMapping("/{id}")
    public ResponseEntity<CalendarDto> update(@PathVariable Long id,
                                              @RequestBody CalendarDto dto) {
        dto.setId(id);
        return service.exists(id)
                ? ResponseEntity.ok(
                CalendarMapper.toDto(service.save(CalendarMapper.toEntity(dto))))
                : ResponseEntity.notFound().build();
    }

    /* ---------- SİL ---------- */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!service.exists(id)) {
            return ResponseEntity.notFound().build();
        }
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
