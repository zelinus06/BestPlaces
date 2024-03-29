package com.bestplaces.Service;

import com.bestplaces.Entity.RentalPost;
import com.bestplaces.Repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RentalPostService {

    private final PostRepository postRepository;

    @Autowired
    public RentalPostService(PostRepository rentalPostRepository) {
        this.postRepository = rentalPostRepository;
    }

    public RentalPost saveRentalPost(RentalPost rentalPost) {
        return postRepository.save(rentalPost);
    }
}

