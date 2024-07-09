package com.bestplaces.Service;

import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportService {
    private static final Map<String, List<String>> reportStorage = new LinkedHashMap<>();

    public void reportPost(String postId, String username, String reason) {
        String report = "Post ID: " + postId + ", Reason: " + reason + ", Reported At: " + LocalDateTime.now() + ", Reported By: " + username;
        reportStorage.computeIfAbsent(postId, k -> new ArrayList<>()).add(report);
    }
    public List<String> getAllReports() {
        return reportStorage.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
}
