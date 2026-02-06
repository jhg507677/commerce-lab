package com.codingcat.commerce.module.security.token;

import com.codingcat.commerce.module.model.ServiceType;
import com.codingcat.commerce.module.security.AuthVo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.SignatureException;

@Slf4j
@Service
public class TokenProvider implements InitializingBean{
  @Value("${jwt.admin_secret_key}") private String JWT_ADMIN;
  @Value("${jwt.user_secret_key}") private String JWT_USER;
  @Value("${jwt.access_expire_time}") private long ACCESS_TOKEN_EXPIRE_TIME_MS;
  @Value("${jwt.refresh_expire_time}") private long REFRESH_TOKEN_EXPIRE_TIME_MS;
  private static final int REFRESH_TOKEN_USE_LIMIT = 5;

  public final String TOKEN_PREFIX = "Bearer ";
  public final String HEADER_AUTHORIZATION = "Authorization";

  private Key ADMIN_KEY;
  private Key USER_KEY;
  @Override
  public void afterPropertiesSet(){
    this.ADMIN_KEY = Keys.hmacShaKeyFor(JWT_ADMIN.getBytes(StandardCharsets.UTF_8));
    this.USER_KEY = Keys.hmacShaKeyFor(JWT_USER.getBytes(StandardCharsets.UTF_8));
  }
  // ****************************************************************************

  public enum JWT_STATUS {
    VALID,
    UNSUPPORTED,
    MALFORMED,
    INVALID_SIGNATURE,
    EXPIRED,
    ERROR
  }


  public record TokenResult(
    String token,
    Instant expiresAt
  ) {}

  public TokenResult generateJwt(
    TokenType tokenType,
    AuthVo auth,
    long curTimestamp,
    Jwt tokenBox
  ) {
    Key secretKey = getSecretKeyByServiceType(auth.getServiceType());

    long expireTimeMs = switch (tokenType) {
      case ACCESS -> ACCESS_TOKEN_EXPIRE_TIME_MS;
      case REFRESH -> REFRESH_TOKEN_EXPIRE_TIME_MS;
    };

    Instant expiresAt = Instant.ofEpochMilli(curTimestamp + expireTimeMs);

    String token = Jwts.builder()
      .setIssuedAt(new Date(curTimestamp))
      .setSubject(String.valueOf(auth.getAuthIdx()))   // sub
      .claim("serviceType", auth.getServiceType().name())
      .setExpiration(Date.from(expiresAt))
      .signWith(secretKey)
      .compact();

    return new TokenResult(token, expiresAt);
  }


  public Integer getUserIdx(String jwt) {
    String userIdx = getClaims(jwt).getBody().get("USER_IDX").toString();
    return Integer.parseInt(userIdx);
  }

  public Date getExpirationFromJwt(String jwt) {
    return getClaims(jwt).getBody().getExpiration();
  }

  public String getTokenIdFromJwt(String jwt) {
    return getClaims(jwt).getBody().getId();
  }



  public JWT_STATUS validateJwt(String jwt) {
    try {
      getClaims(jwt);
    } catch (UnsupportedJwtException uje) {
      return JWT_STATUS.UNSUPPORTED;
    } catch (MalformedJwtException mje) {
      return JWT_STATUS.MALFORMED;
    } catch (ExpiredJwtException eje) {
      return JWT_STATUS.EXPIRED;
    } catch (SignatureException se) {
      return JWT_STATUS.INVALID_SIGNATURE;
    } catch (IllegalArgumentException iae) {
      return JWT_STATUS.MALFORMED;
    } catch (Exception e) {
      e.printStackTrace();
      return JWT_STATUS.ERROR;
    }
    return JWT_STATUS.VALID;
  }

  private Jws<Claims> getClaims(String jwt) {
    Key secret_key = resolveSecretKeyByJwt(jwt);
    try {
      return Jwts.parserBuilder().setSigningKey(secret_key).build().parseClaimsJws(jwt);
    } catch (Exception e) {
      throw e;
    }
  }

  // Prefix 부분 삭제하기
  public String deleteTokenPrefix(String _token){
    if(_token.startsWith(TOKEN_PREFIX))
      return _token.substring(TOKEN_PREFIX.length());
    else
      return _token;
  }

  // 발행된 JWT를 보고 서비스 타입을 가져와 그게 맞는 SecretKey 가져오기
  private Key resolveSecretKeyByJwt(String jwt){
    return switch (getServiceTypeByJwt(jwt)) {
      case USER -> this.USER_KEY;
      case ADMIN -> this.ADMIN_KEY;
      default -> null;
    };
  }

  private Key getSecretKeyByServiceType(ServiceType serviceType){
    return switch (serviceType) {
      case USER -> this.USER_KEY;
      case ADMIN -> this.ADMIN_KEY;
      default -> null;
    };
  }

  // 발행된 JWT를 보고 서비스 타입 가져오기
  public ServiceType getServiceTypeByJwt(String jwt) {
    int i = jwt.lastIndexOf('.');
    String jwtWithoutSignature = jwt.substring(0, i+1);
    return ServiceType.valueOf(Jwts.parserBuilder().build().parseClaimsJwt(jwtWithoutSignature).getHeader().get("SERVICE_NAME").toString());
  }

  public boolean isAccessTokenExpired(String jwtToken,  ServiceType serviceType) {
    try {
      // JWT 파서 생성
      Jws<Claims> claims = getClaims(jwtToken);
      ;
      // JWT의 만료 시간 (expiration) 가져오기
      Date expiration = claims.getBody().getExpiration();

      if (expiration == null) {
        return false; // 만료 시간이 없는 경우는 유효하다고 판단
      }

      // 현재 시간과 만료 시간 비교
      long currentTimeMillis = System.currentTimeMillis();
      long expirationTimeMillis = expiration.getTime();

      // 만료된 시간 차이 (밀리초)
      long timeDifference = currentTimeMillis - expirationTimeMillis;

      // 시간이 만료되지 않아도 리프레시 토큰을 과도하게 호출한 경우 만료 처리
      long maxRetryTimeMillis = REFRESH_TOKEN_USE_LIMIT * ACCESS_TOKEN_EXPIRE_TIME_MS; // 5번 * 30분
      if (timeDifference > maxRetryTimeMillis) {
        return true; // 토큰 만료
      }

      return false; // 토큰 유효

    } catch (Exception e) {
      // JWT 파싱 오류 처리 (잘못된 토큰 등)
      e.printStackTrace();
      return true; // 오류가 발생한 경우 만료된 것으로 간주
    }
  }
}
