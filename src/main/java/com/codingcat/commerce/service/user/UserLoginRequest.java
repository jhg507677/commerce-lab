package com.codingcat.commerce.service.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginRequest {
  @NotBlank(message = "id값은 필수입니다.")
  private String userId;

  @NotBlank(message = "비빌번호는 필수입니다.")
  private String password;

  public User toEntity(){
    return User.builder()
      .userId(userId)
      .password(password)
      .build();
  }
}
