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

    import java.util.regex.Pattern;

    @RestController
    @AllArgsConstructor
    @RequestMapping("/api/auth")
    public class AuthController {

        private SnipUserService snipUserService;


        @PostMapping("/public/login")
        public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest){

            if(loginRequest.getUsername() == null){
                return ResponseEntity.badRequest().body("Username is required");
            }

            if(loginRequest.getPassword() == null){
                return ResponseEntity.badRequest().body("Password is required");
            }

            return ResponseEntity.ok(snipUserService.authenticateUser(loginRequest));
        }


        @PostMapping("/public/register")
        public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {

            // Validate username
            if (registerRequest.getUsername() == null || registerRequest.getUsername().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Username is required");
            }

            if (registerRequest.getUsername().trim().length() < 3) {
                return ResponseEntity.badRequest().body("Username must contain at least 3 characters");
            }

            // Validate email
            String email = registerRequest.getEmail();

            if (email == null || email.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Email is required");
            }

            String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

            if (!Pattern.matches(emailRegex, email.trim())) {
                return ResponseEntity.badRequest().body("Please enter a valid email address");
            }

            // Validate password
            String password = registerRequest.getPassword();

            if (password == null || password.isEmpty()) {
                return ResponseEntity.badRequest().body("Password is required");
            }

            if (password.length() < 8) {
                return ResponseEntity.badRequest().body("Password must contain at least 8 characters");
            }

            // Create user after validation succeeds
            SnipUser user = new SnipUser();

            user.setUsername(registerRequest.getUsername().trim());
            user.setPassword(registerRequest.getPassword());
            user.setEmail(email.trim());
            user.setRole("ROLE_USER");

            snipUserService.registerUser(user);

            return ResponseEntity.ok("User registered successfully");
        }

    }
