package com.mr_syrus.credit.api.controller;

import com.mr_syrus.credit.api.dto.CodeVerificationDto;
import com.mr_syrus.credit.api.dto.SendCodeDto;
import com.mr_syrus.credit.api.entity.SessionEntity;
import com.mr_syrus.credit.api.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/send_code")
    public String sendCode(@RequestBody SendCodeDto dto) {
        return authService.sendCode(dto.getMail(), dto.getPassportSeries(), dto.getPassportNumber());
    }

    @PostMapping("/auth")
    public String auth(@RequestBody CodeVerificationDto dto,
                       HttpServletRequest request,
                       HttpServletResponse response) {
        String ipAddress = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        SessionEntity session = authService.authenticate(dto.getCodeId(), dto.getCode(), ipAddress, userAgent);

        Cookie cookie = new Cookie("session", session.getSessionKey());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 2); // 2 часа
        response.addCookie(cookie);

        return "OK";
    }
}