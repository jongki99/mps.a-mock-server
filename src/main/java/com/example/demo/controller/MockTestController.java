package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
public class MockTestController {

    @GetMapping("*")
    public String getAll(@RequestParam String productId) {
        log.debug("test");
        return "Test";
    }
 
    
    @GetMapping("/mpPoint/move")
    public String getMessageOrderEmailBody(@RequestParam String productId) {
    	log.debug("test");
    	return "Test";
    }
    
}
