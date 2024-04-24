package com.bestplaces.Repository;

import com.bestplaces.Entity.ListLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ListLocationRepository extends JpaRepository<ListLocation, Long> {
}
