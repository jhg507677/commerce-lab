package com.codingcat.commerce.module.security;

import static com.codingcat.commerce.module.model.ApiResponseUtil.sendApiResponseFailServer;

import com.codingcat.commerce.domain.user.User;
import com.codingcat.commerce.domain.user.UserRepository;
import com.codingcat.commerce.module.exception.CustomException;
import com.codingcat.commerce.module.model.ApiResponseUtil;
import com.codingcat.commerce.module.model.ApiResponseVo;
import com.codingcat.commerce.module.security.token.RefreshToken;
import com.codingcat.commerce.module.security.token.RefreshTokenRepository;
import com.codingcat.commerce.module.security.token.TokenProvider;
import com.codingcat.commerce.module.security.token.TokenProvider.JWT_STATUS;
import com.codingcat.commerce.module.security.token.TokenProvider.TokenResult;
import com.codingcat.commerce.module.security.token.TokenType;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AuthService {
  private final TokenProvider tokenProvider;
  private final RefreshTokenRepository tokenRepository;
  private final UserRepository userRepository;


  // 토큰 생성하기
  public ResponseEntity<ApiResponseVo<?>> generateLoginToken(
    HttpServletResponse response,
    AuthDto auth
  ){
    Timestamp currentTimestamp = Timestamp.valueOf(LocalDateTime.now());
    try {
      TokenResult accessToken = tokenProvider.makeToken(TokenType.ACCESS, auth, currentTimestamp.getTime());
      TokenResult refreshToken = tokenProvider.makeToken(TokenType.REFRESH, auth, currentTimestamp.getTime());
      RefreshToken refreshTokenBox = RefreshToken.builder()
        .refreshToken(refreshToken.token())
        .expiredDateTime(refreshToken.expiresAt())
        .userIdx(auth.getUserIdx())
        .build();
      // 생성한 토큰들 DB에 저장
      tokenRepository.save(refreshTokenBox);

      // 응답
      LoginResponse loginResponse = new LoginResponse();
      loginResponse.setAccessToken(accessToken.token());
      loginResponse.setAccessTokenExpire(accessToken.expiresAt());
      loginResponse.setUserIdx(auth.getUserIdx());

      // Refresh Token을 HttpOnly 쿠키로 설정
      ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken.token())
        .httpOnly(true)
        .secure(true)
        .path("/")
        .maxAge(Duration.ofDays(7))
        .sameSite("Strict")
        .build();

      response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
      return ApiResponseUtil.sendApiResponse(HttpStatus.OK, "sm.common.success.default", "success", loginResponse, null);
    }catch (Exception e){
      return sendApiResponseFailServer(e);
    }
  }

  // refresh 토큰으로 새로운 AccessToken을 갱신
  @Transactional
  public String createNewAccessToken(String refreshToken){
    if(tokenProvider.validateToken(refreshToken) != JWT_STATUS.VALID){
      throw new CustomException(HttpStatus.BAD_REQUEST, "sm.common.fail.invalid_token","올바르지 않은 토큰 정보입니다.");
    }

    RefreshToken dbRefreshToken = tokenRepository.findByRefreshToken(refreshToken)
      .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "sm.common.fail.invalid_token_db", "올바르지 않은 토큰 정보입니다."));

    User user = userRepository.findById(dbRefreshToken.getUserIdx())
      .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "sm.common.fail.invalid_user", "존재하지 않는 유저입니다."));

    Timestamp currentTimestamp = Timestamp.valueOf(LocalDateTime.now());
    TokenResult tokenResult = tokenProvider.makeToken(TokenType.ACCESS, user.toAuth(), currentTimestamp.getTime());
    dbRefreshToken.increaseRefreshCount();

    return tokenResult.token();
  }
}
