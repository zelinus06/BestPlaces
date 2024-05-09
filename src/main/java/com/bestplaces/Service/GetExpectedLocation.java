package com.bestplaces.Service;

import com.bestplaces.Entity.ListLocation;
import com.bestplaces.Entity.User;
import com.bestplaces.Repository.ListLocationRepository;
import com.bestplaces.Repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class GetExpectedLocation {
    @Autowired
    private ListLocationRepository listLocationRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private MyUserDetailsService myUserDetailsService;
    @Autowired
    private UserRepository userRepository;

    ArrayList<String> list = new ArrayList<>();

//    @Async
    public void saveSearchResult(String location) {
        list.add(location);
        List<String> duplicates = findDuplicates(list);
        try {
            for (String duplicate : duplicates) {
                String apiUrl = String.format("https://nominatim.openstreetmap.org/search?q=%s&format=json", duplicate);
                String result = restTemplate.getForObject(apiUrl, String.class);
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode jsonNode = objectMapper.readTree(result);
                    double latitude = jsonNode.get(0).get("lat").asDouble();
                    double longtitude = jsonNode.get(0).get("lon").asDouble();

                    Optional<User> userOptional = userRepository.findByUsername(myUserDetailsService.UserNameAtPresent());
                    ListLocation listLocation = new ListLocation();
                    if (userOptional.isPresent()) {
                        User user = userOptional.get();
                        listLocation.setUser(user);
                        listLocation.setLatitude(latitude);
                        listLocation.setLongtitude(longtitude);
                        listLocationRepository.save(listLocation);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> findDuplicates(ArrayList<String> list) {
        List<String> duplicates = new ArrayList<>();
        Set<String> uniqueElements = new HashSet<>();
        for (String element : list) {
            if (!uniqueElements.add(element)) {
                duplicates.add(element);
            }
        }
        return duplicates;
    }
}
