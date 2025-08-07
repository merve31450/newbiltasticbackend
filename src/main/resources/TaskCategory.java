package org.u2soft.billtasticbackend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "task_categories")
public class TaskCategory {

    public enum Type { VIEW, TAG }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    private Type type;

    // Getters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public Type getType() { return type; }

    // Setters
    public void setTitle(String t) { this.title = t; }
    public void setType(Type t)  { this.type = t; }
}
