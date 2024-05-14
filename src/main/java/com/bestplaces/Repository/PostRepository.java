package com.bestplaces.Repository;

import com.bestplaces.Entity.ImageUrl;
import com.bestplaces.Entity.RentalPost;
import com.bestplaces.Entity.User;
import com.bestplaces.Enums.Type;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<RentalPost, Long> {
     List<RentalPost> findAllByType(Type type);
     List<RentalPost> findAllByUserId(User user);
}
