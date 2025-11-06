package com.kozi.koziAPI.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @PostMapping("/register")
    public ResponseEntity<String> registrar() {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                .body("registrar: pendiente de implementación");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login() {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                .body("login: pendiente de implementación");
    }
}
