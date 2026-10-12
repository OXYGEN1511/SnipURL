package com.myproject.snipurl.controllers;
import com.myproject.snipurl.dtos.LoginRequest;

import com.myproject.snipurl.dtos.RegisterRequest;
import com.myproject.snipurl.models.SnipUser;

import com.myproject.snipurl.services.SnipUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private SnipUserService snipUserService;


    @PostMapping("/public/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(snipUserService.authenticateUser(loginRequest));
    }


    @PostMapping("/public/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest){
        SnipUser user = new SnipUser();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(registerRequest.getPassword());
        user.setEmail(registerRequest.getEmail());
        user.setRole("ROLE_USER");
        snipUserService.registerUser(user);
        return ResponseEntity.ok("User registered successfully");
    }

}
