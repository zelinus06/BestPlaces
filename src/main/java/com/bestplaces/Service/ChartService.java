package com.bestplaces.Service;


import com.bestplaces.Entity.RentalPost;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@Transactional
public class ChartService {
    @PersistenceContext
    private EntityManager entityManager;
    public List<RentalPost> createChart(String type, String city, String district, String commune) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<RentalPost> criteriaQuery = criteriaBuilder.createQuery(RentalPost.class);
        Root<RentalPost> rentalPost = criteriaQuery.from(RentalPost.class);
        List<Predicate> predicates = new ArrayList<>();
        if (type != null) {
            predicates.add(criteriaBuilder.equal(rentalPost.get("type"), type));
        }
        if ( city != null) {
            predicates.add(criteriaBuilder.equal(rentalPost.get("city"), city));
            if ( district != null) {
                predicates.add(criteriaBuilder.equal(rentalPost.get("district"), district));
                if ( commune != null) {
                    predicates.add(criteriaBuilder.equal(rentalPost.get("commune"), commune));
                }
            }
        }
        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

}
