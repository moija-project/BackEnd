package com.example.moija_project.controller;

import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/test")
public class TestController {
    @GetMapping("/")
    public String test() throws IOException {

        return "Hello!";
    }
}
