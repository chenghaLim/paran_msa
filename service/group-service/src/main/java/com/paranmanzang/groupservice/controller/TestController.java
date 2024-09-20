package com.paranmanzang.groupservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/api/groups/{test}")
    public String test(@PathVariable String test){
        return test;
    }
}
