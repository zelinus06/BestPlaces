package com.bestplaces.Service;


import com.bestplaces.Entity.SearchedResult;
import com.bestplaces.Entity.User;
import com.bestplaces.Repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class GetExpectedSearch {
    private Map<Integer, Integer> maxPriceSearchCount = new HashMap<>();
    private Map<Integer, Integer> maxAreaSearchCount = new HashMap<>();
    private Map<Integer, Integer> minPriceSearchCount = new HashMap<>();
    private Map<Integer, Integer> minAreaSearchCount = new HashMap<>();
    private Map<String, Integer> mostTypeSearchCount = new HashMap<>();
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MyUserDetailsService myUserDetailsService;

    public void updateAllCount(Integer minArea, Integer maxArea, Integer minPrice, Integer maxPrice, String type) {
        if (minArea != null) {
            minAreaSearchCount.put(minArea, minAreaSearchCount.getOrDefault(minArea, 0) + 1);
        }
        if (minPrice != null) {
            minPriceSearchCount.put(minPrice, minPriceSearchCount.getOrDefault(minPrice, 0) + 1);
        }
        if (maxArea != null) {
            maxAreaSearchCount.put(maxArea, maxAreaSearchCount.getOrDefault(maxArea, 0) + 1);
        }
        if (maxPrice != null) {
            maxPriceSearchCount.put(maxPrice, maxPriceSearchCount.getOrDefault(maxPrice, 0) + 1);
        }
        if (type != null) {
            mostTypeSearchCount.put(type, mostTypeSearchCount.getOrDefault(type, 0) + 1);
        }
        Integer maxPriceWithMaxCount = getKeyWithMaxCount(maxPriceSearchCount);
        Integer maxAreaWithMaxCount = getKeyWithMaxCount(maxAreaSearchCount);
        Integer minPriceWithMaxCount = getKeyWithMaxCount(minPriceSearchCount);
        Integer minAreaWithMaxCount = getKeyWithMaxCount(minAreaSearchCount);
        String mostTypeMaxCount = getKeyWithMaxCount(mostTypeSearchCount);

        Optional<User> userOptional = userRepository.findByUsername(myUserDetailsService.UserNameAtPresent());
        SearchedResult searchedResult = new SearchedResult();
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            searchedResult.setUser(user);
            searchedResult.setId(user.getId());
            if (maxArea != null){
                searchedResult.setMaxArea(maxAreaWithMaxCount);
            }
            if (minArea != null){
                searchedResult.setMinArea(minAreaWithMaxCount);
            }
            if (maxPrice != null){
                searchedResult.setMaxPrice(maxPriceWithMaxCount);
            }
            if (minPrice != null){
                searchedResult.setMinPrice(minPriceWithMaxCount);
            }
            if (type != null) {
                searchedResult.setType(mostTypeMaxCount);
            }
            saveOrUpdateSearchedResult(searchedResult);
        }

    }

    public <T> T getKeyWithMaxCount(Map<T, Integer> countMap) {
        T keyWithMaxCount = null;
        int maxCount = Integer.MIN_VALUE;

        for (Map.Entry<T, Integer> entry : countMap.entrySet()) {
            T key = entry.getKey();
            Integer count = entry.getValue();

            if (count > maxCount) {
                maxCount = count;
                keyWithMaxCount = key;
            }
        }
        return keyWithMaxCount;
    }

    public void saveOrUpdateSearchedResult(SearchedResult searchedResult) {
            // Ta cần kiểm tra xem có sẵn một record cho user trong bảng SearchedResult hay không
            Query query = entityManager.createQuery("SELECT sr FROM SearchedResult sr WHERE sr.user = :user");
            query.setParameter("user", searchedResult.getUser());
            List<SearchedResult> existingResults = query.getResultList();
            if (existingResults.isEmpty()) {
                // Nếu không có record nào cho user, thì thực hiện lưu mới thông tin searchedResult
                entityManager.persist(searchedResult);
            } else {
                // Nếu đã có record cho user, thì cập nhật thông tin của record đầu tiên trong danh sách
                SearchedResult existingResult = existingResults.get(0);
                if (searchedResult.getMinArea() != 0) {
                    existingResult.setMinArea(searchedResult.getMinArea());
                }
                if (searchedResult.getMaxArea() != 0) {
                    existingResult.setMaxArea(searchedResult.getMaxArea());
                }
                if (searchedResult.getMinPrice() != 0) {
                    existingResult.setMinPrice(searchedResult.getMinPrice());
                }
                if (searchedResult.getMaxPrice() != 0) {
                    existingResult.setMaxPrice(searchedResult.getMaxPrice());
                }
                if (searchedResult.getType() != null) {
                    existingResult.setType(searchedResult.getType());
                }
                entityManager.merge(existingResult);
            }
    }
}
