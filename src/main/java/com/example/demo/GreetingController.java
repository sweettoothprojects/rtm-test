package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class GreetingController {

    @GetMapping("/greet")
    public Map<String, String> greet(@RequestParam(name = "name", defaultValue = "World") String name) {
        return Map.of("message", "Hello, " + name + "!");
    }
}
