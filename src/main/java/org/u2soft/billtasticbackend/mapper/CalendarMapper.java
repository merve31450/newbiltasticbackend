package org.u2soft.billtasticbackend.mapper;

import org.u2soft.billtasticbackend.dto.CalendarDto;
import org.u2soft.billtasticbackend.entity.Calendar;

public class CalendarMapper {
    public static CalendarDto toDto(Calendar calendar) {
        CalendarDto dto = new CalendarDto();
        dto.setTitle(calendar.getTitle());

        dto.setNotes(calendar.getNotes());
        dto.setEventDate(calendar.getEventDate());
        return dto;
    }

    public static Calendar toEntity(CalendarDto dto) {
        Calendar calendar = new Calendar();
        calendar.setTitle(dto.getTitle());

        calendar.setNotes(dto.getNotes());
        calendar.setEventDate(dto.getEventDate());
        return calendar;
    }
}
