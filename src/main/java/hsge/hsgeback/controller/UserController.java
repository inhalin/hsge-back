package hsge.hsgeback.controller;

import hsge.hsgeback.dto.request.MypageDto;
import hsge.hsgeback.dto.request.UserPetDto;
import hsge.hsgeback.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<MypageDto> getMyProfile(HttpServletRequest request) {
        MypageDto dto = userService.getUserProfile(request);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping
    public void updateMyProfile(HttpServletRequest request, UserPetDto userPetDto) {
        userService.updateUserProfile(request, userPetDto);
    }

    @PutMapping
    public void updateUserNickname(HttpServletRequest request, @RequestBody UserPetDto putDto) {
        userService.updateUserProfile(request, putDto);
    }

    @PutMapping("/radius")
    public void updateUserRadius(HttpServletRequest request, @RequestBody UserPetDto putDto) {
        userService.updateRadius(request, putDto);
    }

    @PutMapping("/geolocation")
    public void updateUserLocation(HttpServletRequest request, @RequestBody UserPetDto putDto) {
        userService.updateLocation(request, putDto);
    }

    @DeleteMapping("/withdrawal")
    public void withdrawUser(HttpServletRequest request) {
        userService.withdraw(request);
    }
}
