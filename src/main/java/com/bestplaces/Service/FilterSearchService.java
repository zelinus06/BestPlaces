package com.bestplaces.Service;

import com.bestplaces.Entity.RentalPost;
import com.sun.jdi.IntegerValue;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional
public class FilterSearchService {
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private GetExpectedLocation getExpectedResult;
    @Autowired
    private GetExpectedSearch getExpectedSearch;
    public List<RentalPost> searchPost(Double minPrice, Double maxPrice, Integer minArea, Integer maxArea, String type, String city, String district, String commune) {
        String location = null;
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<RentalPost> criteriaQuery = criteriaBuilder.createQuery(RentalPost.class);
        Root<RentalPost> rentalPost = criteriaQuery.from(RentalPost.class);
        List<Predicate> predicates = new ArrayList<>();
        if (minPrice != null && maxPrice != null) {
            predicates.add(criteriaBuilder.between(rentalPost.get("price"), minPrice, maxPrice));
        } else if (minPrice != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(rentalPost.get("price"), minPrice));
        } else if (maxPrice != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(rentalPost.get("price"), maxPrice));
        }

        if (minArea != null && maxArea != null) {
            predicates.add(criteriaBuilder.between(rentalPost.get("area"), minArea, maxArea));
        } else if (minArea != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(rentalPost.get("area"), minArea));
        } else if (maxArea != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(rentalPost.get("area"), maxArea));
        }
        if (type != null) {
            predicates.add(criteriaBuilder.equal(rentalPost.get("type"), type));
        }
        if ( city != null) {
            predicates.add(criteriaBuilder.equal(rentalPost.get("city"), city));
            location = city;
            if ( district != null) {
                predicates.add(criteriaBuilder.equal(rentalPost.get("district"), district));
                location = location + "," + district;
                if ( commune != null) {
                    predicates.add(criteriaBuilder.equal(rentalPost.get("commune"), commune));
                    location = location + "," + commune;
                }
            }
        }
        Integer minPriceInt = null;
        Integer maxPriceInt = null;
        if (minPrice != null) {
              minPriceInt = (int) Math.round(minPrice);
        }
        if (maxPrice != null) {
             maxPriceInt = (int) Math.round(maxPrice);
        }
        getExpectedSearch.updateAllCount(minArea, maxArea, minPriceInt, maxPriceInt, type);
        getExpectedResult.saveSearchResult(location);
        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
