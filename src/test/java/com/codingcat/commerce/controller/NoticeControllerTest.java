package com.codingcat.commerce.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.codingcat.commerce.domain.notice.NoticeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
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
}