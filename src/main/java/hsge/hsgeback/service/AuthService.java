package hsge.hsgeback.service;

import com.amazonaws.services.s3.AmazonS3;
import hsge.hsgeback.constant.Age;
import hsge.hsgeback.constant.Breed;
import hsge.hsgeback.dto.request.SignupDto;
import hsge.hsgeback.dto.response.AgeDto;
import hsge.hsgeback.dto.response.BaseResponseDto;
import hsge.hsgeback.dto.response.BreedDto;
import hsge.hsgeback.dto.response.SignupTokenResponseDto;
import hsge.hsgeback.entity.Pet;
import hsge.hsgeback.entity.PetImg;
import hsge.hsgeback.entity.User;
import hsge.hsgeback.exception.NicknameDuplicateException;
import hsge.hsgeback.repository.PetImgRepository;
import hsge.hsgeback.repository.PetRepository;
import hsge.hsgeback.repository.UserRepository;
import hsge.hsgeback.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
@Slf4j
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PetRepository petRepository;
    private final S3Upload s3Upload;
    private final JWTUtil jwtUtil;

    @Transactional
    public SignupTokenResponseDto createInfo(MultipartFile imgFile, SignupDto signupDto) throws Exception {
        if (checkNicknameDuplicate(signupDto.getNickname())) {
            throw new NicknameDuplicateException("존재하는 닉네임입니다.");
        }
        User user = userRepository.save(signupDto.toUserEntity());
        Pet pet1 = petRepository.save(signupDto.toPetEntity(user));
        PetImg petImg = s3Upload.upload(imgFile, pet1);
        List<PetImg> petImgList = new ArrayList<>();
        petImgList.add(petImg);
        pet1.setPetImg(petImgList);
        String accessToken = jwtUtil.generateAccessToken(Map.of("email", signupDto.getEmail()));
        String refreshToken = jwtUtil.generateRefreshToken(Map.of("email", signupDto.getEmail()));
        return new SignupTokenResponseDto(accessToken, refreshToken);
    }

    public boolean checkNicknameDuplicate(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    public BaseResponseDto<List<Object>> getBreed() {
        List<Object> objects = new ArrayList<>();
        List<Breed> breedList = Arrays.asList(Breed.values());
        for (Breed breed : breedList) {
            BreedDto breedDto = new BreedDto(breed.toString(),breed.getKorean());
            objects.add(breedDto);
        }
        return new BaseResponseDto<>("견종", objects);
    }

    public BaseResponseDto<List<Object>> getAge() {
        List<Object> objects = new ArrayList<>();
        List<Age> ageList = Arrays.asList(Age.values());
        for (Age age : ageList) {
            AgeDto ageDto = new AgeDto(age.toString(),age.getKorean());
            objects.add(ageDto);
        }
        return new BaseResponseDto<>("반려견 나이", objects);
    }

}
