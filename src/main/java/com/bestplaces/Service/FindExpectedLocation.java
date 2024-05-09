package com.bestplaces.Service;

import com.bestplaces.Entity.User;
import com.bestplaces.Repository.ListLocationRepository;
import com.bestplaces.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FindExpectedLocation {
    @Autowired
    private ListLocationRepository listLocationRepository;
    @Autowired
    private UserRepository userRepository;

    public String FindExResult() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Optional<User> userOptional = userRepository.findByUsername(username);
        User user = userOptional.get();

        List<Object[]> list = listLocationRepository.findAllByUserId(user.getId());

        double[][] earthPoints = new double[list.size()][2];

        for (int i = 0; i < list.size(); i++) {
            Object[] location = list.get(i);
            if (location.length >= 2) {
                earthPoints[i][0] = (double) location[0];
                earthPoints[i][1] = (double) location[1];
            } else {
                // Xử lý trường hợp không hợp lệ (nếu cần)
            }
        }

        double[] circle = findCircle(earthPoints);
        System.out.println("Tâm của đường tròn: (" + circle[0] + ", " + circle[1] + ")");
        System.out.println("Bán kính của đường tròn: " + circle[2]);
        String result = circle[0] + "," + circle[1] + ";" + circle[2];
        return result;
    }

    public double[] findCircle(double[][] points) {
        int n = points.length;
        double[] center = {0, 0};
        double radius = 0;

        // Tính toán tâm bằng cách lấy trung bình các tọa độ
        for (double[] point : points) {
            center[0] += point[0] / n;
            center[1] += point[1] / n;
        }

        // Tính khoảng cách từ tâm đến mỗi điểm và lấy khoảng cách lớn nhất làm bán kính
        for (double[] point : points) {
            double distance = haversine(center[0], center[1], point[0], point[1]);
            radius = Math.max(radius, distance);

        }
        return new double[] {center[0], center[1], radius};
    }
    // Haversine formula
    public double haversine(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = R * c;
        return distance;
    }
}

