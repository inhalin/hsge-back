package hsge.hsgeback.service;

import hsge.hsgeback.constant.PushNotification;
import hsge.hsgeback.dto.chat.ChatSimpleDto;
import hsge.hsgeback.dto.match.UserPetMatchDto;
import hsge.hsgeback.entity.Chatroom;
import hsge.hsgeback.entity.Message;
import hsge.hsgeback.entity.User;
import hsge.hsgeback.exception.ResourceNotFoundException;
import hsge.hsgeback.repository.chat.ChatroomRepositoryImpl;
import hsge.hsgeback.repository.chat.ChatroomRepository;
import hsge.hsgeback.repository.chat.MessageRepository;
import hsge.hsgeback.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatroomRepository chatroomRepository;
    private final ChatroomRepositoryImpl chatroomCustomRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    public void manageChatMessage(UserPetMatchDto matchDto) {

        Chatroom prev = chatroomRepository.findByUserEmails(matchDto.getLikerEmail(), matchDto.getPetOwnerEmail());
        if (prev != null) {
            log.info("동일한 견주와의 채팅방이 존재합니다. 메시지를 보냅니다.");

            User liker = userRepository.findById(prev.getLikeUser().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("User - liker", "email", matchDto.getLikerEmail()));

            String content = PushNotification.LIKE_TITLE.getContent(matchDto.getPetName());
            sendMessage(prev, liker, content);
            return;
        }

        Chatroom reverse = chatroomRepository.findByUserEmails(matchDto.getPetOwnerEmail(), matchDto.getLikerEmail());
        if (reverse != null) {
            log.info("당신의 강아지를 좋아하는 사람과의 채팅방이 존재합니다. 메시지를 보납니다.");

            User owner = userRepository.findById(reverse.getLikedUser().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("User - PetOwner", "email", matchDto.getPetOwnerEmail()));

            String content = PushNotification.LIKE_TITLE.getContent(matchDto.getPetName());
            sendMessage(reverse, owner, content);
            return;
        }

        // 기존 채팅방이 없는 경우 채팅방 생성
        saveChatroom(matchDto);
    }

    @Transactional
    public void saveChatroom(UserPetMatchDto matchDto) {

        User liker = userRepository.findByEmail(matchDto.getLikerEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User - Liker", "email", matchDto.getLikerEmail()));
        User petOwner = userRepository.findByEmail(matchDto.getPetOwnerEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User - Pet owner", "email", matchDto.getPetOwnerEmail()));

        Chatroom chatroom = Chatroom.builder()
                .likeUser(liker)
                .likedUser(petOwner)
                .active(false)
                .build();

        chatroomRepository.save(chatroom);

        log.info("새로운 채팅방을 생성하였습니다. 메시지를 보냅니다.");

        String content = PushNotification.LIKE_TITLE.getContent(matchDto.getPetName());

        sendMessage(chatroom, liker, content);
    }

    @Transactional
    public void sendMessage(Chatroom chatroom, User user, String content) {

        Message message = Message.builder()
                .chatroom(chatroom)
                .content(content)
                .user(user)
                .checked(false)
                .build();

        messageRepository.save(message);

        log.info("메시지 전송 완료");
        log.info("보낸 사람: {}, 내용: {}", user.getNickname(), content);
    }

    public List<ChatSimpleDto> findMyChats(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        List<ChatSimpleDto> chats = new ArrayList<>();
        List<ChatSimpleDto> chatsByMe = chatroomCustomRepository.findAllByMe(user.getId());
        List<ChatSimpleDto> chatsByOther = chatroomCustomRepository.findAllByOther(user.getId());

        chats.addAll(chatsByMe);
        chats.addAll(chatsByOther);

        return chats;
    }
}
