package hsge.hsgeback.service;

import hsge.hsgeback.entity.Match;
import hsge.hsgeback.entity.Pet;
import hsge.hsgeback.entity.User;
import hsge.hsgeback.exception.ResourceNotFoundException;
import hsge.hsgeback.repository.MatchRepository;
import hsge.hsgeback.repository.PetRepository;
import hsge.hsgeback.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MatchService {

    private final MatchRepository matchRepository;
    private final UserRepository userRepository;
    private final PetRepository petRepository;

    @Transactional
    public void setUserToPetInterest(String userEmail, Long petId, Boolean likeValue) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new ResourceNotFoundException("User", "email", userEmail));
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new ResourceNotFoundException("Pet", "id", petId.toString()));

        Match match = Match.builder()
                .user(user)
                .pet(pet)
                .likeValue(likeValue)
                .build();
        matchRepository.save(match);

        // TODO: 좋아요 받은 사람에게 푸쉬 알림 보내기 & 채팅방 생성(상단 목록에만 보이는 상태)
    }
}
