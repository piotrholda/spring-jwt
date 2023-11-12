package com.piotrholda.spring.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;

    AuthenticationResponse register(RegisterRequest registerRequest) {
        UserEntity user = UserEntity.builder()
                .firstname(registerRequest.getFirstname())
                .lastname(registerRequest.getLastname())
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.ROLE_USER)
                .build();
        userRepository.save(user);
        return generateAuthenticationResponse(user);
    }

    AuthenticationResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword()));
        UserEntity user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow();
        return generateAuthenticationResponse(user);
    }

    private AuthenticationResponse generateAuthenticationResponse(UserEntity user) {
        String jwt = tokenProvider.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwt)
                .build();
    }
}
