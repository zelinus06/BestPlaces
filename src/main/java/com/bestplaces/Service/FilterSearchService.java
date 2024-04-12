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

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class FilterSearchService {
    @PersistenceContext
    private EntityManager entityManager;

    public List<RentalPost> searchPost(Double minPrice, Double maxPrice, Integer minArea, Integer maxArea, String type, String city, String district, String commune) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<RentalPost> criteriaQuery = criteriaBuilder.createQuery(RentalPost.class);
        Root<RentalPost> rentalPost = criteriaQuery.from(RentalPost.class);
        List<Predicate> predicates = new ArrayList<>();

        System.out.println(minPrice + ","+ maxPrice + "," + minArea+ "," +maxArea+ "," +type);
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
            if ( district != null) {
                predicates.add(criteriaBuilder.equal(rentalPost.get("district"), district));
                if ( commune != null) {
                    predicates.add(criteriaBuilder.equal(rentalPost.get("commune"), commune));
                }
            }
        }

        // Kết hợp tất cả các điều kiện với AND
        criteriaQuery.where(predicates.toArray(new Predicate[0]));

        // Thực hiện truy vấn và trả về kết quả
        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
