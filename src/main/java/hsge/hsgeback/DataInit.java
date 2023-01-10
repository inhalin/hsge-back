package hsge.hsgeback;

import hsge.hsgeback.constant.Age;
import hsge.hsgeback.constant.Breed;
import hsge.hsgeback.dto.request.PetImgDto;
import hsge.hsgeback.entity.Pet;
import hsge.hsgeback.entity.PetImg;
import hsge.hsgeback.entity.User;
import hsge.hsgeback.repository.PetImgRepository;
import hsge.hsgeback.repository.chat.ChatroomRepository;
import hsge.hsgeback.repository.chat.MessageRepository;
import hsge.hsgeback.repository.pet.PetRepository;
import hsge.hsgeback.repository.user.UserRepository;
import hsge.hsgeback.entity.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Profile("dev")
//@Component
public class DataInit {

    private final UserRepository userRepository;
    private final PetRepository petRepository;
    private final PetImgRepository petImgRepository;
    private final ChatroomRepository chatroomRepository;
    private final MessageRepository messageRepository;

    @PostConstruct
    void init() {

        log.info("dump data init when profile dev");


        String[] name = {"홍석주", "신예빈", "이서윤", "안정은", "이태민"};
        String[] email = {"somefood@naver.com", "syb8200@naver.com", "haebalagi77@naver.com", "junnyanne806@naver.com", "a@naver.com"};
        String[] petName = {"포카칩", "수미칩", "오징어집", "감자칩", "막시무스", "페퍼", "후추", "먼지", "꾸끼", "쿠쿠"};
        String[] likeTag = {"#남자사람,#아이,#공놀이,", "#터그놀이,#아이,#사람,", "#수영,#대형견,#중형견,", "#소형견,#옷입기,#사진찍기,", "#잠자기,#간식,#인형,"};
        String[] disLikeTag = {"#큰소리,#옷입기,#사진찍기,", "#향수,#큰소리,#스킨십,", "#꼬리만지기,#발만지기,#뽀뽀,", "#수영,#사진찍기,#옷입기,", "#소형견,#중형견,#암컷,"};
        String[] gender = {"여", "남", "여", "남", "여"};
        Boolean[] neut = {true, false, true, false, true, false, true, false, true, false};
        List<PetImg> re = new ArrayList<>();

        int temp = 10;
        for (int i = 0; i < 5; i++) {
            User user1 = User.builder()
                    .nickname(name[i])
                    .longitude(126.980193)
                    .latitude(37.5685019)
                    .role("user")
                    .email(email[i])
                    .profilePath(2131165383)
                    .password("1234")
                    .town("무교동")
                    .build();
            userRepository.save(user1);

            Pet pet1 = Pet.builder()
                    .breed(Breed.MIX)
                    .age(Age.FIVE_MONTHS)
                    .user(user1)
                    .gender(gender[i])
                    .neutralization(neut[i])
                    .petName(petName[i])
                    .description("사람을 좋아해요!")
                    .likeTag(likeTag[i])
                    .dislikeTag(disLikeTag[i])
                    .build();
            petRepository.save(pet1);
            String s3Url = "https://hsge-bucket.s3.ap-northeast-2.amazonaws.com/2fc7389b-6286-4cb7-8bb6-9e6be17fedd0-87e7180607f6d331fce8c4b2d1b395bb.jpg";
            String originalFileName = "87e7180607f6d331fce8c4b2d1b395bb.jpg";
            String key = "2fc7389b-6286-4cb7-8bb6-9e6be17fedd0-";
            UUID uuid = UUID.randomUUID();

            PetImgDto petImgDto = new PetImgDto();
            petImgDto.setOriImgName(originalFileName);
            String savedFileName = uuid.toString() + ".jpg";  // uuid에 확장자만 더해서 저장되는 파일 이름은 uuid.png 이런식으로 저장 된다.
            petImgDto.setS3Url(s3Url);
            petImgDto.setUuid(key + originalFileName);
            petImgRepository.save(petImgDto.toPetImgEntity(pet1));

            Pet pet2 = Pet.builder()
                    .breed(Breed.MIX)
                    .age(Age.FIVE_MONTHS)
                    .user(user1)
                    .gender(gender[i])
                    .neutralization(neut[temp - 1])
                    .petName(petName[temp - 1])
                    .description("산책을 좋아해요!")
                    .likeTag(likeTag[i])
                    .dislikeTag(disLikeTag[i])
                    .build();
            petRepository.save(pet2);
            petImgRepository.save(petImgDto.toPetImgEntity(pet2));
            temp--;
        }

        User user1 = userRepository.findById(5L).orElseThrow();
        User user2 = userRepository.findById(3L).orElseThrow();
        Chatroom chatroom = Chatroom.builder().likeUser(user1).likedUser(user2).build();
        chatroomRepository.save(chatroom);

        Chatroom chatroom1 = createChatroom(2L, 3L);
        Chatroom chatroom2 = createChatroom(2L, 4L);
        Chatroom chatroom3 = createChatroom(2L, 5L);
        createChatroom(4L, 3L);
        createChatroom(5L, 3L);

        Message message1 = new Message("안녕하세요. 홍석주입니다.", chatroom, user1);
        Message message2 = new Message("안녕하세요. 이태민입니다.", chatroom, user2);
        Message message3 = new Message("날씨가 덥네요.", chatroom, user2);
        Message message4 = new Message("그러게요.", chatroom, user1);
        Message message5 = new Message("전 갑니다.", chatroom, user1);
        Message message6 = new Message("서윤님.", chatroom, user1);
        Message message7 = new Message("화이팅", chatroom, user1);
        Message message8 = new Message("!!!", chatroom, user1);

        messageRepository.save(message1);
        messageRepository.save(message2);
        messageRepository.save(message3);
        messageRepository.save(message4);
        messageRepository.save(message5);
        messageRepository.save(message6);
        messageRepository.save(message7);
        messageRepository.save(message8);


    }

    private Chatroom createChatroom(Long userId1, Long userId2) {
        User user1 = userRepository.findById(userId1).orElseThrow();
        User user2 = userRepository.findById(userId2).orElseThrow();
        Chatroom chatroom = Chatroom.builder().likeUser(user1).likedUser(user2).active(false).build();
        return chatroomRepository.save(chatroom);
    }

}