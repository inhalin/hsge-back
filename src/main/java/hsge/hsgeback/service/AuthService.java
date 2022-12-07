package hsge.hsgeback.service;

import hsge.hsgeback.constant.Age;
import hsge.hsgeback.constant.Breed;
import hsge.hsgeback.dto.request.NicknameDuplicateRequestDto;
import hsge.hsgeback.dto.request.SignupDto;
import hsge.hsgeback.dto.response.AgeDto;
import hsge.hsgeback.dto.response.BaseResponseDto;
import hsge.hsgeback.dto.response.BreedDto;
import hsge.hsgeback.entity.User;
import hsge.hsgeback.repository.PetRepository;
import hsge.hsgeback.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class AuthService {
    @Value("${spring.servlet.multipart.location}")
    private String pathName;
    private final UserRepository userRepository;
    private final PetRepository petRepository;

    @Transactional
    public void createInfo(MultipartFile imgFile, SignupDto signupDto) throws IOException {
        User user = userRepository.save(signupDto.toUserEntity());
        petRepository.save(signupDto.toPetEntity(user));

        UUID uuid = UUID.randomUUID();
        String fileName = uuid + "_" + imgFile.getOriginalFilename();
        File saveFile = new File(pathName, fileName);
        imgFile.transferTo(saveFile);
        log.info("nickname1 : {}", signupDto.getNickname());
    }

    public boolean checkNicknameDuplicate(NicknameDuplicateRequestDto nicknameDto) {
        return userRepository.existsByNickname(nicknameDto.getNickname());
    }

    public BaseResponseDto<List<Object>> getBreed() {
        List<Object> objects = new ArrayList<>();
        List<Breed> breedList = Arrays.asList(Breed.values());
        for (Breed breed : breedList) {
            BreedDto breedDto = new BreedDto();
            breedDto.setKey(breed.toString());
            breedDto.setValue(breed.getKorean());
            objects.add(breedDto);
        }
        return new BaseResponseDto<>("견종", objects);
    }

    public BaseResponseDto<List<Object>> getAge() {
        List<Object> objects = new ArrayList<>();
        List<Age> ageList = Arrays.asList(Age.values());
        for (Age age : ageList) {
            AgeDto ageDto = new AgeDto();
            ageDto.setKey(age.toString());
            ageDto.setValue(age.getKorean());
            objects.add(ageDto);
        }
        return new BaseResponseDto<>("반려견 나이", objects);
    }

}
