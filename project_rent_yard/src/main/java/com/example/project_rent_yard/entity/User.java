package com.example.project_rent_yard.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String phone;

    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    private CustomerType customerType;

    private double totalSpent;

    @Enumerated(EnumType.STRING)
    private Status status;

    private boolean isDeleted=false;

    public enum CustomerType {
        NORMAL, VIP
    }

    public enum Status {
        ACTIVE, BLOCKED
    }


}