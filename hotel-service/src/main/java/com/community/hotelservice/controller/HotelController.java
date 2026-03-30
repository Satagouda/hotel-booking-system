package com.community.hotelservice.controller;

import com.community.hotelservice.dto.HotelDto;
import com.community.hotelservice.entity.Hotel;
import com.community.hotelservice.service.HotelService;
import com.community.hotelservice.util.Mapper;
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
    public ResponseEntity<HotelDto> create(@Valid @RequestBody HotelDto dto) {
        Hotel hotel = Mapper.toEntity(dto);
        Hotel saved = service.create(hotel);
        return ResponseEntity.ok(Mapper.toDto(saved));
    }

    @GetMapping("/search")
    public List<HotelDto> search(@RequestParam String location) {
        return service.searchByLocation(location)
                .stream()
                .map(Mapper::toDto)
                .toList();
    }

    @GetMapping
    public Page<HotelDto> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        return service.getAll(PageRequest.of(page, size))
                .map(Mapper::toDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("Deleted successfully");
    }
}
