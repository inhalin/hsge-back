package hsge.hsgeback;

import hsge.hsgeback.constant.Age;
import hsge.hsgeback.constant.Breed;
import hsge.hsgeback.dto.request.PetImgDto;
import hsge.hsgeback.entity.Pet;
import hsge.hsgeback.entity.PetImg;
import hsge.hsgeback.entity.User;
import hsge.hsgeback.repository.PetImgRepository;
import hsge.hsgeback.repository.PetRepository;
import hsge.hsgeback.repository.user.UserRepository;
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
@Component
public class DataInit {

    private final UserRepository userRepository;
    private final PetRepository petRepository;
    private final PetImgRepository petImgRepository;

    @PostConstruct
    void init() {

        log.info("dump data init when profile dev");





        String[] name = {"홍석주", "신예빈", "이서윤", "안정은","이태민"};
        String[] email = {"somefood@naver.com", "syb8200@naver.com", "haebalagi77@naver.com", "junnyanne806@naver.com","a@naver.com"};
        String[] petName = {"포카칩", "수미칩", "오징어집", "감자칩","막시무스","페퍼","후추","먼지","꾸끼","쿠쿠"};
        String[] likeTag = {"#남자사람,#아이,#공놀이,","#터그놀이,#아이,#사람,","#수영,#대형견,#중형견,","#소형견,#옷입기,#사진찍기,","#잠자기,#간식,#인형,"};
        String[] disLikeTag = {"#큰소리,#옷입기,#사진찍기,","#향수,#큰소리,#스킨십,","#꼬리만지기,#발만지기,#뽀뽀,","#수영,#사진찍기,#옷입기,","#소형견,#중형견,#암컷,"};
        String[] gender = {"여","남","여","남","여"};
        Boolean[] neut = {true,false,true,false,true,false,true,false,true,false};
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
            petImgDto.setUuid(key+originalFileName);
            petImgRepository.save(petImgDto.toPetImgEntity(pet1));

            Pet pet2 = Pet.builder()
                    .breed(Breed.MIX)
                    .age(Age.FIVE_MONTHS)
                    .user(user1)
                    .gender(gender[i])
                    .neutralization(neut[temp-1])
                    .petName(petName[temp-1])
                    .description("산책을 좋아해요!")
                    .likeTag(likeTag[i])
                    .dislikeTag(disLikeTag[i])
                    .build();
            petRepository.save(pet2);
            petImgRepository.save(petImgDto.toPetImgEntity(pet2));
            temp--;
        }
    }
}