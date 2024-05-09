package com.bestplaces.Service;

import com.bestplaces.Dto.PostDto;
import com.bestplaces.Entity.RentalPost;
import com.bestplaces.Entity.SearchedResult;
import com.bestplaces.Entity.User;
import com.bestplaces.Enums.Type;
import com.bestplaces.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RecommenderService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ListLocationRepository listLocationRepository;
    @Autowired
    private SearchedResultRepository searchedResultRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FindExpectedLocation findExpectedLocation;
    @Autowired
    private ImageUrlRepository imageUrlRepository;

    public List<PostDto> recommend() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Optional<User> userOptional = userRepository.findByUsername(username);
        User user = userOptional.get();
        Optional<SearchedResult> searchedResult = searchedResultRepository.findById(user.getId());
        SearchedResult searchedResult1 = searchedResult.get();
        List<RentalPost> rentalPosts = postRepository.findAllByType(Type.valueOf(searchedResult1.getType()));
        int maxPrice = searchedResult1.getMaxPrice();
        int minPrice = searchedResult1.getMinPrice();
        int maxArea = searchedResult1.getMaxArea();
        int minArea = searchedResult1.getMinArea();
        int ExpectedPrice = (maxPrice + minPrice)/2;
        int ExpectedArea = (maxArea + minArea)/2;
        String result = findExpectedLocation.FindExResult();
        double[] result1 = parseCircle(result);
        double radius = result1[0];

        List<PostDto> postDTOs = new ArrayList<>();
        for (RentalPost rentalPost : rentalPosts) {
            int price = rentalPost.getPrice();
            double normalizePrice = normalize(price, maxPrice, minPrice, ExpectedPrice);
            int area = rentalPost.getArea();
            double normalizeArea = normalize(area, maxArea, minArea, ExpectedArea);
            double latitude = rentalPost.getLatitude();
            double longtitude = rentalPost.getLongtitude();
            double distance = haversine(result1[1], result1[2], latitude, longtitude);
            double normalizeDistance = normalizeDistance(distance, radius);
            double[] vector1 = {1, 1, 1};
            double[] vector2 = {normalizePrice, normalizeArea, normalizeDistance};
            double recommendResult = euclideanDistance(vector1, vector2);
            if (recommendResult < 0.86) {
                // in ra màn hình kết quả recommend
                List<String> images = imageUrlRepository.findByPostId(rentalPost);
                postDTOs.add(new PostDto(rentalPost, images));
            }
        }
        return postDTOs;
    }
    public double normalizeDistance(double distance, double radius) {
        if (distance == radius) {
            return 1.0;
        } else if(distance == 0) {
            return 1.0;
        }
        else {
            return 1.0 - distance/(radius*2);
        }
    }

    public double normalize(int value, int maxValue, int minValue, int expectedValue) {
        if (value == expectedValue) {
            return 1.0;
        }
        else if (value == maxValue || value == minValue) {
            return 0.5;
        }
        else if (value < expectedValue && value > minValue) {
            // Ánh xạ giá thuê vào phạm vi từ 0.5 đến 1
            return 0.5 + 0.5 * (expectedValue - value) / (expectedValue - (minValue*2 - expectedValue));
        }
        else if (value > expectedValue && value < maxValue) {
            // Ánh xạ giá thuê vào phạm vi từ 1 đến 0.5
            return 1.0 - 1.0*(value - expectedValue) / ((maxValue*2 - expectedValue)- expectedValue);
        }
        else if (value < minValue && value > (minValue*2 - expectedValue) ) {
                return 1.0*(value - (expectedValue*2 - minValue))/(expectedValue - (minValue*2 - expectedValue));
            }
        else if (value > maxValue && value < (maxValue*2 - expectedValue) ) {
            return 1.0*((maxValue*2 - expectedValue)- value)/((maxValue*2 - expectedValue)- expectedValue);
        }
        else {
            return 0.0;
        }
    }


    public double[] parseCircle(String circleResult) {
        String[] parts = circleResult.split(";");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid circle result format");
        }

        String[] centerCoords = parts[0].split(",");
        if (centerCoords.length != 2) {
            throw new IllegalArgumentException("Invalid center coordinates format");
        }

        double centerX = Double.parseDouble(centerCoords[0]);
        double centerY = Double.parseDouble(centerCoords[1]);
        double radius = Double.parseDouble(parts[1]);

        return new double[]{radius, centerX, centerY};
    }

    public double haversine(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = R * c;
        return distance;
    }

    public double euclideanDistance(double[] vector1, double[] vector2) {
        if (vector1.length != vector2.length) {
            throw new IllegalArgumentException("Vectors must have the same length");
        }

        double sumSquaredDiff = 0.0;
        for (int i = 0; i < vector1.length; i++) {
            double diff = vector1[i] - vector2[i];
            sumSquaredDiff += diff * diff;
        }

        return Math.sqrt(sumSquaredDiff);
    }

}