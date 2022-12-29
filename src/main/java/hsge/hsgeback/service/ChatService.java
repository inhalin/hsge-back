package hsge.hsgeback.service;

import hsge.hsgeback.constant.PushNotification;
import hsge.hsgeback.dto.chat.ChatLeaveRequest;
import hsge.hsgeback.dto.chat.ChatSimpleDto;
import hsge.hsgeback.dto.common.BasicResponse;
import hsge.hsgeback.dto.match.UserPetMatchDto;
import hsge.hsgeback.dto.response.MessageDto;
import hsge.hsgeback.dto.response.MessageResponseDto;
import hsge.hsgeback.dto.response.MessageUserInfoDto;
import hsge.hsgeback.entity.*;
import hsge.hsgeback.exception.NotOwnerException;
import hsge.hsgeback.exception.ResourceNotFoundException;
import hsge.hsgeback.repository.pet.PetRepository;
import hsge.hsgeback.repository.chat.ChatroomRepositoryImpl;
import hsge.hsgeback.repository.chat.ChatroomRepository;
import hsge.hsgeback.repository.chat.MessageRepository;
import hsge.hsgeback.repository.match.MatchRepository;
import hsge.hsgeback.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatroomRepository chatroomRepository;
    private final ChatroomRepositoryImpl chatroomCustomRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final MatchRepository matchRepository;
    private final PetRepository petRepository;

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
            chatroomRepository.updateActive(reverse.getId());

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

    public MessageDto getChatMessages(String email, Long roomId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        Chatroom chatroom = chatroomRepository.findById(roomId)
                .orElseThrow();

        User likeUser = chatroom.getLikeUser();
        User likedUser = chatroom.getLikedUser();

        Long currentUserId = user.getId();
        Long likeUserId = likeUser.getId();
        Long likedUserId = likedUser.getId();

        if (!Objects.equals(currentUserId, likeUserId) && !Objects.equals(currentUserId, likedUserId)) {
            throw new NotOwnerException();
        }

        User otherUser = Objects.equals(currentUserId, likeUserId) ? likedUser : likeUser;
        MessageUserInfoDto userInfo = new MessageUserInfoDto(currentUserId, otherUser.getId(), otherUser.getNickname(), otherUser.getProfilePath());

        List<Message> messageList = chatroom.getMessageList();
        List<MessageResponseDto> result = messageList.stream()
                .map(m -> {
                    MessageResponseDto dto = new MessageResponseDto();
                    dto.setSenderId(m.getUser().getId());
                    dto.setMessage(m.getContent());
                    dto.setCreatedDate(m.getCreatedAt());
                    return dto;
                }).collect(Collectors.toList());

        return new MessageDto(userInfo, result);
    }

    @Transactional
    public void activeRoom(String email, Long roomId) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        Chatroom chatroom = chatroomRepository.findById(roomId)
                .orElseThrow();

        User likeUser = chatroom.getLikeUser();
        User likedUser = chatroom.getLikedUser();

        Long currentUserId = user.getId();
        Long likeUserId = likeUser.getId();
        Long likedUserId = likedUser.getId();

        if (!Objects.equals(currentUserId, likeUserId) && !Objects.equals(currentUserId, likedUserId)) {
            throw new NotOwnerException();
        }

        chatroom.activeChatroom();
    }

    @Transactional
    public BasicResponse leaveChatroom(String email, Long roomId, ChatLeaveRequest request) {

        Chatroom chatroom = chatroomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Chatroom", "id", roomId.toString()));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        List<Pet> myPets = petRepository.findAllByUserId(user.getId());

        if (chatroom.getLikeUser().getId().equals(user.getId()) && !chatroom.getActive()) {
            throw new IllegalArgumentException("내가 좋아요 한 강아지의 채팅방이 시작되지 않은 경우 채팅방을 먼저 나갈 수 없습니다.");
        }

        User counter = userRepository.findById(request.getCounterUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getCounterUserId().toString()));
        List<Pet> counterPets = petRepository.findAllByUserId(counter.getId());

        if (request.getType().equals(ChatLeaveType.DEFAULT)) {
            chatroomRepository.leaveChatroom(roomId);
            counterPets.forEach(pet -> matchRepository.deleteAllByPetIdAndUserId(pet.getId(), user.getId()));
            myPets.forEach(pet -> matchRepository.deleteAllByPetIdAndUserId(pet.getId(), counter.getId()));

            return BasicResponse.of(roomId, "정상적으로 채팅방을 나갔습니다.");
        } else if (request.getType().equals(ChatLeaveType.REPORT)
                || request.getType().equals(ChatLeaveType.UNMATCH)) {
            chatroomRepository.leaveChatroom(roomId);
            counterPets.forEach(pet -> {
                if (matchRepository.existsByPetIdAndUserId(pet.getId(), user.getId())) {
                    matchRepository.updateLikeValue(pet.getId(), user.getId(), false);
                } else {
                    matchRepository.save(Match.builder().likeValue(false).pet(pet).user(user).build());
                }
            });
            myPets.forEach(pet -> {
                if (matchRepository.existsByPetIdAndUserId(pet.getId(), counter.getId())) {
                    matchRepository.updateLikeValue(pet.getId(), counter.getId(), false);
                } else {
                    matchRepository.save(Match.builder().likeValue(false).pet(pet).user(counter).build());
                }
            });

            return BasicResponse.of(roomId, "정상적으로 채팅방을 나가고 상대를 차단했습니다.");
        } else {
            throw new IllegalArgumentException("요청 타입이 잘못되었습니다.");
        }
    }
}
