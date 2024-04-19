package com.bestplaces.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ImagePath")
public class ImageUrl {
    @Id
    @Getter
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private long IdImage;
    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "id_post", nullable = false)
    private RentalPost id_post;
    @Getter
    @Setter
    private String imageUrl;
}
