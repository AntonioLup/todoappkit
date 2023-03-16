package com.todoapp.authRequestJwt.auth;

import com.todoapp.authRequestJwt.config.JwtService;
import com.todoapp.authRequestJwt.dao.TokenRepo;
import com.todoapp.authRequestJwt.dao.UserRepo;
import com.todoapp.authRequestJwt.token.Token;
import com.todoapp.authRequestJwt.token.TokenType;
import com.todoapp.authRequestJwt.user.Role;
import com.todoapp.authRequestJwt.user.UserModel;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    @Autowired
    private final UserRepo repository;
    @Autowired
    private final TokenRepo tokenRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final AuthenticationManager authenticationManager;
    @Transactional
    public AuthenticationResponse register(RegisterRequest request){
        var user = UserModel.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .role(Role.USER)
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        var saveUser = repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        saveUserToken(saveUser, jwtToken);
        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .build();
    }
    @Transactional
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = repository.findByUsername(request.getUsername())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
    @Transactional
    private void revokeAllUserTokens(UserModel user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
    @Transactional
    private void saveUserToken(UserModel saveUser, String jwtToken) {
        var token = Token.builder()
                .user(saveUser)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }
}
