package com.codingcat.commerce.module.model;

import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ApiResponseUtil {
  public static <T> ResponseEntity<ApiResponseVo<?>> sendApiResponse(ApiResponseVo<T> apiResponseVo) {

    // 2️⃣ 에러 로깅
    if (apiResponseVo.getStatus().is4xxClientError() || apiResponseVo.getStatus().is5xxServerError()) {
      if (apiResponseVo.getError() != null) {
        apiResponseVo.getError().printStackTrace();
        log.error("[{}]", apiResponseVo.getCode(), apiResponseVo.getError());
      } else {
        log.error("[{}] {}", apiResponseVo.getCode(), apiResponseVo.getMessage());
      }
    }

    // 3️⃣ HTTP Header 설정 (UTF-8 JSON)
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8));

    // 4️⃣ ResponseEntity 반환
    return ResponseEntity.status(apiResponseVo.getStatus())
      .headers(headers)
      .body(apiResponseVo);
  }

  public static <T> ResponseEntity<ApiResponseVo<T>> sendApiResponse(
    HttpStatus status,
    String code,
    String message,
    T data,
    Exception e
  ) {
    // 에러 로깅
    if (status.is4xxClientError() || status.is5xxServerError()) {
      if (e != null) {
        e.printStackTrace();
        log.error("[" + code + "]", e);
      } else {
        log.error("[{}] {}", code, message);
      }
    }

    // HTTP Header 설정 (UTF-8 JSON)
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8));

    // ApiResponseVo 생성
    ApiResponseVo<T> response = ApiResponseVo.<T>builder()
      .status(status)
      .code(code)
      .message(message)
      .content(data)
      .build();

    return new ResponseEntity<>(response, headers, status);
  }

  public static <T> ResponseEntity<ApiResponseVo<?>> sendApiResponseFailServer(Exception e) {
    if (e != null) e.printStackTrace();

    ApiResponseVo<T> response = ApiResponseVo.<T>builder()
      .status(HttpStatus.INTERNAL_SERVER_ERROR)
      .code("sm.common.fail.server")
      .message("서버에 문제가 발생하여 해당 요청에 실패하였습니다. 관리자에게 문의바랍니다.")
      .content(null)
      .build();

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8));

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
      .headers(headers)
      .body(response);
  }
}