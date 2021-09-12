package skotels.hotelapp.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import skotels.hotelapp.model.Hotels;
import skotels.hotelapp.service.HotelsService;

import java.util.List;

@RestController
@RequestMapping("/api/hotels")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class HotelsController {

    private final HotelsService hotelsService;

    public HotelsController(HotelsService hotelsService) {
        this.hotelsService = hotelsService;
    }

    // Return the hotels
    @GetMapping("/all")
    public List<Hotels> getAllHotels() {
        return this.hotelsService.listAll();
    }

    // Find hotels by given name
    @PostMapping("/searchHotels")
    public List<Hotels> findAllByName(@RequestParam String search){
        return this.hotelsService.findHotelsByName(search);
    }

    // Save hotel in the db
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/save")
    public ResponseEntity<Hotels> saveHotel(@RequestBody Hotels hotel){
        return new ResponseEntity<>(this.hotelsService.saveHotel(hotel), HttpStatus.CREATED);
    }

    // Delete hotel from the db
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/delete")
    public ResponseEntity<String> deleteHotel(@RequestBody Hotels h){
        String name = h.getName();
        this.hotelsService.deleteHotelByName(name);
        return new ResponseEntity<>("Successfully deleted hotel", HttpStatus.OK);
    }

    // Sort hotels by stars in descending order
    @GetMapping("/sortbystars")
    public List<Hotels> sortHotelsByStars(){
        return this.hotelsService.sortDescendingByStars();
    }

    // Sort hotels - alphabetic order
    @GetMapping("/sortalphabetic")
    public List<Hotels> sortHotelsAlphabetic() {
        return this.hotelsService.sortAscendingAlphabetic();
    }
}
