package com.bestplaces.Repository;

import com.bestplaces.Entity.SearchedResult;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SearchedResultRepository extends JpaRepository<SearchedResult, Long> {
}
