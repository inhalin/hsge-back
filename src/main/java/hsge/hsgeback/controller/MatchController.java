package hsge.hsgeback.controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import hsge.hsgeback.dto.match.UserPetMatchDto;
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
    public ResponseEntity<Void> match(HttpServletRequest request, @PathVariable Long petId, @RequestBody hsge.hsgeback.dto.request.UserPetInterestDto interestDto) throws FirebaseMessagingException {
        UserPetMatchDto matchDto = matchService.saveMatch(jwtUtil.getEmail(request), petId, interestDto.getLike());
        matchService.sendMatchNotification(matchDto);
        return ResponseEntity.ok().build();
    }
}
