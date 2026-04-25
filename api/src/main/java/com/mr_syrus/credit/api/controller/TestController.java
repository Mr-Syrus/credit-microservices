package com.mr_syrus.credit.api.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @PostMapping("/test")
    public String test(@RequestBody(required = false) String body) {
        return "OK, body length: " + (body == null ? 0 : body.length());
    }
}