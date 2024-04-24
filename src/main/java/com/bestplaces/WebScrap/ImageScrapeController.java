package com.bestplaces.WebScrap;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class ImageScrapeController {
    private static final String FIXED_URL = "https://phongtro123.com/tinh-thanh/ha-noi";
    @Value("${image.storage.path}")
    private String imageStoragePath; // Đường dẫn đến thư mục lưu trữ hình ảnh

    @GetMapping("/scrape")
    public ResponseEntity<List<String>> scrapeImages() {
        List<String> imageUrls = new ArrayList<>();

        try {
            Document doc = Jsoup.connect(FIXED_URL).get();
            Elements images = doc.select("img");
            for (Element image : images) {
                String imageUrl = image.attr("data-src");
                System.out.println(imageUrl);
                String fileName = UUID.randomUUID().toString() + ".jpg"; // Tạo tên ngẫu nhiên cho hình ảnh
                saveImage(imageUrl, fileName);
                imageUrls.add(fileName);
                System.out.println("saved");
            }

            return ResponseEntity.ok(imageUrls);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    private void saveImage(String imageUrl, String fileName) throws IOException {
//        if (imageUrl.startsWith("http://") || imageUrl.startsWith("https://")) {
//            URI uri = URI.create(imageUrl);
//            File imageFile = new File(imageStoragePath + File.separator + fileName);
//            FileUtils.copyURLToFile(uri.toURL(), imageFile);
//            System.out.println("done");
//        } else {
//            // Nếu là URL tương đối, bỏ qua và không sao chép hình ảnh
//            System.out.println("Skipped relative URL: " + imageUrl);
//        }
        // Kiểm tra xem imageUrl có phải là URL tuyệt đối không
        if (imageUrl.startsWith("http://") || imageUrl.startsWith("https://")) {
            // Tạo kết nối tới URL hình ảnh
            HttpURLConnection connection = null;
            try {
                URL url = URI.create(imageUrl).toURL();
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                // Lấy luồng dữ liệu từ kết nối
                try (InputStream inputStream = connection.getInputStream();
                     OutputStream outputStream = new FileOutputStream(imageStoragePath + File.separator + fileName)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    System.out.println("Downloaded image: " + imageUrl);
                }
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        } else {
            // Nếu là URL tương đối, bỏ qua và không tải hình ảnh
            System.out.println("Skipped relative URL: " + imageUrl);
        }
    }
}


