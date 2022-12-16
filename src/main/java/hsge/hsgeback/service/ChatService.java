package hsge.hsgeback.service;

import hsge.hsgeback.constant.PushNotification;
import hsge.hsgeback.dto.chat.ChatSimpleDto;
import hsge.hsgeback.dto.match.UserPetMatchDto;
import hsge.hsgeback.entity.Chatroom;
import hsge.hsgeback.entity.Message;
import hsge.hsgeback.entity.User;
import hsge.hsgeback.repository.ChatroomCustomRepository;
import hsge.hsgeback.repository.ChatroomRepository;
import hsge.hsgeback.repository.MessageRepository;
import hsge.hsgeback.repository.UserCustomRepository;
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
    private final ChatroomCustomRepository chatroomCustomRepository;
    private final UserCustomRepository userCustomRepository;
    private final MessageRepository messageRepository;

    @Transactional
    public void saveChatroom(UserPetMatchDto matchDto) {

        User liker = userCustomRepository.findByEmail(matchDto.getLikerEmail());
        User petOwner = userCustomRepository.findByEmail(matchDto.getPetOwnerEmail());

        Chatroom chat = Chatroom.builder()
                .likeUser(liker)
                .likedUser(petOwner)
                .active(false)
                .build();
        chatroomRepository.save(chat);

        String content = PushNotification.LIKE_TITLE.getContent(matchDto.getPetName());

        Message message = Message.builder()
                .chatroom(chat)
                .content(content)
                .user(liker)
                .checked(false)
                .build();
        messageRepository.save(message);
    }

    public List<ChatSimpleDto> findMyChats(String email) {
        User user = userCustomRepository.findByEmail(email);

        List<ChatSimpleDto> chats = new ArrayList<>();
        List<ChatSimpleDto> chatsByMe = chatroomCustomRepository.findAllByMe(user.getId());
        List<ChatSimpleDto> chatsByOther = chatroomCustomRepository.findAllByOther(user.getId());

        chats.addAll(chatsByMe);
        chats.addAll(chatsByOther);

        return chats;
    }
}
