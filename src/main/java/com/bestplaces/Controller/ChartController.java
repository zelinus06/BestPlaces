package com.bestplaces.Controller;

import com.bestplaces.Entity.RentalPost;
import com.bestplaces.Entity.User;
import com.bestplaces.Service.ChartService;
import com.bestplaces.Service.UsersService;
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
    @Autowired
    private UsersService usersService;

    @GetMapping()
    public String generateChart(Model model) throws IOException {
        User currentUser = usersService.getCurrentUser();
        model.addAttribute("currentUser", currentUser);
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
        String chartAsBase64 = chartService.generateChart(list);
        model.addAttribute("chartAsBase64", chartAsBase64);
        model.addAttribute("chartType", type);
        model.addAttribute("chartAddress", city + ' ' + district + ' ' + commune);
        User currentUser = usersService.getCurrentUser();
        model.addAttribute("currentUser", currentUser);
        return "chart";
    }
}


