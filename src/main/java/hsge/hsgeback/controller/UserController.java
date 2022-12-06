package hsge.hsgeback.controller;

import hsge.hsgeback.dto.request.MypageDto;
import hsge.hsgeback.dto.request.PutDto;
import hsge.hsgeback.dto.response.PetResponseDto;
import hsge.hsgeback.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<MypageDto> updateMyprofile(HttpServletRequest request){
        MypageDto dto = userService.getUserProfile(request);
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }

    @PutMapping("/users")
    public void updateUserNickname(HttpServletRequest request, @RequestBody PutDto putDto) {
        userService.updateUserProfile(request, putDto);
    }


    @PutMapping("/users/radius")
    public void updateUserRadius(HttpServletRequest request, @RequestBody PutDto putDto){
        userService.updateRadius(request,putDto);
    }

    @PutMapping("/users/geolocation")
    public void updateUserLocation(HttpServletRequest request, @RequestBody PutDto putDto) {
        userService.updateLocation(request, putDto);
    }

}
