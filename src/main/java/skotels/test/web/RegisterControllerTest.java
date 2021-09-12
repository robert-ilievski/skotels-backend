package skotels.test.web;

import org.junit.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import skotels.hotelapp.HotelappApplication;
import skotels.hotelapp.model.User;
import skotels.hotelapp.repository.UserRepository;

@SpringBootTest(classes = HotelappApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RegisterControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    public void destroy() {
        userRepository.findByUsername("newUser").ifPresent(user -> userRepository.delete(user));
    }

    @Test
    public void shouldRegisterSuccessfully() {
        User user = new User("newUser", "newPassword", "ROLE_ADMIN");

        ResponseEntity<String> result =
                restTemplate.postForEntity("/api/auth/signup", user, String.class);

        Assertions.assertTrue(result.hasBody());
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertEquals("User registered successfully!", result.getBody());
    }

    @Test
    public void shouldRegisterUnsuccessfully() {
        User user = new User("admin", "admin", "ROLE_ADMIN");

        ResponseEntity<String> result =
                restTemplate.postForEntity("/api/auth/signup", user, String.class);

        Assertions.assertTrue(result.hasBody());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        Assertions.assertEquals("Error: Username is already taken!", result.getBody());
    }
}
