package com.bestplaces.Service;


import com.bestplaces.Entity.RentalPost;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

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

    public String generateChart(List<RentalPost> list) throws IOException {
        XYDataset dataset = createDataset(list);
        JFreeChart chart = ChartFactory.createScatterPlot(
                "Biểu đồ giá cả",
                "Area (m2)", // Trục ngang
                "Price (kVNĐ)", // Trục dọc
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false
        );

        XYPlot plot = (XYPlot) chart.getPlot();
        // Đặt màu nền của Plot thành màu trắng
        plot.setBackgroundPaint(Color.WHITE);
        Ellipse2D circle = new Ellipse2D.Double(-3.0, -3.0, 6.0, 6.0);
        XYItemRenderer renderer = plot.getRenderer();
        renderer.setSeriesShape(0, circle);
        renderer.setSeriesPaint(0, Color.blue);

        // Thêm đường hồi quy tuyến tính vào biểu đồ
        // Chỉ thêm đường hồi quy nếu danh sách dữ liệu không rỗng
        if (!list.isEmpty()) {
            addLinearRegressionLine(plot, list);
        }

        // Tạo một BufferedImage từ JFreeChart
        BufferedImage chartImage = chart.createBufferedImage(800, 600);
        // Chuyển đổi BufferedImage thành mảng byte (byte array)
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(chartImage, "png", outputStream);
        byte[] chartBytes = outputStream.toByteArray();
        // Chuyển đổi mảng byte thành dạng base64 để hiển thị trong HTML
        String chartAsBase64 = new String(java.util.Base64.getEncoder().encode(chartBytes));
        return chartAsBase64;
    }

    public XYDataset createDataset(List<RentalPost> chartData) {
        XYSeries series = new XYSeries("Data");
        // Lặp qua mỗi mảng Object[] và thêm các giá trị vào series
        for (RentalPost data : chartData) {
            Integer area = (Integer) Objects.requireNonNull(data.getArea()); // Giá trị area
            Integer price = (Integer) Objects.requireNonNull(data.getPrice()); // Giá trị price
            series.add(area, price);
        }
        // Tạo dataset và thêm series vào đó
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        return dataset;
    }

    private void addLinearRegressionLine(XYPlot plot, List<RentalPost> chartData) {
        double[] areas = chartData.stream().mapToDouble(RentalPost::getArea).toArray();
        double[] prices = chartData.stream().mapToDouble(RentalPost::getPrice).toArray();

        // Tính toán các hệ số hồi quy tuyến tính
        SimpleRegression regression = new SimpleRegression();
        for (int i = 0; i < areas.length; i++) {
            regression.addData(areas[i], prices[i]);
        }

        double intercept = regression.getIntercept();
        double slope = regression.getSlope();

        // Tạo một series mới cho đường hồi quy tuyến tính
        XYSeries regressionSeries = new XYSeries("Regression Line");
        double minX = Arrays.stream(areas).min().getAsDouble();
        double maxX = Arrays.stream(areas).max().getAsDouble();
        regressionSeries.add(minX, intercept + slope * minX);
        regressionSeries.add(maxX, intercept + slope * maxX);

        XYSeriesCollection regressionDataset = new XYSeriesCollection();
        regressionDataset.addSeries(regressionSeries);

        plot.setDataset(1, regressionDataset);
        XYLineAndShapeRenderer regressionRenderer = new XYLineAndShapeRenderer(true, false);
        regressionRenderer.setSeriesPaint(0, Color.RED);
        plot.setRenderer(1, regressionRenderer);
    }
}
