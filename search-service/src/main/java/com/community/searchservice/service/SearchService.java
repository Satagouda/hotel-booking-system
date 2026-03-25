package com.community.searchservice.service;

import org.springframework.stereotype.Service;

@Service
public class SearchService {

    public String search() {
        return "Search hotels by location, date, filters";
    }
}