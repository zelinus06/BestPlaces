package com.bestplaces.Entity;

import com.bestplaces.Enums.Type;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
public class ExpectedResult {
    @Id
    @Setter
    private Long id;
    @Setter
    @OneToOne
    @JoinColumn(name = "id", nullable = false)
    private User user;
    @Setter
    private int minArea;
    @Setter
    private int maxArea;
    @Setter
    private int minPrice;
    @Setter
    private int maxPrice;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;
}
