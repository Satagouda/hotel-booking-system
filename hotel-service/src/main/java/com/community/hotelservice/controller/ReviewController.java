package com.community.hotelservice.controller;

import com.community.hotelservice.entity.Review;
import com.community.hotelservice.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService service;

    @PostMapping
    public ResponseEntity<Review> add(@Valid @RequestBody Review review) {
        return ResponseEntity.ok(service.addReview(review));
    }

    @GetMapping("/{hotelId}")
    public List<Review> get(@PathVariable Long hotelId) {
        return service.getReviews(hotelId);
    }
}
