package com.bestplaces.Service;

import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ReportService {
    private static final Map<String, String> reportStorage = new ConcurrentHashMap<>();

    public void reportPost(String postId, String reason) {
        String report = "Post ID: " + postId + ", Reason: " + reason + ", Reported At: " + LocalDateTime.now();
        reportStorage.put(postId, report);
    }

    public String getReport(String postId) {
        return reportStorage.get(postId);
    }
}
