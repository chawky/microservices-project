package com.nailic.JwtAuth.services;

import com.nailic.JwtAuth.entities.CurrentUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.logging.Logger;
import javax.crypto.KeyGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class JwtService {
  private static final Logger logger = Logger.getLogger(JwtService.class.getName());

  // Use a fixed secret key - don't generate it randomly
  @Value("${spring.jwt.secret}")
  private static String SECRET_KEY ;

  // Or better yet, inject from configuration
  // @Value("${jwt.secret}")
  // private String secretKey;

  private static Key getSecretKey() {
    // Use the secret key directly with proper key generation
    return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
  }

  public String generateToken(CurrentUser currentUser) {
    return Jwts.builder()
        .signWith(getSecretKey())
        .setSubject(currentUser.getUsername())
        .setIssuedAt(new Date())
        .setExpiration(
            new Date(System.currentTimeMillis() + 99 * 60 * 1000)) // 99 minutes
    //new Date(System.currentTimeMillis() + 10 * 1000)) //10 seconds
        .compact();
  }

  public static Boolean validateToken(String token, UserDetails userDetails) {
    try {
      // Use the same key method for consistency
      Jwts.parser()
          .setSigningKey(getSecretKey())
          .parseClaimsJws(token);
      return getUserNameFromToken(token).equals(userDetails.getUsername());
    } catch (MalformedJwtException | IllegalArgumentException | UnsupportedJwtException | SignatureException e) {
      logger.info("JWT validation failed: " + e.getMessage());
    }
    return false;
  }

  public static String getUserNameFromToken(String token) {
    return Jwts.parser()
        .setSigningKey(getSecretKey())
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }
}
