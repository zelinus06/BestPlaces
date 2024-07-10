package com.bestplaces.Repository;

import com.bestplaces.Entity.Comment;
import com.bestplaces.Entity.RentalPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select c from Comment c where c.id_post = :idPost")
    List<Comment> showAllComment(@Param("idPost") RentalPost idPost);
}
