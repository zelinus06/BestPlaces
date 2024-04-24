package com.bestplaces.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
public class ListLocation {
    @Id
    @Setter
    private Long id;
    @Setter
    @OneToOne
    @JoinColumn(name = "id", nullable = false)
    private User user;
    @Setter
    private double latitude;
    @Setter
    private double longtitude;
}
