package com.codingcat.commerce;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.codingcat.commerce.domain.user.User;
import com.codingcat.commerce.domain.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/*
컨트룰러 테스트
*/
@SpringBootTest
@AutoConfigureMockMvc // MockMvc 생성 및 자동 구성
class TestControllerTest {

  @Autowired protected MockMvc mockMvc;
  @Autowired private WebApplicationContext context;
  @Autowired private UserRepository userRepository;

  @BeforeEach
  public void mockMvcSetUp(){
    this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
  }

  @AfterEach
  public void cleanUp(){
    userRepository.deleteAll();
  }

  @DisplayName("회원 목록 조회 시 저장된 모든 고객 정보를 반환한다.")
  @Test
  void getAllMembers() throws Exception {
    final String url = "/test";
    // given
    User user = userRepository.save(User.createTestUser());

    // when
    final ResultActions result = mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON));

    // then
    result.andExpect(jsonPath("$[0].id").value(user.getUserId()))
      .andExpect(jsonPath("$[0].name").value(user.getName()));
    ;
  }
}
/*
@AutoConfigureMockMvc
- MockMvc를 생성하고 자동으로 구성하는 애너테이션
- MockMvc는 애플리케이션을 서버에 배포하지 않고도 테스트용 MVC 환경을 만들어 요청 및 전송, 응답 기능을 제공하는 유틸리티 클래스
- 즉 컨트롤러를 테스트할때 사용
*/