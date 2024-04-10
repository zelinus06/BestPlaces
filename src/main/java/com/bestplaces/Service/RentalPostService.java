package com.bestplaces.Service;

import com.bestplaces.Dto.RentalPostDto;
import com.bestplaces.Entity.RentalPost;
import com.bestplaces.Entity.User;
import com.bestplaces.Repository.PostRepository;
import com.bestplaces.Repository.UserRepository;
import com.bestplaces.Service.Impl.UserServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

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

    public RentalPost saveRentalPost(RentalPostDto rentalPostDto) {
        Optional<User> userOptional = userRepository.findByUsername(myUserDetailsService.UserNameAtPresent());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String phoneNumber = user.getPhoneNumber();
            RentalPost rentalPost = new RentalPost(rentalPostDto.getId(), rentalPostDto.getTitle() ,rentalPostDto.getCity(), rentalPostDto.getDistrict(), rentalPostDto.getCommune(), rentalPostDto.getExactAddress(), rentalPostDto.getImagePath(), rentalPostDto.getPrice(), rentalPostDto.getArea(), rentalPostDto.getType(), rentalPostDto.getDescription(), rentalPostDto.getPhoneNumber());
            rentalPost.setUser(user);
            rentalPost.setPhoneNumber(phoneNumber);
            String address = rentalPostDto.getExactAddress() + "," + rentalPostDto.getCommune() + "," + rentalPostDto.getDistrict()  + "," + rentalPostDto.getCity();
            String apiUrl = String.format("https://nominatim.openstreetmap.org/search?q=%s&format=json", address);
            String result = restTemplate.getForObject(apiUrl, String.class);
            try {
                // Parse JSON response
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(result);

                // Lấy giá trị latitude và longitude từ đối tượng JSON trong mảng
                double latitude = jsonNode.get(0).get("lat").asDouble();
                double longitude = jsonNode.get(0).get("lon").asDouble();
                rentalPost.setLatitude(latitude);
                rentalPost.setLongtitude(longitude);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return postRepository.save(rentalPost);
        } else {
            return null;
        }
    }

    public List<RentalPost> getAllPosts() {
        return postRepository.findAll();
    }
}

