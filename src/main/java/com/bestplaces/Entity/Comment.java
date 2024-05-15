package com.bestplaces.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private RentalPost id_post;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User id_user;

    private String comment;
}
