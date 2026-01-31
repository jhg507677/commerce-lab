package com.codingcat.commerce.domain.notice;

import com.codingcat.commerce.dto.AddNoticeRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
