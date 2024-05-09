package com.bestplaces.Repository;

import com.bestplaces.Entity.ListLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface ListLocationRepository extends JpaRepository<ListLocation, Long> {
    @Query("SELECT  l.latitude, l.longtitude FROM ListLocation l WHERE l.user.id = :userId")
    List<Object[]> findAllByUserId(Long userId);

}
