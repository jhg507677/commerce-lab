package com.codingcat.commerce.module.exception;

public class CustomException extends RuntimeException {

  public String getCustomCode() {
    return customCode;
  }

  private final String customCode;  // 에러 코드나 특정 식별자
  public CustomException(String customCode) {
    this.customCode = customCode;
  }
}
