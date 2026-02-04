package com.codingcat.commerce.module.security.token;

import com.codingcat.commerce.module.model.ServiceType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.SignatureException;

@Slf4j
@Service
public class TokenProvider implements InitializingBean{
  @Value("${jwt.admin_secret_key}") private String JWT_ADMIN_SECRET_KEY;
  @Value("${jwt.user_secret_key}") private String JWT_USER_SECRET_KEY;

  public final String TOKEN_PREFIX = "Bearer ";
  public final String HEADER_AUTHORIZATION = "Authorization";

  private Key ADMIN_KEY;
  private Key USER_KEY;
  @Override
  public void afterPropertiesSet(){
    this.ADMIN_KEY = Keys.hmacShaKeyFor(JWT_ADMIN_SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    this.USER_KEY = Keys.hmacShaKeyFor(JWT_USER_SECRET_KEY.getBytes(StandardCharsets.UTF_8));
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

  public ServiceType getServiceTypeByJwt(String jwt) {
    int i = jwt.lastIndexOf('.');
    String jwtWithoutSignature = jwt.substring(0, i+1);
    return ServiceType.valueOf(Jwts.parserBuilder().build().parseClaimsJwt(jwtWithoutSignature).getHeader().get("SERVICE_NAME").toString());
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
    Key secret_key = getSecretKeyByJwt(jwt);
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

  private Key getSecretKeyByJwt(String jwt){
    return switch (getServiceTypeByJwt(jwt)) {
      case USER -> this.USER_KEY;
      case ADMIN -> this.ADMIN_KEY;
      default -> null;
    };
  }

}
