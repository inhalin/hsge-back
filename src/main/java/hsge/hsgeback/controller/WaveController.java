package hsge.hsgeback.controller;

import hsge.hsgeback.dto.common.BasicResponse;
import hsge.hsgeback.service.WaveService;
import hsge.hsgeback.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/waves")
public class WaveController {

    private final JWTUtil jwtUtil;
    private final WaveService waveService;

    @PostMapping("/to/{userId}")
    public ResponseEntity<BasicResponse> wave(HttpServletRequest request, @PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(waveService.wave(jwtUtil.getEmail(request), userId));
    }
}
