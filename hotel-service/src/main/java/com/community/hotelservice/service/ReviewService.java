package com.community.hotelservice.service;

import com.community.hotelservice.entity.Review;
import com.community.hotelservice.repo.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository repository;

    public Review addReview(Review review) {
        return repository.save(review);
    }

    public List<Review> getReviews(Long hotelId) {
        return repository.findByHotelId(hotelId);
    }
}
