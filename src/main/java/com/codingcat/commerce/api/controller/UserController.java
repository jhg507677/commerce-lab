package com.codingcat.commerce.api.controller;

import com.codingcat.commerce.domain.user.UserService;
import com.codingcat.commerce.dto.AddUserRequest;
import com.codingcat.commerce.module.model.ApiResponseVo;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    @Valid @RequestBody AddUserRequest request
  ){
    return userService.signUp(request);
  }

  @Operation(summary = "로그인", description = "")
  @PostMapping("/api/public/v1/user/login")
  public ResponseEntity<ApiResponseVo<?>> signIn(
    @Valid @RequestBody AddUserRequest request
  ){
    return userService.login(request);
  }

}


