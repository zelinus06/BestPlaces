package com.bestplaces.Repository;

import com.bestplaces.Entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    Optional<User> findByUsername(String username);
    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.role = 'USER' WHERE u.username = ?1")
    void setUser(String username);
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.phoneNumber = ?2 WHERE u.username = ?1")
    void setPhoneNumber(String username, String phoneNumber);

}