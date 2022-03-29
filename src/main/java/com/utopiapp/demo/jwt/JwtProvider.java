package com.utopiapp.demo.jwt;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * Clase que genera el token y valida que este bien formado y no este expirado
 */
@Component
public class JwtProvider {

    // Implementamos un logger para ver cual metodo da error en caso de falla
    private final static Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    //Valores que tenemos en el aplicattion.properties
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private int expiration;

    /**
     *setIssuedAt --> Asigna fecha de creción del token
     *setExpiration --> Asigna fecha de expiración
     * signWith --> Firma
     */
    public String generateToken(Map<String, Object> user){

        return Jwts.builder().setClaims(user)
                .signWith(SignatureAlgorithm.HS512, secret)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expiration * 1000L))
                .compact();
    }

    //subject --> user
    public String getEmailFromToken(String token){
        Map<String, Object> user = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return (String) user.get("email");
    }

    public Boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        }catch (MalformedJwtException e){
            logger.error("Token mal formado\n" + e.getMessage());
        }catch (UnsupportedJwtException e){
            logger.error("Token no soportado\n" + e.getMessage());
        }catch (ExpiredJwtException e){
            logger.error("Token expirado\n" + e.getMessage());
        }catch (IllegalArgumentException e){
            logger.error("Token vacio\n" + e.getMessage());
        }catch (SignatureException e){
            logger.error("Fallo con la firma\n" + e.getMessage());
        }
        return false;
    }
}