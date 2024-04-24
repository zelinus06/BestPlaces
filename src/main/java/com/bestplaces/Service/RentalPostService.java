package com.bestplaces.Service;

import com.bestplaces.Dto.PostDto;
import com.bestplaces.Dto.RentalPostDto;
import com.bestplaces.Entity.RentalPost;
import com.bestplaces.Entity.User;
import com.bestplaces.Repository.ImageUrlRepository;
import com.bestplaces.Repository.PostRepository;
import com.bestplaces.Repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.List;

@Service
public class RentalPostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private MyUserDetailsService myUserDetailsService;
    @Autowired
    private ImageUrlRepository imageUrlRepository;

    public RentalPost saveRentalPost(RentalPostDto rentalPostDto) {
        Optional<User> userOptional = userRepository.findByUsername(myUserDetailsService.UserNameAtPresent());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String phoneNumber = user.getPhoneNumber();
            long postId = generateUniqueId();
            RentalPost rentalPost = new RentalPost(rentalPostDto.getId() ,rentalPostDto.getCity(), rentalPostDto.getDistrict(), rentalPostDto.getCommune(), rentalPostDto.getStreet(), rentalPostDto.getNumberHouse(), rentalPostDto.getType(), rentalPostDto.getPhoneNumber());
            rentalPost.setId_post(postId);
            rentalPost.setUser(user);
            rentalPost.setPhoneNumber(phoneNumber);
            String address = rentalPostDto.getNumberHouse() + "," + rentalPostDto.getStreet() + "," + rentalPostDto.getCommune() + "," + rentalPostDto.getDistrict()  + "," + rentalPostDto.getCity();
            String apiUrl = String.format("https://nominatim.openstreetmap.org/search?q=%s&format=json", address);
            String result = restTemplate.getForObject(apiUrl, String.class);
            try {
                // Parse JSON response
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(result);
                if (jsonNode.isArray() && jsonNode.size() > 0) {
                    double latitude = jsonNode.get(0).get("lat").asDouble();
                    double longitude = jsonNode.get(0).get("lon").asDouble();
                    rentalPost.setLatitude(latitude);
                    rentalPost.setLongtitude(longitude);
                } else {
                    String address1 = rentalPostDto.getStreet() + "," + rentalPostDto.getCommune() + "," + rentalPostDto.getDistrict()  + "," + rentalPostDto.getCity();
                    String apiUrl1 = String.format("https://nominatim.openstreetmap.org/search?q=%s&format=json", address1);
                    String result1 = restTemplate.getForObject(apiUrl1, String.class);
                    ObjectMapper objectMapper1 = new ObjectMapper();
                    JsonNode jsonNode1 = objectMapper1.readTree(result1);
                    double latitude = jsonNode1.get(0).get("lat").asDouble();
                    double longitude = jsonNode1.get(0).get("lon").asDouble();
                    rentalPost.setLatitude(latitude);
                    rentalPost.setLongtitude(longitude);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            postRepository.save(rentalPost);
            Optional<RentalPost> rentalPost1 = postRepository.findById(rentalPost.getId_post());
            RentalPost rentalPost2 = rentalPost1.get();
            return rentalPost2;
        } else {
            return null;
        }
    }

    public List<RentalPost> getAllPosts() {
        List<RentalPost> list = postRepository.findAll();
        return list;
    }

    public List<PostDto> getPosts(List<RentalPost> posts) {
        List<PostDto> postDTOs = new ArrayList<>();
        for (RentalPost post : posts) {
            List<String> images = imageUrlRepository.findByPostId(post);
            postDTOs.add(new PostDto(post, images));
        }
        return postDTOs;
    }

    private long generateUniqueId() {
        long currentTime = System.currentTimeMillis();
        Random random = new Random();
        long randomLong = random.nextLong() & Long.MAX_VALUE;
        return currentTime + randomLong;
    }
}

