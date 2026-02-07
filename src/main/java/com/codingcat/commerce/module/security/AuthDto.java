package com.codingcat.commerce.module.security;

import com.codingcat.commerce.module.model.ServiceType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class AuthDto {
  private Long userIdx;
  private Long adminIdx;
  private String userId;
  private String email;
  private ServiceType serviceType;
  public String getAuthId(){
    if(serviceType == ServiceType.USER) return userId;
    else return email;
  }
  public Long getAuthIdx(){
    if(serviceType == ServiceType.USER) return userIdx;
    else return adminIdx;
  }
}
