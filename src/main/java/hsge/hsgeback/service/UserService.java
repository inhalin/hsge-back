package hsge.hsgeback.service;

import hsge.hsgeback.dto.request.MypageDto;
import hsge.hsgeback.dto.request.PutDto;
import hsge.hsgeback.dto.response.PetResponseDto;
import hsge.hsgeback.entity.Pet;
import hsge.hsgeback.entity.User;
import hsge.hsgeback.repository.UserRepository;
import hsge.hsgeback.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;


    public MypageDto getUserProfile(HttpServletRequest request){
        User user = getUser(request);
        MypageDto mypageDto = new MypageDto();
        mypageDto.setNickname(user.getNickname());
        mypageDto.setProfilePath(user.getProfilePath());
        mypageDto.setLatitude(user.getLatitude());
        mypageDto.setLongtitude(user.getLongtitude());
        return mypageDto;
    }

    @Transactional
    public void updateUserProfile(HttpServletRequest request, PutDto putDto) {
        User user = getUser(request);
        user.setNickname(putDto.getNickname());
        user.setProfilePath(putDto.getProfilePath());
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
    public void updateLocation(HttpServletRequest request, PutDto putDto) {
        User user = getUser(request);
        user.setLatitude(putDto.getLatitude());
        user.setLongtitude(putDto.getLongtitude());
    }

    @Transactional
    public void updateRadius(HttpServletRequest request, PutDto putDto) {
        User user = getUser(request);
        user.setRadius(putDto.getRadius());
    }




}
