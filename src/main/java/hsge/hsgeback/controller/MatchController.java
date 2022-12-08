package hsge.hsgeback.controller;

import hsge.hsgeback.dto.request.UserPetInterestDto;
import hsge.hsgeback.service.MatchService;
import hsge.hsgeback.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MatchController {

    private final MatchService matchService;
    private final JWTUtil jwtUtil;

    @PostMapping("/pets/{petId}/interest")
    public ResponseEntity<Void> match(HttpServletRequest request, @PathVariable Long petId, @RequestBody UserPetInterestDto interestDto) {
        matchService.setUserToPetInterest(jwtUtil.getEmail(request), petId, interestDto.getLike());
        return ResponseEntity.ok().build();
    }
}
