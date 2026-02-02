package com.codingcat.commerce.module.model;

import static com.codingcat.commerce.module.model.ImportanceLevel.LOG_ONLY;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class ApiResponseVo<T> {
  @Schema(description = "HTTP 상태코드")
  private HttpStatus status;

  @Schema(description = "메시지 코드", example = "sm.common.success.default")
  private String code;

  @Schema(description = "한글 메시지")
  private String message;

  @Schema(description = "한글 메시지(국제화 도입시 사용)")
  private String enMessage;

  @Schema(description = "실제 반환 데이터")
  private T content;

  // 에러 메시지 문자열
  private Exception error;

  @Default
  @Schema(description = "응답 내용을 DB에다가 저장할때 중요도")
  private ImportanceLevel importance = LOG_ONLY;

  public static ApiResponseVo<Object> ok() {
    return ApiResponseVo.builder().status(HttpStatus.OK).code("sm.common.success.default").message("success").build();
  }
}