package com.myproject.snipurl.services;


import com.myproject.snipurl.dtos.LoginRequest;
import com.myproject.snipurl.models.SnipUser;
import com.myproject.snipurl.repositories.SnipUserRepository;
import com.myproject.snipurl.security.jwt.JwtAuthenticationResponse;
import com.myproject.snipurl.security.jwt.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    public JwtAuthenticationResponse authenticateUser(LoginRequest dto){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(),dto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailImpl userDetail =(UserDetailImpl) authentication.getPrincipal();
        String jwt = jwtUtils.generateToken(userDetail);
        return new JwtAuthenticationResponse(jwt);
    }
    public SnipUser findByUsername(String name) {
        return userRepository.findByUsername(name).orElseThrow(
                () -> new UsernameNotFoundException("User not found with username: " + name));
    }








}
