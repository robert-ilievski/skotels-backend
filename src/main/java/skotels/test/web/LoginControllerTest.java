package skotels.test.web;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import skotels.hotelapp.HotelappApplication;
import skotels.hotelapp.model.User;

@SpringBootTest(classes = HotelappApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LoginControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldLoginSuccessfully() {
        User user = new User("admin", "admin", "ROLE_ADMIN");
        ResponseEntity<User> result =
                restTemplate.postForEntity("/api/auth/login", user, User.class);

        Assertions.assertTrue(result.hasBody());
        Assertions.assertEquals(user.getUsername(), result.getBody().getUsername());
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void shouldLoginUnsuccessfully() {
        User user = new User("admin", "wrongPassword", "ROLE_ADMIN");
        ResponseEntity<User> result =
                restTemplate.postForEntity("/api/auth/login", user, User.class);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    @WithMockUser(username = "username", roles={"ADMIN"})
    public void shouldLogoutSuccessfully() {
        ResponseEntity<String> result =
                restTemplate.postForEntity("/api/auth/logout", null, String.class);

        Assertions.assertTrue(result.hasBody());
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertEquals("logout successfull", result.getBody());
    }
}
