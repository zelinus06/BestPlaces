package com.bestplaces.Service;

import com.bestplaces.Entity.ExpectedResult;
import com.bestplaces.Entity.ListLocation;
import com.bestplaces.Entity.RentalPost;
import com.bestplaces.Entity.User;
import com.bestplaces.Repository.ExpectedResultRepository;
import com.bestplaces.Repository.ListLocationRepository;
import com.bestplaces.Repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@Transactional
public class FilterSearchService {
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private GetExpectedResult getExpectedResult;
    @Async
    public void saveSearchResultAsync(String location) {
        getExpectedResult.saveSearchResult(location);
    }

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
        saveSearchResultAsync(location);
        // Kết hợp tất cả các điều kiện với AND
        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
