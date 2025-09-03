package com.pragma.powerup.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtValidator {

  @Value("${jwt.secret}")
  private String secretKey;

  public Claims validateAndExtractClaims(String token) {
    SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
  }

  public boolean isValid(String token) {
    try {
      validateAndExtractClaims(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public String extractUserId(String token) {
    Claims claims = validateAndExtractClaims(token);
    Object userIdClaim = claims.get("userId");
    return userIdClaim != null ? userIdClaim.toString() : null;
  }

  public String extractEmail(String token) {
    Claims claims = validateAndExtractClaims(token);
    return claims.getSubject();
  }

  public String extractRole(String token) {
    Claims claims = validateAndExtractClaims(token);

    // Intentar primero "roles" (array) y luego "role" (string) para compatibilidad
    Object rolesObj = claims.get("roles");
    if (rolesObj instanceof java.util.List) {
      @SuppressWarnings("unchecked")
      java.util.List<String> rolesList = (java.util.List<String>) rolesObj;
      return rolesList.isEmpty() ? null : rolesList.get(0);
    }

    // Fallback a formato singular
    return claims.get("role", String.class);
  }
}
