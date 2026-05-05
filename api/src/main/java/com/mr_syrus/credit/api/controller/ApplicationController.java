package com.mr_syrus.credit.api.controller;

import com.mr_syrus.credit.api.dto.ApplicationResponseDto;
import com.mr_syrus.credit.api.dto.CreateApplicationDto;
import com.mr_syrus.credit.api.entity.UserEntity;
import com.mr_syrus.credit.api.service.ApplicationService;
import com.mr_syrus.credit.api.service.ClientService;
import com.mr_syrus.credit.api.service.SessionService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

@Controller
public class ApplicationController {

    private final ApplicationService applicationService;
    private final SessionService sessionService;

    public ApplicationController(ApplicationService applicationService, SessionService sessionService) {
        this.applicationService = applicationService;
        this.sessionService = sessionService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<ApplicationResponseDto>> getApplicationsByPassport(
            @RequestParam String passportSeries,
            @RequestParam String passportNumber) {

        List<ApplicationResponseDto> applications = applicationService
                .getApplicationsByPassport(passportSeries, passportNumber);
        return ResponseEntity.ok(applications);
    }

    @PostMapping("/create_application")
    public ResponseEntity<Integer> createApplication(@RequestBody CreateApplicationDto dto,
                                                     HttpServletRequest request) {
        String sessionKey = extractSessionKey(request);
        UserEntity currentUser = sessionService.getUserBySessionKey(sessionKey);
        Integer applicationId = applicationService.createApplication(dto, currentUser);
        return ResponseEntity.ok(applicationId);
    }

    private String extractSessionKey(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) throw new IllegalArgumentException("No session cookie");
        return Arrays.stream(cookies)
                .filter(c -> "session".equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No active session"));
    }
}
