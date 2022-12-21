package hsge.hsgeback.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hsge.hsgeback.dto.chat.ChatSimpleDto;
import hsge.hsgeback.entity.Chatroom;
import hsge.hsgeback.entity.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static hsge.hsgeback.entity.QChatroom.chatroom;
import static hsge.hsgeback.entity.QMessage.message;

@Repository
@RequiredArgsConstructor
public class ChatroomCustomRepository {

    private final JPAQueryFactory queryFactory;

    public List<ChatSimpleDto> findAllByMe(Long userId) {
        List<ChatSimpleDto> dtoList = new ArrayList<>();
        List<Chatroom> chatrooms = queryFactory
                .selectFrom(chatroom)
                .where(chatroom.likeUser.id.eq(userId), chatroom.active.eq(true))
                .fetch();

        for (Chatroom chat : chatrooms) {
            Message latestMessageInfo = getLatestMessageInfo(chat);
            ChatSimpleDto dto = mapToChatSimpleDto(chat, latestMessageInfo);

            dtoList.add(dto);
        }

        return dtoList;
    }

    public List<ChatSimpleDto> findAllByOther(Long userId) {
        List<ChatSimpleDto> dtoList = new ArrayList<>();
        List<Chatroom> chatrooms = queryFactory.selectFrom(chatroom).where(chatroom.likedUser.id.eq(userId)).fetch();

        for (Chatroom chat : chatrooms) {
            Message latestMessageInfo = getLatestMessageInfo(chat);

            ChatSimpleDto dto = mapToChatSimpleDto(chat, latestMessageInfo);

            dtoList.add(dto);
        }
        return dtoList;
    }

    private ChatSimpleDto mapToChatSimpleDto(Chatroom chat, Message message) {
        return ChatSimpleDto.builder()
                .nickname(chat.getLikeUser().getNickname())
                .profilePath(chat.getLikeUser().getProfilePath())
                .active(chat.getActive())
                .latestMessage(message.getContent())
                .checked(message.getChecked())
                .build();
    }

    private Message getLatestMessageInfo(Chatroom chat) {
        return (Message) queryFactory
                .select(message.content, message.checked)
                .from(message)
                .where(message.chatroom.id.eq(chat.getId()))
                .orderBy(message.createdAt.desc())
                .fetchFirst();
    }
}
