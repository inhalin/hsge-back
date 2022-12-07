package hsge.hsgeback.controller;

import hsge.hsgeback.dto.request.NicknameDuplicateRequestDto;
import hsge.hsgeback.dto.request.SignupDto;
import hsge.hsgeback.dto.response.BaseResponseDto;
import hsge.hsgeback.dto.response.NicknameDuplicateResponseDto;
import hsge.hsgeback.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/api/auth/signup", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public void signup(@RequestPart MultipartFile imgFile, @RequestPart SignupDto signupDto) throws IOException {
        authService.createInfo(imgFile, signupDto);
    }

    @PostMapping("/api/auth/duplicate-nickname")
    public ResponseEntity<NicknameDuplicateResponseDto> checkDuplicateNickname(@RequestBody NicknameDuplicateRequestDto nicknameDto) {
        NicknameDuplicateResponseDto dto = new NicknameDuplicateResponseDto();
        if (authService.checkNicknameDuplicate(nicknameDto)) {
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

}
