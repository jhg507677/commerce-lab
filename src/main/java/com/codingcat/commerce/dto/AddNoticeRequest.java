package com.codingcat.commerce.dto;

import com.codingcat.commerce.domain.notice.Notice;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddNoticeRequest {
  @NotBlank(message = "제목은 필수입니다.")
  private String title;

  @NotBlank(message = "내용은 필수입니다.")
  private String content;

  public Notice toEntity(){
    return Notice.builder()
    .title(title)
    .content(content)
    .build();
  }
}
