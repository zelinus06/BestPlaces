package com.bestplaces.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
public class ListLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Setter
    private double latitude;
    @Setter
    private double longtitude;
}
