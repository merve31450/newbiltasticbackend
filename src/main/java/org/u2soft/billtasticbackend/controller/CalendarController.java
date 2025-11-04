package org.u2soft.billtasticbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.u2soft.billtasticbackend.dto.CalendarDto;
import org.u2soft.billtasticbackend.service.CalendarService;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/calendars")
@CrossOrigin(origins = {
        "http://localhost:3000",
        "http://127.0.0.1:3000",
        "http://192.168.56.1:3000"
})
@RequiredArgsConstructor
public class CalendarController {

    private final CalendarService calendarService; // ✅ Tek tanım yeterli

    /* =====================================================
       1️⃣ KULLANICIYA GÖRE TAKVİM LİSTELEME
       ===================================================== */
    @GetMapping
    public ResponseEntity<List<CalendarDto>> getAllForCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); // ✅ "auth" ismini koruduk

        List<CalendarDto> calendars = calendarService.getAllCalendarsByUser(username);
        return ResponseEntity.ok(calendars);
    }

    /* =====================================================
       2️⃣ TEK KAYIT (sadece sahibine)
       ===================================================== */
    @GetMapping("/{id}")
    public ResponseEntity<CalendarDto> getOne(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        return calendarService.findByIdAndUser(id, username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /* =====================================================
       3️⃣ EKLE (kullanıcıya göre)
       ===================================================== */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CalendarDto create(@RequestBody @Valid CalendarDto dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        return calendarService.createCalendarForUser(dto, username);
    }

    /* =====================================================
       4️⃣ GÜNCELLE (sadece kendi etkinliği)
       ===================================================== */
    @PutMapping("/{id}")
    public ResponseEntity<CalendarDto> update(@PathVariable Long id,
                                              @RequestBody @Valid CalendarDto dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        dto.setId(id);
        return calendarService.updateCalendarForUser(dto, username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /* =====================================================
       5️⃣ SİL (sadece kendi etkinliği)
       ===================================================== */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        boolean deleted = calendarService.deleteCalendarForUser(id, username);
        return deleted
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
