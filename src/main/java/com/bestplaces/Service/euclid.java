package com.bestplaces.Service;

public class euclid {
    public static void main(String[] args) {

        double[] houseA = {1, 1, 1}; // Giá và diện tích của căn nhà A
        double[] houseB = {0.5, 0.5, 0.5}; // Giá và diện tích của căn nhà B

        double similarity = calculateEuclideanDistance(houseA, houseB);

        System.out.println("Độ tương đồng giữa căn nhà A và B: " + similarity);
    }

    // Hàm tính khoảng cách Euclid giữa hai căn nhà
    public static double calculateEuclideanDistance(double[] houseA, double[] houseB) {
        double sum = 0.0;
        double weightPrice = 1.0;
        double weightArea = 1.0;
        double weightDistrict = 1.0;
        // Kiểm tra xem độ dài của hai mảng có bằng nhau không
        if (houseA.length != houseB.length) {
            throw new IllegalArgumentException("Hai căn nhà phải có cùng số lượng thuộc tính");
        }

        // Tính tổng bình phương của hiệu của các thuộc tính
        for (int i = 0; i < houseA.length; i++) {
            double diff = 0.0;
            diff = (houseA[i] - houseB[i]);
            if (i == 0) {
                sum += diff * diff * weightPrice;
            }
            if (i == 1) {
                sum += diff * diff * weightArea;
            }
            if (i == 2) {
                sum += diff * diff * weightDistrict;
            }
        }

        // Trả về căn bậc hai của tổng
        return Math.sqrt(sum);
    }
}
