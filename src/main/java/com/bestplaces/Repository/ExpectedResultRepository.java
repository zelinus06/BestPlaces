package com.bestplaces.Repository;

import com.bestplaces.Entity.ExpectedResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpectedResultRepository extends JpaRepository<ExpectedResult, Long> {
}
