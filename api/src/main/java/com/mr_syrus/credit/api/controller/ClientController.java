package com.mr_syrus.credit.api.controller;

import com.mr_syrus.credit.api.dto.CodeVerificationDto;
import com.mr_syrus.credit.api.dto.CreateApplicationDto;
import com.mr_syrus.credit.api.dto.RegistrationClientDto;
import com.mr_syrus.credit.api.entity.UserEntity;
import com.mr_syrus.credit.api.service.ApplicationService;
import com.mr_syrus.credit.api.service.ClientService;
import com.mr_syrus.credit.api.service.SessionService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

@RestController
@RequestMapping("/api/client")
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody RegistrationClientDto dto) {
        String codeId = clientService.register(dto);
        return ResponseEntity.ok(Map.of("codeId", codeId));
    }

    @PostMapping("/register/confirm")
    public ResponseEntity<String> confirmRegistration(@RequestBody CodeVerificationDto dto) {
        clientService.confirmRegistration(dto);
        return ResponseEntity.ok("Registration confirmed successfully");
    }
}
