package com.hubsi.authmicroservice.application.usecases;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Date;
import java.util.Map;

public interface JwtUseCases {

    /** * Extrai o nome de usuário do token JWT.
     *
     * @param token o JWT token
     * @return the username extraido do token jwt
     */
    String extractUsername(String token);

    /**
     * Gera um token JWT para o usuário fornecido.
     *
     * @param userDetails os detalhes do usuário
     * @return o token JWT gerado
     */
    String generateToken(UserDetails userDetails);

    /**
     * Gera um token JWT com reivindicações adicionais.
     *
     * @param extraClaims as reivindicações adicionais a serem incluídas no token
     * @param userDetails os detalhes do usuário
     * @return o token JWT gerado com as reivindicações adicionais
     */
    String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);

    /**
     * Verifica se o token JWT é válido para os detalhes do usuário fornecidos.
     *
     * @param token o token JWT a ser verificado
     * @param userDetails os detalhes do usuário
     * @return true se o token for válido, false caso contrário
     */
    boolean isTokenValid(String token, UserDetails userDetails);

    /**
     * Verifica se o token JWT está expirado.
     *
     * @param token o token JWT a ser verificado
     * @return true se o token estiver expirado, false caso contrário
     */
    boolean isTokenExpired(String token);

    /**
     * Extrai a data de expiração do token JWT.
     *
     * @param token o token JWT
     * @return a data de expiração do token
     */
    Date extractExpiration(String token);

    /**
     * Extrai todas as reivindicações do token JWT.
     *
     * @param token o token JWT
     * @return as reivindicações extraídas do token
     */
    Claims extractAllClaims(String token);

    /**
     * Extrai todas as reivindicações do token JWT como um mapa.
     *
     * @return um mapa contendo as reivindicações extraídas do token
     */
    Key getSignInKey();

    /**
     * Verifica se o token JWT é válido.
     *
     * @param token o token JWT a ser verificado
     * @return true se o token for válido, false caso contrário
     */
    boolean isTokenValid(String token);
}
