package org.u2soft.billtasticbackend.mapper;

import org.u2soft.billtasticbackend.dto.CalendarDto;
import org.u2soft.billtasticbackend.entity.Calendar;

public final class CalendarMapper {

    private CalendarMapper() {}   // util sınıf

    public static CalendarDto toDto(Calendar entity) {
        if (entity == null) return null;
        CalendarDto dto = new CalendarDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setNotes(entity.getNotes());
        dto.setEventDate(entity.getEventDate());
        return dto;
    }

    public static Calendar toEntity(CalendarDto dto) {
        if (dto == null) return null;
        Calendar entity = new Calendar();
        entity.setId(dto.getId());
        entity.setTitle(dto.getTitle());
        entity.setNotes(dto.getNotes());
        entity.setEventDate(dto.getEventDate());
        return entity;
    }
}
