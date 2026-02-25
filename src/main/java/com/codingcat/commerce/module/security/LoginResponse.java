package com.codingcat.commerce.module.security;

import java.time.Instant;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Setter
public class LoginResponse {
  private Long userIdx;
  private String accessToken;
  private Instant accessTokenExpire;
}
