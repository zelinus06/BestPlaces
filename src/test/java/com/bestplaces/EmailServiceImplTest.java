//package com.bestplaces;
//
//
//import static org.junit.Assert.assertEquals;
//import static org.mockito.Mockito.*;
//
//import com.bestplaces.Entity.User;
//import com.bestplaces.Repository.UserRepository;
//import com.bestplaces.Service.Impl.EmailServiceImpl;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//public class EmailServiceImplTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @InjectMocks
//    private EmailServiceImpl emailService;
//
//    @Before
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//    }
//
//    @Test
//    public void testGetEmailById() {
//        // Tạo một đối tượng User giả
//        User mockUser = new User();
//        mockUser.setId(1L);
//        mockUser.setEmail("test@example.com");
//
//        // Mock userRepository.findById
//        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(mockUser));
//
//        // Gọi hàm cần test
//        String email = emailService.getEmailById(1L);
//
//        // Kiểm tra kết quả
//        assertEquals("test@example.com", email);
//    }
//}
//
