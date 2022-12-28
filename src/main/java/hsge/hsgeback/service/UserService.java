package hsge.hsgeback.service;

import hsge.hsgeback.dto.redis.LocationDto;
import hsge.hsgeback.dto.redis.WalkDto;
import hsge.hsgeback.dto.request.MypageDto;
import hsge.hsgeback.dto.request.ReportDto;
import hsge.hsgeback.dto.request.UserPetDto;
import hsge.hsgeback.entity.Chatroom;
import hsge.hsgeback.entity.User;
import hsge.hsgeback.repository.ReportRepository;
import hsge.hsgeback.repository.UserRepository;
import hsge.hsgeback.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;

    private final ReportRepository reportRepository;
    private final JWTUtil jwtUtil;
    private final RedisTemplate<String, LocationDto> redisTemplate;


    public MypageDto getUserProfile(String email) {
        Optional<User> optional = userRepository.findByEmail(email);
        if (optional.isEmpty()) {
            throw new IllegalArgumentException();
        }
        User user = optional.get();
        MypageDto mypageDto = new MypageDto();
        mypageDto.setNickname(user.getNickname());
        mypageDto.setTown(user.getTown());
        mypageDto.setProfilePath(user.getProfilePath());
        mypageDto.setLatitude(user.getLatitude());
        mypageDto.setLongtitude(user.getLongitude());
        mypageDto.setRadius(user.getRadius());
        return mypageDto;
    }

    @Transactional
    public void updateUserProfile(String email, UserPetDto userPetDto) {
        Optional<User> optional = userRepository.findByEmail(email);
        User user = optional.orElseThrow();
        user.setNickname(userPetDto.getNickname());
        user.setProfilePath(userPetDto.getProfilePath());
    }

    public User getUser(HttpServletRequest request) {
        String email = jwtUtil.getEmail(request);
        Optional<User> optional = userRepository.findByEmail(email);
        if (optional.isEmpty()) {
            throw new UsernameNotFoundException("유저를 찾을 수 없습니다.");
        }
        User user = optional.get();
        return user;
    }

    @Transactional
    public void updateLocation(String email, UserPetDto userPetDto) {
        Optional<User> optional = userRepository.findByEmail(email);
        if (optional.isEmpty()) {
            throw new IllegalArgumentException();
        }
        User user = optional.get();
        user.setTown(userPetDto.getTown());
        user.setLatitude(userPetDto.getLatitude());
        user.setLongitude(userPetDto.getLongtitude());
    }

    @Transactional
    public void updateRadius(String email, UserPetDto putDto) {
        Optional<User> optional = userRepository.findByEmail(email);
        if (optional.isEmpty()) {
            throw new IllegalArgumentException();
        }
        User user = optional.get();
        user.setRadius(putDto.getRadius());
    }

    @Transactional
    public void withdraw(String email) {
        userRepository.deleteByEmail(email);
    }

    @Transactional
    public void reportUser(String email, ReportDto reportDto) {
        Optional<User> optional = userRepository.findByEmail(email);
        if (optional.isEmpty()) {
            throw new IllegalArgumentException();
        }
        User reporter = optional.get();
        Optional<User> optional1 = userRepository.findById(reportDto.getReportee());
        if (optional1.isEmpty()) {
            throw new IllegalArgumentException();
        }
        User reportee = optional1.get();
        reportee.setReportCount(reportee.getReportCount() + 1);
        if (reportee.getReportCount() >= 3) {
            reportee.setValid(false);
        }
        reportRepository.save(reportDto.toReportEntity(reporter, reportee));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> walkLocation(String email, LocationDto locationDto) {
        Optional<User> optional = userRepository.findByEmail(email);
        User user = optional.orElseThrow();
        ValueOperations<String, LocationDto> vop = redisTemplate.opsForValue();
        vop.set(user.getNickname(), locationDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public LocationDto testRedis(String email) {
        Optional<User> optional = userRepository.findByEmail(email);
        User user = optional.orElseThrow();
        ValueOperations<String, LocationDto> vop = redisTemplate.opsForValue();
        return vop.get(user.getNickname());
    }


    public List<WalkDto> getWalkAround(String email) {
        Optional<User> optional = userRepository.findByEmail(email);
        User user = optional.orElseThrow();
        List<Chatroom> chatroomList = user.getLikeUser();
        List<User> likeUser = chatroomList.stream()
                .filter(Chatroom::getActive)
                .map(Chatroom::getLikedUser)
                .collect(Collectors.toList());
        ValueOperations<String, LocationDto> vop = redisTemplate.opsForValue();
        LocationDto result = vop.get(user.getNickname());
        Double currentLatitude = result.getLatitude();
        Double currentLongitude = result.getLongitude();
        // 3KM 고정
        return likeUser.stream()
                .filter(a -> {
                    if ((currentLatitude - 0.03 < a.getLatitude()) && (currentLatitude + 0.03 > a.getLatitude()) && (currentLongitude - 0.03 < a.getLongitude()) && (currentLongitude + 0.03 > a.getLongitude()))
                        return true;
                    return false;
                }).collect(Collectors.toList()).stream().map(w -> WalkDto.builder()
                        .userId(w.getId())
                        .longitude(w.getLongitude())
                        .latitude(w.getLatitude())
                        .nickname(w.getNickname())
                        .build()).collect(Collectors.toList());

    }
}
