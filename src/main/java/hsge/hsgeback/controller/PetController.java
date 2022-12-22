package hsge.hsgeback.controller;

import hsge.hsgeback.dto.request.SignupDto;
import hsge.hsgeback.dto.request.UserPetDto;
import hsge.hsgeback.dto.response.PetInfoResponseDto;
import hsge.hsgeback.dto.response.PetResponseDto;
import hsge.hsgeback.service.PetService;
import hsge.hsgeback.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api")
public class PetController {

    /**
     * 1. 회원 비밀번호, 닉네임(중복확인), 프로필 사진(15종 아이콘 제공) 변경 -> 일단 비밀번호, 닉네임(수정시 중복확인 포함시켜서)
     * 2. 프로필 사진, 이름, 성별, 중성화 여부, 나이, 품종 ,Like 태그, Dislike 태그, 자기소개 수정
     */
    private final PetService petService;

    private final JWTUtil jwtUtil;

    @GetMapping("/pets/area")
    public ResponseEntity<List<PetResponseDto>> findAllUser(HttpServletRequest request) {
        String email = jwtUtil.getEmail(request);
        List<PetResponseDto> dto = petService.findPetByLocation(email);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    // GET 특정 유저의 반려견 리스트 /api/users/{userId}/pets
    @GetMapping("/users/{userId}/pets")
    public ResponseEntity<List<PetInfoResponseDto>> getPets(@PathVariable Long userId) {
        List<PetInfoResponseDto> petList = petService.getAllPet(userId);
        return new ResponseEntity<>(petList, HttpStatus.OK);
    }

    // GET 특정 반려견 보기 /api/pets/{petId}
    @GetMapping("/pets/{petId}")
    public ResponseEntity<PetInfoResponseDto> getOnePet(@PathVariable Long petId) {
        PetInfoResponseDto pet = petService.getOnePet(petId);
        return new ResponseEntity<>(pet, HttpStatus.OK);
    }

    // GET 본인 반려견 리스트 보기 /api/pets
    @GetMapping("/pets")
    public ResponseEntity<List<PetInfoResponseDto>> getMyPet(HttpServletRequest request) {
        String email = jwtUtil.getEmail(request);
        List<PetInfoResponseDto> myPet = petService.getMyPet(email);
        return new ResponseEntity<>(myPet, HttpStatus.OK);
    }

    // POST 본인 반려견 생성 /api/pets
    @PostMapping(value = "/pets",consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public void postPet(HttpServletRequest request, @RequestPart SignupDto signupDto, @RequestPart MultipartFile imgFile) throws IOException {
        String email = jwtUtil.getEmail(request);
        petService.postPet(email, signupDto, imgFile);
    }

    // PUT 반려견 수정 /api/pets/{petId}
    // 본인 강아지 제외해서 수정 시도시 예외처리
    @PutMapping(value = "/pets/{petId}",consumes = {MediaType.APPLICATION_JSON_VALUE,MediaType.MULTIPART_FORM_DATA_VALUE})
    public void putPet(HttpServletRequest request, @PathVariable Long petId, @RequestPart("content") UserPetDto userPetDto, @RequestPart(required = false) MultipartFile imgFile) throws IOException {
//        if (imgFile == null){
//            log.info("imgFile : {}", imgFile);
//            String email = jwtUtil.getEmail(request);
//            petService.updatePetWithoutImg(email,petId,userPetDto);
//        }
            log.info(" 있음 : {}");
            String email = jwtUtil.getEmail(request);
            petService.updatePet(email, petId, userPetDto, imgFile);
    }

    // DELETE 반려견 삭제 /api/pets/{petId}
    @DeleteMapping("/pets/{petId}")
    public void deletePet(@PathVariable Long petId) {
        petService.deletePet(petId);
    }
}
