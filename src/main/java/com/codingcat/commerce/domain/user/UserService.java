package com.codingcat.commerce.domain.user;

import com.codingcat.commerce.dto.AddUserRequest;
import com.codingcat.commerce.module.model.ApiResponseUtil;
import com.codingcat.commerce.module.model.ApiResponseVo;
import com.codingcat.commerce.module.security.AuthService;
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
  private final AuthService authService;

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

    return authService.generateLoginToken(user.toAuth());
  }
}
