package hsge.hsgeback.service;

import hsge.hsgeback.dto.common.BasicResponse;
import hsge.hsgeback.dto.match.UserPetMatchDto;
import hsge.hsgeback.entity.Match;
import hsge.hsgeback.entity.Pet;
import hsge.hsgeback.entity.User;
import hsge.hsgeback.event.match.MatchedEventPublisher;
import hsge.hsgeback.exception.ResourceDuplicateException;
import hsge.hsgeback.exception.ResourceNotFoundException;
import hsge.hsgeback.repository.match.MatchRepository;
import hsge.hsgeback.repository.pet.PetRepository;
import hsge.hsgeback.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MatchService {

    private final MatchRepository matchRepository;
    private final UserRepository userRepository;
    private final PetRepository petRepository;
    private final ChatService chatService;
    private final MatchedEventPublisher matchedEventPublisher;

    @Transactional
    public BasicResponse saveMatch(String userEmail, Long petId, Boolean likeValue) {

        User liker = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userEmail));
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new ResourceNotFoundException("Pet", "id", petId.toString()));

        if (matchRepository.existsByPetIdAndUserId(pet.getId(), liker.getId())) {
            throw new ResourceDuplicateException("이미 좋아요한 강아지입니다.");
        }

        Match match = Match.builder()
                .user(liker)
                .pet(pet)
                .likeValue(likeValue)
                .build();
        matchRepository.save(match);

        UserPetMatchDto matchDto = UserPetMatchDto.builder()
                .likerEmail(liker.getEmail())
                .likerNickname(liker.getNickname())
                .petId(pet.getId())
                .petName(pet.getPetName())
                .petOwnerEmail(pet.getUser().getEmail())
                .petOwnerNickname(pet.getUser().getNickname())
                .build();

        if (likeValue) {
            chatService.manageChatMessage(matchDto);
            matchedEventPublisher.publishEvent(matchDto);
        }

        return BasicResponse.of(match.getId(), "좋아요가 정상적으로 되었습니다.");
    }
}
