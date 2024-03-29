package com.bestplaces.Repository;

import com.bestplaces.Entity.RentalPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<RentalPost, Long> {
}
