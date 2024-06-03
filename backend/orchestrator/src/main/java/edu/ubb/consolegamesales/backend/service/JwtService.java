package edu.ubb.consolegamesales.backend.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;


@Service
public class JwtService {

    private final String jwtSecret;

    public JwtService(@Value("${jwtSecret}") String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    public boolean verifyToken(String token, UserDetails user) {
        Claims claims = decodeTokenPayload(token);
        // verify username is the same
        return claims.getSubject().equals(user.getUsername())
                // verify token is not expired
                && claims.getExpiration().after(new Date());
    }

    public Long decodeUserId(String token) {
        // returns the userId from the jwt token payload
        Claims claims = decodeTokenPayload(token);
        return claims.get("userId", Long.class);
    }

    private SecretKey getSigninKey() {
        // process the jwt secret key
        byte[] keyBytes = Decoders.BASE64URL.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims decodeTokenPayload(String token) {
        // return all attributes from the token payload
        return Jwts.parser().verifyWith(getSigninKey())
                .build().parseSignedClaims(token).getPayload();
    }

}

