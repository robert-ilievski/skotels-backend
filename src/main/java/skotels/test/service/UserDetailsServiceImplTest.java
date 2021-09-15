package skotels.test.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import skotels.hotelapp.model.User;
import skotels.hotelapp.repository.UserRepository;
import skotels.hotelapp.service.implementation.UserDetailsServiceImpl;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userService;

    private User user;

    private final static String USERNAME = "username";
    private final static String PASSWORD = "password";

    @BeforeEach
    public void init() {
        user = User.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .role("ROLE_ADMIN")
                .build();
    }
    @Test
    public void shouldLoadUserByUsernameSuccessfully() {
        // given
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.ofNullable(user));

        // when
        UserDetails result = userService.loadUserByUsername(USERNAME);

        // then
        verify(userRepository).findByUsername(USERNAME);
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    public void shouldLoadUserByUsernameUnsuccessfully() {
        // given
        when(userRepository.findByUsername(any())).thenThrow(new UsernameNotFoundException("User not found!"));

        // when + then
        try {
            userService.loadUserByUsername(USERNAME);
        } catch (Exception exception) {
            verify(userRepository).findByUsername(USERNAME);
            assertThatThrownBy(() -> userService.loadUserByUsername(USERNAME))
                    .isInstanceOf(UsernameNotFoundException.class);
        }
    }

    @Test
    public void shouldFindByUsernameAndPasswordSuccessfully() {
        // given
        when(userRepository.findByUsernameAndPassword(USERNAME, PASSWORD)).thenReturn(Optional.ofNullable(user));

        // when
        Optional<User> result = userService.findByUsernameAndPassword(USERNAME, PASSWORD);

        // then
        verify(userRepository).findByUsernameAndPassword(USERNAME, PASSWORD);
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result.get().getUsername()).isEqualTo(user.getUsername());
        assertThat(result.get().getPassword()).isEqualTo(user.getPassword());
    }

    @Test
    public void shouldFindByUsernameAndPasswordUnsuccessfully() {
        // given
        when(userRepository.findByUsernameAndPassword(USERNAME, PASSWORD)).thenReturn(Optional.empty());

        // when
        Optional<User> result = userService.findByUsernameAndPassword(USERNAME, PASSWORD);

        // then
        verify(userRepository).findByUsernameAndPassword(USERNAME, PASSWORD);
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    @Test
    public void existsByUsername() {
        // given
        when(userRepository.existsByUsername(USERNAME)).thenReturn(true);

        // when
        Boolean result = userService.existsByUsername(USERNAME);

        // then
        verify(userRepository).existsByUsername(USERNAME);
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(true);
    }

    @Test
    public void notExistsByUsername() {
        // given
        when(userRepository.existsByUsername(USERNAME)).thenReturn(false);

        // when
        Boolean result = userService.existsByUsername(USERNAME);

        // then
        verify(userRepository).existsByUsername(USERNAME);
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(false);
    }

    @Test
    public void shouldSaveUserSuccessfully(){
        // given
        when(userRepository.save(user)).thenReturn(user);

        // when
        User result = userService.save(user);

        // then
        verify(userRepository).save(user);
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(user);
    }
}
