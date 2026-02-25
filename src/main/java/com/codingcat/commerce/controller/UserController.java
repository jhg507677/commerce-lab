package com.codingcat.commerce.controller;

import com.codingcat.commerce.service.user.UserService;
import com.codingcat.commerce.service.user.UserCreateRequest;
import com.codingcat.commerce.module.response.ApiResponseVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController {
  private final UserService userService;

  @Operation(summary = "회원가입", description = "")
  @PostMapping("/api/public/v1/user")
  public ResponseEntity<ApiResponseVo<?>> signUp(
    @Valid @RequestBody UserCreateRequest request
  ){
    return userService.signUp(request);
  }

  @Operation(summary = "로그인", description = "")
  @PostMapping("/api/public/v1/user/login")
  public ResponseEntity<ApiResponseVo<?>> signIn(
    HttpServletResponse response,
    @Valid @RequestBody UserCreateRequest request
  ){
    return userService.login(response, request);
  }

  @Operation(summary = "리프레시토큰 재발급", description = "")
  @PostMapping("/api/public/v1/user/refresh")
  public ResponseEntity<ApiResponseVo<?>> refresh(
    @CookieValue(name = "refreshToken") String refreshToken
  ){
    return userService.refresh(refreshToken);
  }
  public record CreateAccessTokenRequest(
    @NotBlank(message = "리프레시 토큰을 필수로 넣어주세요.") String refreshToken
  ) {}


  @Operation(summary = "유저 상세", description = "")
  @Parameters({@Parameter(name = "id", description = "삭제 공지사항 ID", required = true)})
  @PostMapping("/api/v1/user/{idx}")
  public ResponseEntity<ApiResponseVo<?>> signIn(
    @PathVariable(value = "idx") Long idx
  ){
    return userService.getUser(idx);
  }
}


