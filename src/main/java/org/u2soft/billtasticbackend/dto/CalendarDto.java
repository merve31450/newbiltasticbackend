package org.u2soft.billtasticbackend.dto;


import java.time.LocalDateTime;

public class CalendarDto {
    private String title;

    private String notes;
    private LocalDateTime eventDate;

    // Getter ve Setter'lar
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public LocalDateTime getEventDate() { return eventDate; }
    public void setEventDate(LocalDateTime eventDate) { this.eventDate = eventDate; }

}
