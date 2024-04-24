package com.bestplaces.Dto;

import com.bestplaces.Entity.RentalPost;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class PostDto {
    @Getter
    @Setter
    private RentalPost post;
    @Getter
    @Setter
    private List<String> images;

    public PostDto(RentalPost post, List<String> images) {
        this.post = post;
        this.images = images;
    }
}
