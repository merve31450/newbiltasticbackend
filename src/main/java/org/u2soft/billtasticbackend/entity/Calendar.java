package org.u2soft.billtasticbackend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "calendars") // "calenders" tablosu veritabanında kullanılacak
public class Calendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String notes;
    private LocalDateTime eventDate;

    @ManyToOne
    @JoinColumn(name = "parent_calendar_id")
    private Calendar parentCalendar;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDateTime eventDate) {
        this.eventDate = eventDate;
    }

    public Calendar getParentCalendar() {
        return parentCalendar;
    }

    public void setParentCalendar(Calendar parentCalendar) {
        this.parentCalendar = parentCalendar;
    }
}
