package hsge.hsgeback.controller;

import hsge.hsgeback.dto.redis.LocationDto;
import hsge.hsgeback.dto.redis.WalkDto;
import hsge.hsgeback.dto.request.MypageDto;
import hsge.hsgeback.dto.request.ReportDto;
import hsge.hsgeback.dto.request.UserPetDto;
import hsge.hsgeback.entity.User;
import hsge.hsgeback.service.UserService;
import hsge.hsgeback.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    private final JWTUtil jwtUtil;

    @GetMapping
    public ResponseEntity<MypageDto> getUserProfile(HttpServletRequest request) {
        String email = jwtUtil.getEmail(request);
        MypageDto dto = userService.getUserProfile(email);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping
    public void updateMyProfile(HttpServletRequest request, @RequestBody UserPetDto userPetDto) {
        String email = jwtUtil.getEmail(request);
        userService.updateUserProfile(email, userPetDto);
    }

    @PutMapping("/radius")
    public void updateUserRadius(HttpServletRequest request, @RequestBody UserPetDto putDto) {
        String email = jwtUtil.getEmail(request);
        userService.updateRadius(email, putDto);
    }

    @PutMapping("/geolocation")
    public void updateUserLocation(HttpServletRequest request, @RequestBody UserPetDto putDto) {
        String email = jwtUtil.getEmail(request);
        userService.updateLocation(email, putDto);
    }

    @DeleteMapping("/withdrawal")
    public void withdrawUser(HttpServletRequest request) {
        String email = jwtUtil.getEmail(request);
        userService.withdraw(email);
    }

    @PostMapping("/report")
    public void reportUser(HttpServletRequest request, @RequestBody ReportDto reportDto) {
        String email = jwtUtil.getEmail(request);
        userService.reportUser(email, reportDto);
    }

    @PostMapping("/currentLocation")
    public ResponseEntity<?> walkLocation(HttpServletRequest request, @RequestBody LocationDto locationDto) {
        String email = jwtUtil.getEmail(request);
        return userService.walkLocation(email, locationDto);
    }

    @GetMapping("/currentLocation")
    public ResponseEntity<LocationDto> getRedisLocation(HttpServletRequest request){
        String email = jwtUtil.getEmail(request);
        LocationDto dto = userService.testRedis(email);
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }

    @GetMapping("/handShake")
    public ResponseEntity<List<WalkDto>> handShake(HttpServletRequest request){
        String email = jwtUtil.getEmail(request);
        List<WalkDto> dto = userService.getWalkAround(email);
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }
}
