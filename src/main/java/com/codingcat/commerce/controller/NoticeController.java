package com.codingcat.commerce.controller;

import com.codingcat.commerce.domain.notice.Notice;
import com.codingcat.commerce.domain.notice.NoticeService;
import com.codingcat.commerce.dto.AddNoticeRequest;
import com.codingcat.commerce.dto.NoticeResponse;
import com.codingcat.commerce.module.model.ApiResponseUtil;
import com.codingcat.commerce.module.model.ApiResponseVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController // http응답으로 객체 데이터를 Json 형태로 변환
@RequiredArgsConstructor
public class NoticeController {
  private final NoticeService noticeService;


  @Operation(
    summary = "게시물 생성", description = "새로운 공지사항 게시물을 생성합니다.",
    responses = {@ApiResponse(responseCode = "201", content = @Content(schema = @Schema(implementation = Notice.class)))}
  )
  @PostMapping("/api/articles")
  public ResponseEntity<ApiResponseVo<?>> addNotice(
    @Valid @RequestBody AddNoticeRequest addNoticeRequest
  ){
    try{
      Notice savedNotice = noticeService.save(addNoticeRequest);
      ApiResponseVo<?> response = ApiResponseVo.builder().status(HttpStatus.CREATED).code("sm.common.success.default").message("게시물 생성 완료").content(savedNotice).build();
      return ApiResponseUtil.sendApiResponse(response);
    }catch (Exception e){
      return ApiResponseUtil.sendApiResponseFailServer(e);
    }
  }

  @Operation(summary = "게시물 조회", description = "",
    responses = {@ApiResponse(responseCode = "200" ,content = @Content(schema = @Schema(implementation = NoticeResponse[].class)))}
  )
  @GetMapping("/api/articles")
  public ResponseEntity<ApiResponseVo<?>> getNoticeList(
  ){
    try{
      List<Notice> noticeList = noticeService.findAll();
      List<NoticeResponse> result = noticeList.stream().map(NoticeResponse::new).toList();
      ApiResponseVo<?> response = ApiResponseVo.builder().status(HttpStatus.OK).code("sm.common.success.default").message("게시물 목록 조회")
        .content(result).build();
      return ApiResponseUtil.sendApiResponse(response);
    }catch (Exception e){
      return ApiResponseUtil.sendApiResponseFailServer(e);
    }
  }
}
