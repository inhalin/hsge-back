package hsge.hsgeback;

import hsge.hsgeback.constant.Age;
import hsge.hsgeback.constant.Breed;
import hsge.hsgeback.entity.Pet;
import hsge.hsgeback.entity.User;
import hsge.hsgeback.repository.PetRepository;
import hsge.hsgeback.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.PrePersist;

@Slf4j
@RequiredArgsConstructor
@Profile("dev")
@Component
public class DataInit {

    private final UserRepository userRepository;
    private final PetRepository petRepository;

    @PostConstruct
    void init() {

        log.info("dump data init when profile dev");

        String[] name = {"홍석주", "신예빈", "이서윤", "안정은","a","b","c","d","e","f","g","h","i","j","k","l","m","n","r","q"};
        String[] email = {"somefood@naver.com", "syb8200@naver.com", "haebalagi77@naver.com", "junnyanne806@naver.com","a@naver.com","sad@naver.com","we@naver.com","qewe754wq@naver.com","qe85wewq@naver.com","qewe675wq@naver.com","q324ewewq@naver.com","qewe3241wq@naver.com","qe52wewq@naver.com","qew23ewq@naver.com","qewe423wq@naver.com","qe2wewq@naver.com","3qewewq@naver.com","qewew4q@naver.com","qew3ewq@naver.com","qew2ewq@naver.com"};
        String[] petName = {"포카칩", "수미칩", "오징어집", "감자칩","포카칩", "수미칩", "오징어집", "감자칩","포카칩", "수미칩", "오징어집", "감자칩","포카칩", "수미칩", "오징어집", "감자칩","포카칩", "수미칩", "오징어집", "감자칩"};
        String[] likeTag = {"#지방,#단거,#도박,","#지방,#단거,#도박,","#지방,#단거,","#지방,#단거,","#지방,#단거,","#지방,","#지방,#단거,","#지방,#도박,","#지방,#단거,#도박,","#지방,#단거,#도박,","#지방,#단거,","#지방,","#지방,#단거,","#지방,","#지방,#단거,#도박,","#지방,#단거,","#지방,#단거,#도박,","#지방,#단거,","#지방,#단거,","#지방,"};

        for (int i = 0; i < 20; i++) {
            User user1 = User.builder()
                    .nickname(name[i])
                    .longtitude(126.980193)
                    .latitude(37.5685019)
                    .role("user")
                    .email(email[i])
                    .profilePath(1)
                    .password("1234")
                    .build();
            userRepository.save(user1);

            Pet pet1 = Pet.builder()
                    .breed(Breed.MIX)
                    .age(Age.FIVE_MONTHS)
                    .user(user1)
                    .gender("남")
                    .neutralization(true)
                    .petName(petName[i])
                    .description("수미칩입니다.")
                    .likeTag(likeTag[i])
                    .dislikeTag("#지방,#단거,")
                    .picture("https://user-images.githubusercontent.com/106398273/204183502-2974fadc-dae6-4238-83ce-3b4acd12373d.jpeg")
                    .build();
            petRepository.save(pet1);

            Pet pet2 = Pet.builder()
                    .breed(Breed.MIX)
                    .age(Age.FIVE_MONTHS)
                    .user(user1)
                    .gender("남")
                    .neutralization(true)
                    .petName(petName[i])
                    .description("수미칩입니다.")
                    .likeTag(likeTag[i])
                    .dislikeTag(likeTag[i])
                    .picture("https://user-images.githubusercontent.com/106398273/204183502-2974fadc-dae6-4238-83ce-3b4acd12373d.jpeg")
                    .build();
            petRepository.save(pet2);
        }




//        for (int i = 1; i <= 50; i++) {
//            User user2 = User.builder()
//                    .nickname("브라질코코넛" + i)
//                    .longtitude(126.980193)
//                    .latitude(37.5685019)
//                    .role("user")
//                    .email("dummy"+ i +"@naver.com")
//                    .profilePath(1)
//                    .password("1234")
//                    .build();
//            userRepository.save(user2);
//
//            Pet pet3 = Pet.builder()
//                    .breed(Breed.MIX)
//                    .age(Age.FIVE_MONTHS)
//                    .user(user2)
//                    .gender("남")
//                    .neutralization(true)
//                    .petName("호날두")
//                    .description("수미칩입니다.")
//                    .likeTag("굳건"+i+"#프로틴"+i+"#BCAA"+i)
//                    .dislikeTag("지방#단거#도박")
//                    .picture("https://user-images.githubusercontent.com/106398273/204183502-2974fadc-dae6-4238-83ce-3b4acd12373d.jpeg")
//                    .build();
//            petRepository.save(pet3);
//
//            Pet pet4 = Pet.builder()
//                    .breed(Breed.MIX)
//                    .age(Age.FIVE_MONTHS)
//                    .user(user2)
//                    .gender("남")
//                    .neutralization(true)
//                    .petName("수미칩")
//                    .description("수미칩입니다.")
//                    .likeTag("굳건"+i+"#프로틴"+i+"#BCAA"+i)
//                    .dislikeTag("지방#단거#도박")
//                    .picture("https://user-images.githubusercontent.com/106398273/204183502-2974fadc-dae6-4238-83ce-3b4acd12373d.jpeg")
//                    .build();
//            petRepository.save(pet4);
//
//        }




//        User user2 = User.builder()
//                .nickname("모로코땅콩")
//                .longtitude(126.980193)
//                .latitude(37.5685019)
//                .role("user")
//                .email("e1e@e.com")
//                .profilePath(1)
//                .password("1234")
//                .build();
//        User user3 = User.builder().nickname("덴마크당나귀").longtitude(126.980193).latitude(37.5685019).role("user").email("eee@e.com").profilePath(1).password("1234").build();
//        User user4 = User.builder().nickname("모로코추파춥스").longtitude(126.980193).latitude(37.5685019).role("user").email("www@e.com").profilePath(1).password("1234").build();
//        User user5 = User.builder().nickname("핀란드브로콜리").longtitude(123.4).latitude(7521.2).role("user").email("qqq@e.com").profilePath(1).password("1234").build();


//        userRepository.save(user2);
//        userRepository.save(user3);
//        userRepository.save(user4);
//        userRepository.save(user5);


//        Pet pet1 = Pet.builder().breed(Breed.MIX).age(Age.FIVE_MONTHS).user(user2).gender("남").neutralization(true).petName("수미칩").description("수미칩입니다.").likeTag("굳건#프로틴#BCAA").dislikeTag("지방#단거#도박").picture("https://user-images.githubusercontent.com/106398273/204183502-2974fadc-dae6-4238-83ce-3b4acd12373d.jpeg").build();
//        Pet pet2 = Pet.builder().breed(Breed.DACHSHUND).age(Age.ELEVEN_MONTHS).user(user1).gender("남").neutralization(false).petName("매운새우깡").description("매운새우깡.").likeTag("3대운동#웨이트#근성장").dislikeTag("유산소#호날두#비염").picture("https://user-images.githubusercontent.com/106398273/204183502-2974fadc-dae6-4238-83ce-3b4acd12373d.jpeg").build();
//        Pet pet3 = Pet.builder().breed(Breed.MALTESE).age(Age.EIGHT_MONTHS).user(user2).gender("여").neutralization(true).petName("존시나").description("존시나입니다.").likeTag("오토바이#자전거#차").dislikeTag("브롤#브로콜리#당나귀").picture("https://user-images.githubusercontent.com/106398273/204183502-2974fadc-dae6-4238-83ce-3b4acd12373d.jpeg").build();
//        Pet pet4 = Pet.builder().breed(Breed.SCHNAUZER).age(Age.NINE_MONTHS).user(user3).gender("여").neutralization(false).petName("아이폰").description("아이폰입니다.").likeTag("축구#노티드#랜디스").dislikeTag("휴식#커피#덴마크").picture("https://user-images.githubusercontent.com/106398273/204183502-2974fadc-dae6-4238-83ce-3b4acd12373d.jpeg").build();
//        petRepository.save(pet2);
//        petRepository.save(pet3);
//        petRepository.save(pet4);
    }
}