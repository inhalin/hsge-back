package hsge.hsgeback.service;

import hsge.hsgeback.dto.request.MypageDto;
import hsge.hsgeback.dto.request.ReportDto;
import hsge.hsgeback.dto.request.UserPetDto;
import hsge.hsgeback.entity.User;
import hsge.hsgeback.repository.ReportRepository;
import hsge.hsgeback.repository.UserRepository;
import hsge.hsgeback.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;

    private final ReportRepository reportRepository;
    private final JWTUtil jwtUtil;


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
        mypageDto.setLongtitude(user.getLongtitude());
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
        user.setLongtitude(userPetDto.getLongtitude());
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
        reportRepository.save(reportDto.toReportEntity(reporter, reportee));
    }
}
