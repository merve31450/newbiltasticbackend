package org.u2soft.billtasticbackend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String task;

    private String collection;

    private String description;

    @Enumerated(EnumType.STRING)
    private Badge badge;

    private LocalDateTime remindAt;

    @Builder.Default
    @Column(nullable = false)
    private boolean completed = false;

    @ManyToOne
    private TaskCategory category;

    public enum Badge {
        PENDING, COMPLETED, ARCHIVED
    }
}
