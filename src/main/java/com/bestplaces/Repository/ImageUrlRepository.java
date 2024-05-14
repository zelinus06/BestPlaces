package com.bestplaces.Repository;

import com.bestplaces.Entity.ImageUrl;
import com.bestplaces.Entity.RentalPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageUrlRepository extends JpaRepository<ImageUrl, Long> {
    @Query("select i.imageUrl from ImageUrl i where i.id_post = :idPost")
    List<String> findByPostId(@Param("idPost") RentalPost idPost);
    @Query("select i from ImageUrl i where i.id_post = :idPost")
    List<ImageUrl> findAllByIdPost(@Param("idPost") RentalPost idPost);
}
