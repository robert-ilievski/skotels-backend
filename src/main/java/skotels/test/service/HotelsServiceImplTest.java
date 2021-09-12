package skotels.test.service;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import skotels.hotelapp.model.Hotels;
import skotels.hotelapp.repository.HotelsRepository;
import skotels.hotelapp.service.implementation.HotelsServiceImpl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HotelsServiceImplTest {

    @Mock
    private HotelsRepository hotelsRepository;

    @InjectMocks
    private HotelsServiceImpl hotelsService;

    @Test
    public void shouldSuccessfullyFindHotelById() {
        // given
        String id = "id";
        Date date = Date.from(Instant.now());
        Hotels hotel = Hotels.builder()._id(new ObjectId(date)).build();

        when(hotelsRepository.findById(id)).thenReturn(java.util.Optional.ofNullable(hotel));

        // when
        Optional<Hotels> result = hotelsService.findHotelById(id);

        // then
        verify(hotelsRepository, atLeast(2)).findById(any());
        assertThat(result).isNotNull();
        assertThat(result.get().get_id()).isEqualTo(hotel.get_id());
    }

    @Test
    public void shouldUnsuccessfullyFindHotelById() {
        // given
        String id = "id";

        when(hotelsRepository.findById(id)).thenReturn(Optional.empty());
        // when
        Optional<Hotels> result = hotelsService.findHotelById(id);

        // then
        verify(hotelsRepository).findById(id);
        assertThat(result).isNull();
    }

    @Test
    public void shouldSuccessfullyFindHotelsByName() {
        // given
        String name = "name";
        Hotels hotel1 = Hotels.builder().name("name1").build();
        Hotels hotel2 = Hotels.builder().name("name2").build();
        List<Hotels> hotels = new ArrayList<>();
        hotels.add(hotel1);
        hotels.add(hotel2);

        when(hotelsRepository.findAllByNameContains(name)).thenReturn(hotels);

        // when
        List<Hotels> result = hotelsService.findHotelsByName(name);

        // then
        verify(hotelsRepository).findAllByNameContains(name);
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result).isEqualTo(hotels);
    }

    @Test
    public void shouldUnsuccessfullyFindHotelsByName() {
        // given
        String name = "name";

        when(hotelsRepository.findAllByNameContains(name)).thenReturn(new ArrayList<>());

        // when
        List<Hotels> result = hotelsService.findHotelsByName(name);

        // then
        verify(hotelsRepository).findAllByNameContains(name);
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    @Test
    public void shouldSuccessfullySaveHotel() {
        // given
        Hotels hotel = Hotels.builder().name("name").build();

        when(hotelsRepository.save(any())).thenReturn(hotel);

        // when
        Hotels result = hotelsService.saveHotel(hotel);

        // then
        verify(hotelsRepository).save(any());
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(hotel);
    }

    @Test
    public void shouldSuccessfullyListAllHotels() {
        // given
        Hotels hotel1 = Hotels.builder().name("name1").build();
        Hotels hotel2 = Hotels.builder().name("name2").build();
        List<Hotels> hotels = new ArrayList<>();
        hotels.add(hotel1);
        hotels.add(hotel2);

        when(hotelsRepository.findAll()).thenReturn(hotels);

        // when
        List<Hotels> result = hotelsService.listAll();

        // then
        verify(hotelsRepository).findAll();
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result).isEqualTo(hotels);
    }

    @Test
    public void shouldUnsuccessfullyListAllHotels() {
        // given
        when(hotelsRepository.findAll()).thenReturn(new ArrayList<>());

        // when
        List<Hotels> result = hotelsService.listAll();

        // then
        verify(hotelsRepository).findAll();
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    @Test
    public void shouldSuccessfullySortHotelsByStars() {
        // given
        Hotels hotel1 = Hotels.builder().name("name1").stars("5").build();
        Hotels hotel2 = Hotels.builder().name("name2").stars("3").build();
        List<Hotels> hotelsSorted = new ArrayList<>();
        hotelsSorted.add(hotel1);
        hotelsSorted.add(hotel2);

        when(hotelsRepository.findAll(Sort.by(Sort.Direction.DESC, "stars"))).thenReturn(hotelsSorted);

        // when
        List<Hotels> result = hotelsService.sortDescendingByStars();

        // then
        verify(hotelsRepository).findAll(Sort.by(Sort.Direction.DESC, "stars"));
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
    }

    @Test
    public void shouldUnsuccessfullySortHotelsByStars() {
        // given
        when(hotelsRepository.findAll(Sort.by(Sort.Direction.DESC, "stars"))).thenReturn(new ArrayList<>());

        // when
        List<Hotels> result = hotelsService.sortDescendingByStars();

        // then
        verify(hotelsRepository).findAll(Sort.by(Sort.Direction.DESC, "stars"));
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    @Test
    public void shouldSuccessfullySortHotelsByName() {
        // given
        Hotels hotel1 = Hotels.builder().name("name1").build();
        Hotels hotel2 = Hotels.builder().name("name2").build();
        List<Hotels> hotelsSorted = new ArrayList<>();
        hotelsSorted.add(hotel1);
        hotelsSorted.add(hotel2);

        when(hotelsRepository.findAll(Sort.by(Sort.Direction.ASC, "name"))).thenReturn(hotelsSorted);

        // when
        List<Hotels> result = hotelsService.sortAscendingAlphabetic();

        // then
        verify(hotelsRepository).findAll(Sort.by(Sort.Direction.ASC, "name"));
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
    }

    @Test
    public void shouldUnsuccessfullySortHotelsByName() {
        // given
        when(hotelsRepository.findAll(Sort.by(Sort.Direction.ASC, "name"))).thenReturn(new ArrayList<>());

        // when
        List<Hotels> result = hotelsService.sortAscendingAlphabetic();

        // then
        verify(hotelsRepository).findAll(Sort.by(Sort.Direction.ASC, "name"));
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }
}
