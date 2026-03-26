package com.community.hotelservice.controller;

import com.community.hotelservice.entity.Hotel;
import com.community.hotelservice.service.HotelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService service;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Hotel hotel) {
        return ResponseEntity.ok(service.create(hotel));
    }

    @GetMapping("/search")
    public List<Hotel> search(@RequestParam String location) {
        return service.searchByLocation(location);
    }

    @GetMapping
    public Page<Hotel> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return service.getAll(PageRequest.of(page, size));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("Deleted");
    }
}
