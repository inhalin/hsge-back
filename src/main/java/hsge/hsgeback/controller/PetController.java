package hsge.hsgeback.controller;

import hsge.hsgeback.dto.request.PutDto;
import hsge.hsgeback.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/pets")
public class PetController {

    /**
     * 1. 회원 비밀번호, 닉네임(중복확인), 프로필 사진(15종 아이콘 제공) 변경 -> 일단 비밀번호, 닉네임(수정시 중복확인 포함시켜서)
     * 2. 프로필 사진, 이름, 성별, 중성화 여부, 나이, 품종 ,Like 태그, Dislike 태그, 자기소개 수정
     */
    private final PetService petService;

    @PutMapping("/{petId}")
    public void putPet(@PathVariable Long petId, @RequestBody PutDto putDto) {
        petService.putPet(petId, putDto);
    }
}
