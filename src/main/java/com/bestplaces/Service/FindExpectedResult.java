package com.bestplaces.Service;

public class FindExpectedResult {
    public static void main(String[] args) {
        // Ví dụ điểm tọa độ trái đất
        double[][] earthPoints = {
                {21.0058553, 105.9373266},
                {20.9796101, 105.9160221},
                {20.9830954, 105.9310738},
                {21.0072277, 105.9576432},
                {21.0272254, 105.923409},
                {21.0242233, 105.9306881}
        };
        double[] circle = findCircle(earthPoints);
        System.out.println("Tâm của đường tròn: (" + circle[0] + ", " + circle[1] + ")");
        System.out.println("Bán kính của đường tròn: " + circle[2]);
    }

    public static double[] findCircle(double[][] points) {
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
    public static double haversine(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = R * c;
        return distance;
    }
}

