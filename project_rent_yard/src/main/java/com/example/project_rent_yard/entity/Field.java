package com.example.project_rent_yard.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="field")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Field {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100, nullable = false, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    private FieldType fieldType;

    private double price;

    @Enumerated(EnumType.STRING)
    private FieldStatus status;

    private boolean isDeleted=false;

    public enum FieldType {
         Field5, Field7
    }

    public enum FieldStatus {
        AVAILABLE, MAINTENANCE
    }
}
