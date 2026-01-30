package com.codingcat.commerce.controller;

import com.codingcat.commerce.domain.notice.Notice;
import com.codingcat.commerce.domain.notice.NoticeService;
import com.codingcat.commerce.dto.AddNoticeRequest;
import com.codingcat.commerce.module.model.ApiResponseUtil;
import com.codingcat.commerce.module.model.ApiResponseVo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NoticeController {
  private final NoticeService noticeService;

  @PostMapping("/api/articles")
  public ResponseEntity<ApiResponseVo<Notice>> addNotice(@Valid @RequestBody AddNoticeRequest addNoticeRequest){
    try{
      Notice savedNotice = noticeService.save(addNoticeRequest);
      ApiResponseVo<Notice> response = ApiResponseVo.<Notice>builder().status(HttpStatus.CREATED).code("success").message("게시물 생성 완료").content(savedNotice).build();
      return ApiResponseUtil.sendApiResponse(response);
    }catch (Exception e){
      return ApiResponseUtil.sendApiResponseFailServer(e);
    }
  }
}
