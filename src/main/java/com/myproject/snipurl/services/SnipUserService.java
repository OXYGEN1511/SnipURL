package com.myproject.snipurl.services;


import com.myproject.snipurl.models.SnipUser;
import com.myproject.snipurl.repositories.SnipUserRepository;
import com.myproject.snipurl.security.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SnipUserService {
    private PasswordEncoder passwordEncoder;
    private SnipUserRepository userRepository;
    private AuthenticationManager authenticationManager;
    private JwtUtils jwtUtils;

    public SnipUser registerUser(SnipUser user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);

    }









}
