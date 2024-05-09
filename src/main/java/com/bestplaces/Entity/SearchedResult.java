package com.bestplaces.Entity;

import com.bestplaces.Enums.Type;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
public class SearchedResult {
    @Id
    @Setter
    private Long id;
    @Setter
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Setter
    private int minArea;
    @Setter
    private int maxArea;
    @Setter
    private int minPrice;
    @Setter
    private int maxPrice;
    @Setter
    private String type;
}
