package com.codingcat.commerce.module.security.token;

import static org.assertj.core.api.Assertions.assertThat;

import com.codingcat.commerce.domain.user.User;
import com.codingcat.commerce.domain.user.UserRepository;
import com.codingcat.commerce.module.model.ServiceType;
import com.codingcat.commerce.module.security.CustomUserDetailsService;
import com.codingcat.commerce.module.security.UserPrincipal;
import com.codingcat.commerce.module.security.token.TokenProvider.JWT_STATUS;
import com.codingcat.commerce.module.security.token.TokenProvider.TokenResult;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

@SpringBootTest
class TokenProviderTest {
  @Autowired private TokenProperties tokenProperties;
  @Autowired private TokenProvider tokenProvider;
  @Autowired private UserRepository userRepository;

  @AfterEach
  public void cleanUp(){
    userRepository.deleteAll();
  }

  @DisplayName("================ 유저 정보와 만료 기간을 전달해 토큰을 만들 수 있다. ================ ")
  @Test
  void test(){
    Timestamp currentTimestamp = Timestamp.valueOf(LocalDateTime.now());
    // given
    User testUser = userRepository.save(User.createTestUser());

    // when
    TokenResult accessToken  = tokenProvider.makeToken(TokenType.ACCESS, testUser.toAuth(), currentTimestamp.getTime());

    //then
    String userId = tokenProvider.getAuthIdFromToken(accessToken.token());
    assertThat(testUser.getUserId()).isEqualTo(userId);
  }

  @DisplayName("================ 유효한 토큰은 유효성 검사에 성공한다. ================ ")
  @Test
  void validateTokenOK(){
    // when
    String token = JwtFactory
      .builder().build()
      .createUserToken(tokenProperties, User.createTestUser());

    //then
    JWT_STATUS jwtStatus = tokenProvider.validateToken(token);
    assertThat(jwtStatus).isEqualTo(JWT_STATUS.VALID);
  }

  @DisplayName("================ 만료된 토큰은 유효성 검사에 실패한다. ================ ")
  @Test
  void validateTokenExpired(){
    // when
    String token = JwtFactory
      .builder().expiration(new Date(new Date().getTime() - Duration.ofDays(7).toMillis())).build()
      .createUserToken(tokenProperties, User.createTestUser());

    //then
    JWT_STATUS jwtStatus = tokenProvider.validateToken(token);
    assertThat(jwtStatus).isEqualTo(JWT_STATUS.EXPIRED);
  }

  @DisplayName("================ 토큰 기반으로 인증 정보를 가져올 수 있다. ================ ")
  @Test
  void getAuthentication(){
    // given
    User testUser = userRepository.save(User.createTestUser());
    String token = JwtFactory.builder().build().createUserToken(tokenProperties, testUser);

// when
    UsernamePasswordAuthenticationToken authentication = tokenProvider.getAuthentication(ServiceType.USER, token);

// then
    UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

// UserPrincipal에서 id를 가져오기
    String id = userPrincipal.getId();

    assertThat(id).isEqualTo(testUser.getUserId());
  }
}