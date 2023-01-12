package hsge.hsgeback.service;

import com.slack.api.Slack;
import com.slack.api.model.block.Blocks;
import com.slack.api.model.block.LayoutBlock;
import com.slack.api.model.block.composition.BlockCompositions;
import com.slack.api.webhook.WebhookPayloads;
import hsge.hsgeback.dto.redis.LocationDto;
import hsge.hsgeback.dto.redis.NameDto;
import hsge.hsgeback.dto.redis.ResponseDto;
import hsge.hsgeback.dto.request.MypageDto;
import hsge.hsgeback.dto.request.ReportDto;
import hsge.hsgeback.dto.request.UserPetDto;
import hsge.hsgeback.entity.Chatroom;
import hsge.hsgeback.entity.Report;
import hsge.hsgeback.entity.User;
import hsge.hsgeback.repository.ReportRepository;
import hsge.hsgeback.repository.user.UserRepository;
import hsge.hsgeback.repository.user.UserTokenRepository;
import hsge.hsgeback.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    public static final String VENUS_VISITED = "venues_visited";

    private final GeoOperations<String, String> geoOperations;

    private final UserRepository userRepository;
    private final UserTokenRepository userTokenRepository;

    private final ReportRepository reportRepository;
    private final JWTUtil jwtUtil;


    public MypageDto getUserProfile(String email) {
        Optional<User> optional = userRepository.findByEmail(email);
        if (optional.isEmpty()) {
            throw new IllegalArgumentException();
        }
        User user = optional.get();
        MypageDto mypageDto = new MypageDto();
        mypageDto.setNickname(user.getNickname());
        mypageDto.setTown(user.getTown());
        mypageDto.setProfilePath(user.getProfilePath());
        mypageDto.setLatitude(user.getLatitude());
        mypageDto.setLongtitude(user.getLongitude());
        mypageDto.setRadius(user.getRadius());
        return mypageDto;
    }

    @Transactional
    public void updateUserProfile(String email, UserPetDto userPetDto) {
        Optional<User> optional = userRepository.findByEmail(email);
        User user = optional.orElseThrow();
        user.setNickname(userPetDto.getNickname());
        user.setProfilePath(userPetDto.getProfilePath());
    }

    public User getUser(HttpServletRequest request) {
        String email = jwtUtil.getEmail(request);
        Optional<User> optional = userRepository.findByEmail(email);
        if (optional.isEmpty()) {
            throw new UsernameNotFoundException("유저를 찾을 수 없습니다.");
        }
        User user = optional.get();
        return user;
    }

    @Transactional
    public void updateLocation(String email, UserPetDto userPetDto) {
        Optional<User> optional = userRepository.findByEmail(email);
        if (optional.isEmpty()) {
            throw new IllegalArgumentException();
        }
        User user = optional.get();
        user.setTown(userPetDto.getTown());
        user.setLatitude(userPetDto.getLatitude());
        user.setLongitude(userPetDto.getLongtitude());
    }

    @Transactional
    public void updateRadius(String email, UserPetDto putDto) {
        Optional<User> optional = userRepository.findByEmail(email);
        if (optional.isEmpty()) {
            throw new IllegalArgumentException();
        }
        User user = optional.get();
        user.setRadius(putDto.getRadius());
    }

    @Transactional
    public void withdraw(String email) {
        userRepository.deleteByEmail(email);
        userTokenRepository.deleteByEmail(email);
    }

    @Transactional
    public void reportUser(String email, ReportDto reportDto) throws IOException {
        Optional<User> optional = userRepository.findByEmail(email);
        if (optional.isEmpty()) {
            throw new IllegalArgumentException();
        }
        User reporter = optional.get();
        Optional<User> optional1 = userRepository.findById(reportDto.getReportee());
        if (optional1.isEmpty()) {
            throw new IllegalArgumentException();
        }
        User reportee = optional1.get();
        reportee.setReportCount(reportee.getReportCount() + 1);
        reportRepository.save(reportDto.toReportEntity(reporter, reportee));
        if (reportee.getReportCount() > 6) {
            reportee.setValid(false);
            List<Report> byReportee = reportRepository.findByReportee(reportee);
            StringBuilder stringBuilder = new StringBuilder();
            String description = stringBuilder.append(byReportee.get(0).getDescription()).append("\n")
                    .append(byReportee.get(1).getDescription()).append("\n")
                    .append(byReportee.get(2).getDescription()).append("\n")
                    .append(byReportee.get(3).getDescription()).append("\n")
                    .append(byReportee.get(4).getDescription()).append("\n")
                    .append(byReportee.get(5).getDescription()).append("\n")
                    .append(byReportee.get(6).getDescription()).append("\n")
                    .toString();
            List<LayoutBlock> layoutBlocks = Blocks.asBlocks(
                    getHeader("🚨 7번 신고 당한 회원 탄생 🚨"),
                    Blocks.divider(),
                    getSection("☠️ 신고 당한 회원 ☠️ : " + reportee.getNickname()),
                    Blocks.divider(),
                    getSection("✅ 신고한 이유 : "),
                    getSection(description),
                    Blocks.divider());
            Slack.getInstance().send("https://hooks.slack.com/services/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",
                    WebhookPayloads.payload(p -> p.text("유저 신고 알람").blocks(layoutBlocks)));
        }
    }


    public NameDto add(String email, LocationDto locationDto) {
        User findUser = userRepository.findByEmail(email).orElseThrow();
        String key = findUser.getNickname() + ":" + findUser.getId();
        Point point = new Point(locationDto.getLng(), locationDto.getLat());
        geoOperations.add(VENUS_VISITED, point, key);
        return new NameDto(findUser.getNickname());
    }

    public List<ResponseDto> nearByVenues(String email) {
        User findUser = userRepository.findByEmail(email).orElseThrow();
        String key = findUser.getNickname() + ":" + findUser.getId();
        Point myPosition = Objects.requireNonNull(geoOperations.position(VENUS_VISITED, key)).get(0);
        List<ResponseDto> dto = new ArrayList<>();
        Circle circle = new Circle(new Point(myPosition.getX(), myPosition.getY()), new Distance(3, Metrics.KILOMETERS));
        // 백키로 반경에 있는 포인트들 뽑아오기
        GeoResults<RedisGeoCommands.GeoLocation<String>> res = geoOperations.radius(VENUS_VISITED, circle);
        assert res != null;
        List<GeoResult<RedisGeoCommands.GeoLocation<String>>> content = res.getContent();
//      x가 경도 y가 위도
        for (GeoResult<RedisGeoCommands.GeoLocation<String>> c : content) {
            ResponseDto responseDto = new ResponseDto();
            List<Point> position = geoOperations.position(VENUS_VISITED, c.getContent().getName());
            assert position != null;
            for (Point point : position) {
                responseDto.setLat(point.getX());
                responseDto.setLng(point.getY());
                responseDto.setName(c.getContent().getName());
                dto.add(responseDto);
            }
        }

        List<List<Chatroom>> chatrooms = new ArrayList<>();
        chatrooms.add(findUser.getLikeUser());
        chatrooms.add(findUser.getLikedUser());
        List<Chatroom> chatrooms1 = chatrooms.stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
        Set<User> userList = new HashSet<>();
        List<User> like = chatrooms1.stream()
                .filter(Chatroom::getActive)
                .map(Chatroom::getLikeUser)
                .collect(Collectors.toList());
        userList.addAll(like);
        List<User> liked = chatrooms1.stream()
                .filter(Chatroom::getActive)
                .map(Chatroom::getLikedUser)
                .collect(Collectors.toList());
        userList.addAll(liked);

        List<ResponseDto> result = new ArrayList<>();

        for (ResponseDto d : dto) {
            String[] split = d.getName().split(":");
            for (User u : userList) {
                if (!findUser.getNickname().equals(split[0])) {
                    if (u.getNickname().equals(split[0])) {
                        d.setName(split[0]);
                        Long id = Long.valueOf(split[1]);
                        d.setUserId(id);
                        result.add(d);
                    }
                }
            }
        }
        return result;
    }

    public void deleteRedisData(LocationDto locationDto) {
        User findUser = userRepository.findByNickname(locationDto.getName());
        String key = findUser.getNickname() + ":" + findUser.getId();
        geoOperations.remove(VENUS_VISITED, key);
    }


    @NotNull
    private LayoutBlock getSection(String message) {
        return Blocks.section(s ->
                s.text(BlockCompositions.markdownText(message)));
    }


    @NotNull
    private LayoutBlock getHeader(String text) {
        return Blocks.header(h -> h.text(
                BlockCompositions.plainText(pt -> pt
                        .emoji(true)
                        .text(text))));
    }
}
