package com.codingcat.commerce.domain.user;

import com.codingcat.commerce.dto.AddUserRequest;
import com.codingcat.commerce.module.model.ApiResponseUtil;
import com.codingcat.commerce.module.model.ApiResponseVo;
import com.codingcat.commerce.module.model.ServiceType;
import com.codingcat.commerce.module.security.AuthVo;
import com.codingcat.commerce.module.security.token.TokenProvider;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final TokenProvider tokenProvider;

  public ResponseEntity<ApiResponseVo<?>> signUp(AddUserRequest request){
    // 비밀번호 암호화
    request.setPassword(passwordEncoder.encode(request.getPassword()));
    Long savedIdx = userRepository.save(request.toEntity()).getIdx();
    return ApiResponseUtil.sendApiResponse(HttpStatus.OK, "sm.common.success.default", "success", savedIdx, null);
  }

  /*로그인*/
  public ResponseEntity<ApiResponseVo<?>> login(AddUserRequest request) {
    // 아이디 검증
    User user = userRepository.findByUserId(request.toEntity()).orElse(null);
    if(user == null){
      return ApiResponseUtil.sendApiResponse(HttpStatus.NOT_FOUND, "sm.common.fail.invalid_request", "로그인 할 수없는 계정입니다.", null, null);
    }

    // 비밀번호 검증
    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      return ApiResponseUtil.sendApiResponse(HttpStatus.BAD_REQUEST, "sm.common.fail.invalid_invalid_request", "로그인 할 수없는 계정입니다.", null, null);
    }

    // JWT 토큰 생성
    AuthVo authVo = user.toAuth();
    authVo.setServiceType(ServiceType.USER);
    HashMap<String, Object> loginTokenParam =  tokenProvider.generateLoginToken(authVo);
    if(loginTokenParam.containsKey("message")){
      dao.insertAuthLog(authLogVo, "login_fail","token_creation_fail");
      return ApiResponse.builder().status(HttpStatus.UNAUTHORIZED.value()).code("sm.auth_login.fail."+loginTokenParam.get("message").toString()).message("로그인을 할 수 없습니다.").build();
    }

    // ***** 7. 로그인 성공 응답 반환
    NormalLoginResponse result = createLoginResponse(user, loginTokenParam, true);

    return ApiResponseUtil.sendApiResponse(HttpStatus.OK, "sm.common.success.default", "success", user.getUserId(), null);
  }
}
