package skotels.hotelapp.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import skotels.hotelapp.model.User;
import skotels.hotelapp.payload.MessageResponse;
import skotels.hotelapp.service.implementation.UserDetailsServiceImpl;

@RestController
@RequestMapping("/api/auth")
//@CrossOrigin(origins = "https://skotels.netlify.app")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class LoginController {

    private final UserDetailsServiceImpl userService;
    private final PasswordEncoder encoder;

    public LoginController(UserDetailsServiceImpl userService,
                           PasswordEncoder encoder) {
        this.userService = userService;
        this.encoder = encoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginUser){
        String username = loginUser.getUsername();
        String password = loginUser.getPassword();
        String encodedPassword = this.userService.loadUserByUsername(username).getPassword();
        if (this.encoder.matches(password, encodedPassword)){
            User user1 = this.userService.findByUsernameAndPassword(username, encodedPassword)
                    .orElseThrow(() -> new RuntimeException("InvalidUserCredentials"));
            return ResponseEntity.ok().body(user1);
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/logout")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> logoutUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(null);
        return ResponseEntity.ok(new MessageResponse("logout successful"));
    }
}
