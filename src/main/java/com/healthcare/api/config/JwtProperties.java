package com.healthcare.api.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Propriedades do JWT, validadas na inicialização da aplicação.
 *
 * Se "jwt.secret" estiver ausente ou em branco, a aplicação falha ao subir
 * com uma mensagem clara em vez de um erro mais tarde, em tempo de
 * execução, quando alguém tentar logar.
 *
 * Isso é especialmente importante no perfil "prod", onde não há valor
 * default para o secret (veja application-prod.yml).
 */
@Validated
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    @NotBlank(message = "jwt.secret não pode estar vazio. Defina a variável de ambiente JWT_SECRET.")
    private String secret;

    @Positive(message = "jwt.expiration-ms deve ser um valor positivo.")
    private long expirationMs;

    @Positive(message = "jwt.refresh-expiration-ms deve ser um valor positivo.")
    private long refreshExpirationMs;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getExpirationMs() {
        return expirationMs;
    }

    public void setExpirationMs(long expirationMs) {
        this.expirationMs = expirationMs;
    }

    public long getRefreshExpirationMs() {
        return refreshExpirationMs;
    }

    public void setRefreshExpirationMs(long refreshExpirationMs) {
        this.refreshExpirationMs = refreshExpirationMs;
    }
}