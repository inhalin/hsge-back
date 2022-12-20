package hsge.hsgeback.controller;

import hsge.hsgeback.dto.firebase.FcmTokenDto;
import hsge.hsgeback.dto.request.NicknameDuplicateRequestDto;
import hsge.hsgeback.dto.request.SignupDto;
import hsge.hsgeback.dto.response.BaseResponseDto;
import hsge.hsgeback.dto.response.NicknameDuplicateResponseDto;
import hsge.hsgeback.dto.response.SignupTokenResponseDto;
import hsge.hsgeback.service.AuthService;
import hsge.hsgeback.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.representer.BaseRepresenter;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;
    private final JWTUtil jwtUtil;

    @PostMapping(value = "/api/auth/signup", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<SignupTokenResponseDto> signup(@RequestPart MultipartFile imgFile, @RequestPart SignupDto signupDto) throws IOException {
        SignupTokenResponseDto dto = authService.createInfo(imgFile, signupDto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping("/api/auth/duplicate-nickname")
    public ResponseEntity<NicknameDuplicateResponseDto> checkDuplicateNickname(@RequestBody NicknameDuplicateRequestDto nicknameDto) {
        NicknameDuplicateResponseDto dto = new NicknameDuplicateResponseDto();
        if (authService.checkNicknameDuplicate(nicknameDto.getNickname())) {
            dto.setData(false);
        } else {
            dto.setData(true);
        }
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/api/common/breed")
    public BaseResponseDto<List<Object>> getBreed() {
        return authService.getBreed();
    }

    @GetMapping("/api/common/age")
    private BaseResponseDto<List<Object>> getAge() {
        return authService.getAge();
    }

    @PostMapping("/api/auth/fcm/token")
    public ResponseEntity<String> saveFcmToken(HttpServletRequest request, @RequestBody FcmTokenDto tokenDto) {
        String email = jwtUtil.getEmail(request);
        authService.saveFcmToken(email, tokenDto.getToken());
        return ResponseEntity.ok("기기 토큰이 정상적으로 저장되었습니다.");
    }

    @DeleteMapping("/api/auth/fcm/token")
    public ResponseEntity<String> deleteFcmToken(HttpServletRequest request) {
        authService.deleteFcmToken(jwtUtil.getEmail(request));
        return ResponseEntity.ok("기기 토큰이 정상적으로 삭제되었습니다.");
    }
}
