package com.bestplaces.Service;

import com.bestplaces.Dto.CommentDto;
import com.bestplaces.Dto.PostDto;
import com.bestplaces.Dto.RentalPostDto;
import com.bestplaces.Entity.ImageUrl;
import com.bestplaces.Entity.Comment;
import com.bestplaces.Entity.RentalPost;
import com.bestplaces.Entity.User;
import com.bestplaces.Enums.Type;
import com.bestplaces.Repository.ImageUrlRepository;
import com.bestplaces.Repository.PostRepository;
import com.bestplaces.Repository.CommentRepository;
import com.bestplaces.Repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    @Autowired
    private CommentRepository commentRepository;

    public RentalPost saveRentalPost(RentalPostDto rentalPostDto) {
        Optional<User> userOptional = userRepository.findByUsername(myUserDetailsService.UserNameAtPresent());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String phoneNumber = user.getPhoneNumber();
            long postId = generateUniqueId();
            RentalPost rentalPost = new RentalPost(rentalPostDto.getId() ,rentalPostDto.getCity(), rentalPostDto.getDistrict(), rentalPostDto.getCommune(), rentalPostDto.getStreet(), rentalPostDto.getNumberHouse(), rentalPostDto.getType(), rentalPostDto.getPhoneNumber(), rentalPostDto.getPrice(), rentalPostDto.getArea(), rentalPostDto.getTitle(), rentalPostDto.getDescription());
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

    public PostDto getDetailPost(Long idPost) {
        Optional<RentalPost> postOptional = postRepository.findById(idPost);
        RentalPost rentalPost = postOptional.get();
        List<String> images = imageUrlRepository.findByPostId(rentalPost);
        PostDto postDto = new PostDto(rentalPost, images);
        return postDto;
    }

    public List<CommentDto> showComment(Long idPost) {
        List<CommentDto> comments = new ArrayList<>() ;
        Optional<RentalPost> postOptional = postRepository.findById(idPost);
        RentalPost rentalPost = postOptional.get();
        List<Comment> comment = commentRepository.showAllComment(rentalPost);
        for (Comment comment1 : comment) {
            User user = comment1.getId_user();
            String username = user.getUsername();
            String avatarUrl = user.getAvatar();
            CommentDto commentDto = new CommentDto(comment1, username, avatarUrl);
            comments.add(commentDto);
        }
        return comments;
    }

    public List<RentalPost> getUserPost() {
        Optional<User> userOptional = userRepository.findByUsername(myUserDetailsService.UserNameAtPresent());
        User user = userOptional.get();
        List<RentalPost> list = postRepository.findAllByUserId(user);
        return list;
    }

    public void deletePost(Long idPost) {
        Optional<RentalPost> postOptional = postRepository.findById(idPost);
        RentalPost rentalPost = postOptional.get();
        List<ImageUrl> imageUrl = imageUrlRepository.findAllByIdPost(rentalPost);
        imageUrlRepository.deleteAll(imageUrl);
        postRepository.delete(rentalPost);
    }

    public RentalPost updatePost(Long idPost, int newArea, int newPrice, String newCity, String newDistrict, String newCommune, String newStreet, String newNumberhouse, String newDescription, String newTitle, Type newType) {
        Optional<RentalPost> postOptional = postRepository.findById(idPost);
        if (postOptional.isPresent()) {
            RentalPost rentalPost = postOptional.get();
            if (newArea != rentalPost.getArea()) {
                rentalPost.setArea(newArea);
            }
            if (newPrice != rentalPost.getPrice()) {
                rentalPost.setPrice(newPrice);
            }
            if (!Objects.equals(newCity, rentalPost.getCity())) {
                rentalPost.setCity(newCity);
            }
            if (!Objects.equals(newDistrict, rentalPost.getDistrict())) {
                rentalPost.setDistrict(newDistrict);
            }
            if (!Objects.equals(newCommune, rentalPost.getCommune())) {
                rentalPost.setCommune(newCommune);
            }

            if (!Objects.equals(newStreet, rentalPost.getStreet())) {
                rentalPost.setStreet(newStreet);
            }
            if (!Objects.equals(newNumberhouse, rentalPost.getNumberHouse())) {
                rentalPost.setNumberHouse(newNumberhouse);
            }

            String address = newCity + "," + newDistrict + "," + newCommune+ "," + newStreet + "," + newNumberhouse;
            String apiUrl = String.format("https://nominatim.openstreetmap.org/search?q=%s&format=json", address);
            String result = restTemplate.getForObject(apiUrl, String.class);
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(result);
                if (jsonNode.isArray() && jsonNode.size() > 0) {
                    double latitude = jsonNode.get(0).get("lat").asDouble();
                    double longitude = jsonNode.get(0).get("lon").asDouble();
                    rentalPost.setLatitude(latitude);
                    rentalPost.setLongtitude(longitude);
                } else {
                    String address1 = newCity + "," + newDistrict + "," + newCommune+ "," + newStreet;
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
            if (!Objects.equals(newDescription, rentalPost.getDescription())) {
                rentalPost.setDescription(newDescription);
            }
            if (!Objects.equals(newTitle, rentalPost.getTitle())) {
                rentalPost.setTitle(newTitle);
            }
            if (!Objects.equals(newType, rentalPost.getType())) {
                rentalPost.setType(newType);
            }
            return postRepository.save(rentalPost);
        }
        return null;
    }

    private long generateUniqueId() {
        long currentTime = System.currentTimeMillis();
        Random random = new Random();
        long randomLong = random.nextLong() & Long.MAX_VALUE;
        return currentTime + randomLong;
    }

    public void comment(long postId, String comments) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        Optional<User> users = userRepository.findByUsername(username);
        Comment comment = new Comment();
        Optional<RentalPost> rentalPost = postRepository.findById(postId);
        comment.setComment(comments);
        comment.setId_user(users.get());
        comment.setId_post(rentalPost.get());
        commentRepository.save(comment);
    }


}

