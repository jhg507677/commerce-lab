package com.codingcat.commerce.module.security;

import static com.codingcat.commerce.module.model.ApiResponseUtil.sendApiResponseFailServer;

import com.codingcat.commerce.domain.user.UserRepository;
import com.codingcat.commerce.module.model.ApiResponseUtil;
import com.codingcat.commerce.module.model.ApiResponseVo;
import com.codingcat.commerce.module.security.token.LoginToken;
import com.codingcat.commerce.module.security.token.LoginTokenRepository;
import com.codingcat.commerce.module.security.token.TokenProvider;
import com.codingcat.commerce.module.security.token.TokenProvider.TokenResult;
import com.codingcat.commerce.module.security.token.TokenType;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {
  private final TokenProvider tokenProvider;
  private final LoginTokenRepository tokenRepository;


  // 토큰 생성하기
  public ResponseEntity<ApiResponseVo<?>> generateLoginToken(
    AuthDto auth
  ){
    Timestamp currentTimestamp = Timestamp.valueOf(LocalDateTime.now());
    try {
      TokenResult accessToken = tokenProvider.generateJwt(TokenType.ACCESS, auth, currentTimestamp.getTime());
      LoginToken accessTokenBox = LoginToken.builder()
        .accessToken(accessToken.token())
        .expiredDateTime(accessToken.expiresAt())
        .userIdx(auth.getUserIdx())
        .build();

      TokenResult refreshToken = tokenProvider.generateJwt(TokenType.REFRESH, auth, currentTimestamp.getTime());
      LoginToken refreshTokenBox = LoginToken.builder()
        .refreshToken(refreshToken.token())
        .expiredDateTime(refreshToken.expiresAt())
        .userIdx(auth.getUserIdx())
        .build();
      // 생성한 토큰들 DB에 저장
      LoginToken savedAccessToken = tokenRepository.save(accessTokenBox);
      LoginToken savedRefreshToken = tokenRepository.save(refreshTokenBox);

      // 응답
      LoginResponse loginResponse = new LoginResponse();
      loginResponse.setAccessToken(savedAccessToken.getAccessToken());
      loginResponse.setAccessTokenExpire(savedAccessToken.getExpiredDateTime());
      loginResponse.setRefreshToken(savedRefreshToken.getRefreshToken());
      return ApiResponseUtil.sendApiResponse(HttpStatus.OK, "sm.common.success.default", "success", loginResponse, null);
    }catch (Exception e){
      return sendApiResponseFailServer(e);
    }
  }
}
