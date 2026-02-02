package com.codingcat.commerce.domain.notice;

import com.codingcat.commerce.dto.AddNoticeRequest;
import com.codingcat.commerce.dto.UpdateNoticeRequest;
import com.codingcat.commerce.module.model.ApiResponseUtil;
import com.codingcat.commerce.module.model.ApiResponseVo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class NoticeService {
  private final NoticeRepository noticeRepository;

  public Notice save(AddNoticeRequest request){
    return noticeRepository.save(request.toEntity());
  }

  public List<Notice> findAll(){
    return noticeRepository.findAll();
  }

  public void deleteById(long id) {
    noticeRepository.deleteById(id);
  }

  // @Transactional : 매칭한 메서드를 하나의 트랜잭션으로 묶는 역할
  @Transactional
  public ResponseEntity<ApiResponseVo<?>> update(Long id, UpdateNoticeRequest request) {
    Notice notice = noticeRepository.findById(id).orElse(null);
    if (notice == null) {
      return ApiResponseUtil.sendApiResponse(HttpStatus.NOT_FOUND, "sm.common.fail.invalid_id", "존재하지 않는 게시물입니다.", null, null);
    }
    notice.update(request.getTitle(), request.getContent());
    return ApiResponseUtil.sendApiResponse(HttpStatus.OK, "sm.common.success.default", "success", notice, null);
  }
}
