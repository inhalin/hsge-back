package hsge.hsgeback.controller;

import hsge.hsgeback.dto.request.PutDto;
import hsge.hsgeback.dto.response.PetInfoResponseDto;
import hsge.hsgeback.dto.response.PetResponseDto;
import hsge.hsgeback.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class PetController {

    /**
     * 1. 회원 비밀번호, 닉네임(중복확인), 프로필 사진(15종 아이콘 제공) 변경 -> 일단 비밀번호, 닉네임(수정시 중복확인 포함시켜서)
     * 2. 프로필 사진, 이름, 성별, 중성화 여부, 나이, 품종 ,Like 태그, Dislike 태그, 자기소개 수정
      */
    private final PetService petService;

    @GetMapping("/pets/area")
    public ResponseEntity<List<PetResponseDto>> findAllUser(HttpServletRequest request) {
        List<PetResponseDto> dto = petService.findPetByLocation(request);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    // GET 특정 유저의 반려견 리스트 /api/users/{userId}/pets
    @GetMapping("/users/{userId}/pets")
    public ResponseEntity<List<PetInfoResponseDto>> getPets(@PathVariable Long userId){
        List<PetInfoResponseDto> petList = petService.getAllPet(userId);
        return new ResponseEntity<>(petList,HttpStatus.OK);
    }
}
