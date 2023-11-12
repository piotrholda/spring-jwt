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
    private final UserMapper userMapper;

    AuthenticationResponse register(RegisterRequest registerRequest) {
        UserEntity user = userMapper.toUserEntity(registerRequest);
        user.setPassword(passwordEncoder.encode(registerRequest.password()));
        user.setRole(Role.ROLE_USER);
        userRepository.save(user);
        return generateAuthenticationResponse(user);
    }

    AuthenticationResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.username(), loginRequest.password()));
        UserEntity user = userRepository.findByUsername(loginRequest.username())
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
