package com.codingcat.commerce;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.codingcat.commerce.study.QuizController.QuizCode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
class QuizControllerTest {
  @Autowired protected MockMvc mockMvc;
  @Autowired WebApplicationContext context;

  // objectMapper는 Jackson 라이브러리에서 제공하는 클래스로 객체와 Json간의 변환을 처리
  @Autowired private ObjectMapper objectMapper;

  @BeforeEach
  public void setMockMvc(){
    this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
  }

  @DisplayName("quiz() :GET / quiz?code = 1이면 응답 코드는 201, 응답 본문은 Created!을 리턴한다.")
  @Test
  void getQuiz() throws Exception {
    // given
    final String url = "/quiz";

    // when
    final ResultActions result = mockMvc.perform(get(url).param("code","1"));
    result.andExpect(status().isCreated())
      .andExpect(content().string("Created!"));
  }

  @DisplayName("quiz() :GET / quiz?code = 2이면 응답 코드는 400, 응답 본문은 Created!을 리턴한다.")
  @Test
  void getQuiz2() throws Exception {
    final String url = "/quiz";
    final ResultActions result = mockMvc.perform(get(url).param("code","2"));
    result.andExpect(status().isBadRequest()).andExpect(content().string("Bad Request!"));
  }

  @DisplayName("quiz() :POST / quiz?code = 2이면 응답 코드는 400, 응답 본문은 Created!을 리턴한다.")
  @Test
  void postQuiz() throws Exception {
    final String url = "/quiz";
    final ResultActions result = mockMvc.perform(post(url)
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(new QuizCode(1))));
      result.andExpect(status().isForbidden())
        .andExpect(content().string("Forbidden!"));
  }

  @DisplayName("quiz() :POST / quiz?code = 2이면 응답 코드는 400, 응답 본문은 Created!을 리턴한다.")
  @Test
  void postQuiz2() throws Exception {
    final String url = "/quiz";
    final ResultActions reuslt = mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(new QuizCode(13))));
    reuslt.andExpect(status().isOk())
      .andExpect(content().string("OK!"));
  }
}