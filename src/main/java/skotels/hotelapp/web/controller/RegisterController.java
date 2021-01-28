package skotels.hotelapp.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import skotels.hotelapp.model.User;
import skotels.hotelapp.payload.MessageResponse;
import skotels.hotelapp.service.implementation.UserDetailsServiceImpl;

@RestController
@RequestMapping("/api/auth")
//@CrossOrigin(origins = "https://skotels.netlify.app")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class RegisterController {

    private final UserDetailsServiceImpl userService;
    private final PasswordEncoder encoder;

    public RegisterController(UserDetailsServiceImpl userService,
                              PasswordEncoder encoder) {
        this.userService = userService;
        this.encoder = encoder;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody User newUser) {
        if (this.userService.existsByUsername(newUser.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }
        // Create new user's account
        String username = newUser.getUsername();
        String password = this.encoder.encode(newUser.getPassword());
        if (newUser.getRole() == null){
            this.userService.save(new User(username, password, "ROLE_USER"));
        }
        else if (newUser.getRole().equals("ROLE_ADMIN")) {
            this.userService.save(new User(username, password, "ROLE_ADMIN"));
        }
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}