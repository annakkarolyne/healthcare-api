package com.healthcare.api.service;

import com.healthcare.api.dto.LoginRequest;
import com.healthcare.api.dto.LoginResponse;
import com.healthcare.api.entity.Usuario;
import com.healthcare.api.repository.UsuarioRepository;
import com.healthcare.api.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    public AuthService(AuthenticationManager authenticationManager,
                        JwtService jwtService,
                        UsuarioRepository usuarioRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.usuarioRepository = usuarioRepository;
    }

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha())
        );

        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        String token = jwtService.generateToken(usuario.getEmail());
        String refreshToken = jwtService.generateRefreshToken(usuario.getEmail());

        return new LoginResponse(
                token,
                refreshToken,
                "Bearer",
                usuario.getNome(),
                usuario.getRole().name()
        );
    }
}