// src/main/java/org/u2soft/billtasticbackend/entity/TaskCategory.java
package org.u2soft.billtasticbackend.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskCategory {


    public enum Type { WORK, PERSONAL, STUDY }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Type type;

    private String name;
    private String color;
}
