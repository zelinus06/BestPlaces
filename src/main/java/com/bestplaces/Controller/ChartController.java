package com.bestplaces.Controller;

import com.bestplaces.Entity.RentalPost;
import com.bestplaces.Service.ChartService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;

@Controller
@RequestMapping("/chart")
public class ChartController {
    @Autowired
    private ChartService chartService;

    @GetMapping()
    public String generateChart() throws IOException {
        return "chart";
    }

    @PostMapping()
    public String CreateChart(@RequestParam(required = false, value = "Type") String type,
                              @RequestParam(required = false, value = "city") String city,
                              @RequestParam(required = false, value = "district") String district,
                              @RequestParam(required = false, value = "commune") String commune,
                              Model model) throws IOException {

        if (Objects.equals(type, "")) {
            type = null;
        }
        if (Objects.equals(city, "")) {
            city = null;
        }
        if (Objects.equals(district, "")) {
            district = null;
        }
        if (Objects.equals(commune, "")) {
            commune = null;
        }

        List<RentalPost> list = chartService.createChart(type, city, district, commune);
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

////         Tạo một series mới cho đường line dựa trên dữ liệu
//        XYSeries lineSeries = createLineSeries(list);
//        XYSeriesCollection lineDataset = new XYSeriesCollection();
//        lineDataset.addSeries(lineSeries);

//        // Thêm đường line vào plot
//        plot.setDataset(1, lineDataset);
//        XYLineAndShapeRenderer splineTrendRenderer = new XYLineAndShapeRenderer(true, false);
//        splineTrendRenderer.setSeriesPaint(0, Color.BLUE);
//        plot.setRenderer(1, splineTrendRenderer);



        // Thêm đường hồi quy tuyến tính vào biểu đồ
        addLinearRegressionLine(plot, list);

        // Tạo một BufferedImage từ JFreeChart
        BufferedImage chartImage = chart.createBufferedImage(800, 600);
        // Chuyển đổi BufferedImage thành mảng byte (byte array)
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(chartImage, "png", outputStream);
        byte[] chartBytes = outputStream.toByteArray();

        // Chuyển đổi mảng byte thành dạng base64 để hiển thị trong HTML
        String chartAsBase64 = new String(java.util.Base64.getEncoder().encode(chartBytes));
        model.addAttribute("chartAsBase64", chartAsBase64);
        model.addAttribute("chartType", type);
        model.addAttribute("chartAddress", city + ' ' + district + ' ' + commune);
        return "chart";
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

//    public XYSeries createLineSeries(List<RentalPost> chartData) {
//        XYSeries lineSeries = new XYSeries("Line");
//
//        Set<Integer> areas = new HashSet<>();
//        for (RentalPost data : chartData) {
//            areas.add(data.getArea());
//        }
//
//        for (Integer area : areas) {
//            // Tạo một bản đếm cho các khoảng giá trong giá trị trục ngang hiện tại (area)
//            Map<Integer, Integer> priceCounts = new HashMap<>();
//            // Lặp qua dữ liệu RentalPost và tính số lượng điểm trong mỗi khoảng giá trong giá trị trục ngang hiện tại (area)
//            for (RentalPost data : chartData) {
//                if (data.getArea() == (area)) {
//                    Integer price = data.getPrice();
//                    priceCounts.put(price, priceCounts.getOrDefault(price, 0) + 1);
//                }
//            }
//
//            List<Map.Entry<Integer, Integer>> sortedPriceCounts = new ArrayList<>(priceCounts.entrySet());
//            sortedPriceCounts.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));
//
//            // Lấy ra top 3 giá trị và tính giá trị trung bình của khoảng giá đó
//            int count = 0;
//            double totalAveragePrice = 0;
//            for (Map.Entry<Integer, Integer> entry : sortedPriceCounts) {
//                if (count >= 3) {
//                    break;
//                }
//                Integer price = entry.getKey();
//                totalAveragePrice += price;
//                count++;
//            }
//            double averagePrice = totalAveragePrice / count;
//
//            // Thêm điểm trung bình của khoảng giá vào đường line
//            lineSeries.add((double) area, averagePrice);
//        }
//        return lineSeries;
//    }


}


