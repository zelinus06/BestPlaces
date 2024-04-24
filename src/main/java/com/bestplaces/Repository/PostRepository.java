package com.bestplaces.Repository;

import com.bestplaces.Entity.ImageUrl;
import com.bestplaces.Entity.RentalPost;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<RentalPost, Long> {
//    @Query("SELECT p FROM RentalPost p WHERE p.userId.id = :userId AND p.imagepath IS NULL ")
//    RentalPost findByUserId(Long userId);
}
