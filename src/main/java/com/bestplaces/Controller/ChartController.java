package com.bestplaces.Controller;

import com.bestplaces.Entity.RentalPost;
import com.bestplaces.Service.ChartService;
import jakarta.servlet.http.HttpServletResponse;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Controller
public class ChartController {
    @Autowired
    private ChartService chartService;

    @GetMapping("/chart")
    public String generateChart(Model model, HttpServletResponse response) throws IOException {
        return "chart";
    }

    @PostMapping("/chart")
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
        renderer.setSeriesPaint(0, Color.red);

        // Tạo một BufferedImage từ JFreeChart
        BufferedImage chartImage = chart.createBufferedImage(800, 600);
        // Chuyển đổi BufferedImage thành mảng byte (byte array)
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(chartImage, "png", outputStream);
        byte[] chartBytes = outputStream.toByteArray();

        // Chuyển đổi mảng byte thành dạng base64 để hiển thị trong HTML
        String chartAsBase64 = new String(java.util.Base64.getEncoder().encode(chartBytes));
        model.addAttribute("chartAsBase64", chartAsBase64);
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
}


