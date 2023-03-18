package com.james.api.service;

import org.springframework.stereotype.Service;

@Service
public class SearchService {

    public boolean isEmpty(String parameter1) {
        return !parameter1.isEmpty();
    }
}
