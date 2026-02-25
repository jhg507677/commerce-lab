package com.codingcat.commerce.module.security.token;

import com.codingcat.commerce.service.user.User;
import com.codingcat.commerce.module.model.ServiceType;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import java.util.Date;
@Getter
public class JwtFactory {
  private Date issuedAt = new Date();
  private Date expiration = new Date(new Date().getTime() + Duration.ofDays(14).toMillis());
  private Map<String, Object> claims = Collections.emptyMap();

  @Builder
  public JwtFactory(Date issuedAt, Date expiration, Map<String, Object> claims) {
    this.issuedAt = issuedAt;
    this.expiration = expiration;
    this.claims = claims;
  }

  public static JwtFactory withDefaultValues(){
    return JwtFactory.builder().build();
  }

  public String createUserToken(TokenProperties tokenProperties, User testUser){
    return Jwts.builder()
      .setHeaderParam("SERVICE_TYPE", ServiceType.USER)
      .setIssuer(tokenProperties.getISSUER())
      .setIssuedAt(this.issuedAt)
      .setSubject(testUser.getUserId())
      .claim("IDX", testUser.getIdx())
      .setExpiration(this.expiration)
      .signWith(Keys.hmacShaKeyFor(tokenProperties.getUSER_SECRET().getBytes(StandardCharsets.UTF_8)))
      .compact();
  }
}