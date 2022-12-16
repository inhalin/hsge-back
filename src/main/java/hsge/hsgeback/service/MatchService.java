package hsge.hsgeback.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import hsge.hsgeback.dto.match.UserPetMatchDto;
import hsge.hsgeback.entity.Match;
import hsge.hsgeback.entity.Pet;
import hsge.hsgeback.entity.User;
import hsge.hsgeback.repository.MatchRepository;
import hsge.hsgeback.repository.PetCustomRepository;
import hsge.hsgeback.repository.UserCustomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static hsge.hsgeback.constant.PushNotification.LIKE_BODY;
import static hsge.hsgeback.constant.PushNotification.LIKE_TITLE;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MatchService {

    private final MatchRepository matchRepository;
    private final UserCustomRepository userRepository;
    private final PetCustomRepository petRepository;
    private final FcmService fcmService;

    @Transactional
    public UserPetMatchDto saveMatch(String userEmail, Long petId, Boolean likeValue) {
        User user = userRepository.findByEmail(userEmail);
        Pet pet = petRepository.findById(petId);

        Match match = Match.builder()
                .user(user)
                .pet(pet)
                .likeValue(likeValue)
                .build();
        matchRepository.save(match);

        return UserPetMatchDto.builder()
                .likerNickname(user.getNickname())
                .petName(pet.getPetName())
                .petOwnerEmail(pet.getUser().getEmail())
                .build();
    }

    public void sendMatchNotification(UserPetMatchDto matchDto) throws FirebaseMessagingException {
        User user = userRepository.findByEmail(matchDto.getPetOwnerEmail());
        String fcmToken = userRepository.getFcmTokenByEmail(user.getEmail());

        String title = LIKE_TITLE.getContent(matchDto.getPetName());
        String body = LIKE_BODY.getContent(matchDto.getLikerNickname());

        Map<String, String> message = new HashMap<>();
        message.put("title", title);
        message.put("body", body);

        log.debug("FCM Message: 강아지 좋아요 푸시 알림");
        log.debug("title = {}, body = {}", title, body);

        fcmService.sendMessageTo(fcmToken, message);
    }
}
