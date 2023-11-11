package com.piotrholda.spring.jwt.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY = "771f81b449e8057b87d97f1f4915cf64976b78ee6854250a092df19384d5e774ffc4d5c49c5063c068e0803689bb2e9b2d9cd4a679cf9943edd982ce25cf03a4764e6d74797628a8f86b4b14198a3ae4cc85aac1c3e24ab4d3022dd54261392dcdd006557d8a4e1b6a2fb90a1b39d876f5af91860d57b2d36205980df043be7df874aa72cb7b461966ecd2091ae5a4b500549f72b2c4c5db27f991ef9859f75f0992909a5dfc7b66b9eedf587f8dc382fa0e876d20b7dfc472aee49f42bdeda6e9885ffbcda9a5415b357ff8a25d0087b562a48e6e08bcbda5890f2c19ffbbe6ce9bec1c31712ce3beab3c70b0d776e8385896ec42735e70ea5ed73a9b57974f";

    public String extractUsername(String jwt) {
        return extractClaim(jwt, Claims::getSubject);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(Map.of(), userDetails);
    }

    boolean isTokenValid(String jwt, UserDetails userDetails) {
        String username = extractUsername(jwt);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(jwt));
    }

    private boolean isTokenExpired(String jwt) {
        return extractClaim(jwt, Claims::getExpiration).before(new Date());
    }

    String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        long currentTimeMillis = System.currentTimeMillis();
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(currentTimeMillis))
                .setExpiration(new Date(currentTimeMillis + 1000 * 60 * 30))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public <T> T extractClaim(String jwt, Function<Claims, T> claimResolver) {
        Claims claims = extractAllClaims(jwt);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String jwt) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] bytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(bytes);
    }
}
