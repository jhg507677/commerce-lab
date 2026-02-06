package com.codingcat.commerce.module.security;

import com.codingcat.commerce.module.security.token.Jwt;
import com.codingcat.commerce.module.security.token.TokenProvider;
import com.codingcat.commerce.module.security.token.TokenProvider.TokenResult;
import com.codingcat.commerce.module.security.token.TokenType;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

RequiredArgsConstructor
@Service
public class AuthService {
  private final TokenProvider tokenProvider;

  // 토큰 생성하기
  public HashMap<String,Object> generateLoginToken(
    AuthVo auth
  ){
    Timestamp currentTimestamp = Timestamp.valueOf(LocalDateTime.now());
    try {
      // Access Token & Refresh Token 발행
      Jwt tokenBox = new Jwt();
      TokenResult accessToken = tokenProvider.generateJwt(TokenType.ACCESS, auth, currentTimestamp.getTime(), tokenBox);
      TokenResult refreshToken = tokenProvider.generateJwt(TokenType.REFRESH, auth, currentTimestamp.getTime(), tokenBox);

      // expiresAt.toEpochMilli();
      // DB에 토큰들 저장
      TokenVo accessTokenVo = new TokenVo(accessWtokenIdx, serviceName, user.getWuserIdx(), accessToken,
        TokenVo.TokenType.access,
        currentTimestamp,
        accessTokenExpires,
        1, ip, tokenPairId);
      result.put("access_token",accessToken);
      result.put("accessTokenExpires",accessTokenExpires);
      try {
        dao.insertWebUserToken(accessTokenVo);
      }catch (Exception e){
        printErrorLog(e);
        result.put("message","access_token_db");
      }

      TokenVo refreshTokenDto = new TokenVo(refreshWtokenIdx, serviceName, user.getWuserIdx(), refreshToken,
        TokenVo.TokenType.refresh, currentTimestamp,
        new Timestamp(jwtProvider.getExpirationFromJwt(refreshToken, serviceName).getTime()),
        1, ip, tokenPairId);
      result.put("refresh_token",refreshToken);
      try {
        dao.insertWebUserToken(refreshTokenDto);
      }catch (Exception e){
        e.printStackTrace();
        result.put("message","refresh_token_db");
      }
      return result;
    }catch (Exception e){
      printErrorLog(e);
      result.put("message","token_exception");
      return result;
    }
  }
}
