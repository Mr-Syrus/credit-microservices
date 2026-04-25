package com.mr_syrus.credit.api.controller;

import com.mr_syrus.credit.api.dto.CodeVerificationDto;
import com.mr_syrus.credit.api.dto.RegistrationEmployeeDto;
import com.mr_syrus.credit.api.service.ClientService;
import com.mr_syrus.credit.api.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody RegistrationEmployeeDto dto) {
        String codeId = employeeService.register(dto);
        return ResponseEntity.ok(Map.of("codeId", codeId));
    }

    @PostMapping("/register/confirm")
    public ResponseEntity<String> confirmRegistration(@RequestBody CodeVerificationDto dto) {
        employeeService.confirmRegistration(dto);
        return ResponseEntity.ok("Registration confirmed successfully");
    }

}
