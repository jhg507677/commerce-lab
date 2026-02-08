package com.codingcat.commerce.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.codingcat.commerce.domain.notice.Notice;
import com.codingcat.commerce.domain.notice.NoticeRepository;
import com.codingcat.commerce.dto.AddNoticeRequest;
import com.codingcat.commerce.dto.UpdateNoticeRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
class NoticeControllerTest {
  @Autowired protected MockMvc mockMvc;
  @Autowired protected ObjectMapper objectMapper; // 직렬화, 역직렬화를 위한 클래스
  @Autowired private WebApplicationContext context;
  @Autowired NoticeRepository noticeRepository;

  @BeforeEach
  public void setMockMvc(){
    this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    noticeRepository.deleteAll();
  }

  @DisplayName("공지사항 글 추가")
  @Test
  public void addArticle() throws Exception{
    // given
    final String url = "/api/articles";
    final String title = "title";
    final String content = "content";
    final AddNoticeRequest request = new AddNoticeRequest(title, content);

    // 객채 -> JSON으로 역직렬화
    final String requestBody = objectMapper.writeValueAsString(request);

    // when
    ResultActions result = mockMvc.perform(
      post(url)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(requestBody)
    );

    result.andExpect(status().isCreated());
    List<Notice> notices = noticeRepository.findAll();

    assertThat(notices.size()).isEqualTo(1);
    assertThat(notices.get(0).getTitle()).isEqualTo(title);
  }

  @DisplayName("블로그 글 목록 조회에 성공한다.")
  @Test
  void findAllNoticeList() throws Exception {
    // given
    final String url = "/api/articles";
    final String title = "title";
    final String content = "content";

    noticeRepository.save(Notice.builder().title(title).content(content).build());

    ResultActions result = mockMvc.perform(get(url)
      .accept(MediaType.APPLICATION_JSON));

    // when
    // 저장부터 하고

    result
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[0].content").value(content))
      .andExpect(jsonPath("$[0].title").value(title));
  }

  @DisplayName("게시물 삭제")
  @Test
  void deleteNotice() throws Exception {
    // given
    final String url = "/api/articles/{idx}";
    Notice notice = Notice.builder()
      .title("title")
      .content("content")
      .build();
    Notice savedNotice = noticeRepository.save(notice);

    // when
    mockMvc.perform(delete(url, savedNotice.getId())).andExpect(status().isOk());

    // then
    List<Notice> noticeList = noticeRepository.findAll();
    assertThat(noticeList).isEmpty();
  }

  @DisplayName("게시물 수정")
  @Test
  void updateNotice() throws Exception {
    // given
    final String url = "/api/articles/{idx}";
    Notice notice = Notice.builder()
      .title("title")
      .content("content")
      .build();
    Notice savedNotice = noticeRepository.save(notice);
    final String newTitle = "new title";
    final String newContent = "new content";
    UpdateNoticeRequest request = new UpdateNoticeRequest(newTitle, newContent);


    // when
    ResultActions result = mockMvc.perform(
      put(url, savedNotice.getId())
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(request))
      ).andExpect(status().isOk());

    result.andExpect(status().isOk());
    Notice noticeResult = noticeRepository.findById(savedNotice.getId()).get();
    assertThat(noticeResult.getTitle()).isEqualTo(newTitle);
    assertThat(noticeResult.getContent()).isEqualTo(newContent);
  }
}