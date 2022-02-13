package com.khafizov.simplerestapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CatController {
    private static final String RETURN_PING_STRING = "Cats Service. Version 0.1";

    @GetMapping("/ping")
    public String ping() {
        return RETURN_PING_STRING;
    }
}
