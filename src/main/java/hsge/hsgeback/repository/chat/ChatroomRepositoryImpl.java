package hsge.hsgeback.repository.chat;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hsge.hsgeback.dto.chat.ChatSimpleDto;
import hsge.hsgeback.entity.Chatroom;
import hsge.hsgeback.entity.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static hsge.hsgeback.entity.QChatroom.chatroom;
import static hsge.hsgeback.entity.QMessage.message;
import static hsge.hsgeback.entity.QUser.user;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ChatroomRepositoryImpl implements ChatroomRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ChatSimpleDto> findAllByMe(Long userId) {
        List<ChatSimpleDto> dtoList = new ArrayList<>();
        List<Chatroom> chatrooms = queryFactory
                .selectFrom(chatroom)
                .join(chatroom.likeUser, user).fetchJoin()
                .where(chatroom.likeUser.id.eq(userId), chatroom.active.eq(true))
                .fetch();

        for (Chatroom chat : chatrooms) {
            Message latestMessageInfo = getLatestMessageInfo(chat);
            ChatSimpleDto dto = mapToChatSimpleDto(chat, latestMessageInfo);

            dtoList.add(dto);
        }

        return dtoList;
    }

    @Override
    public List<ChatSimpleDto> findAllByOther(Long userId) {
        List<ChatSimpleDto> dtoList = new ArrayList<>();
        List<Chatroom> chatrooms = queryFactory
                .selectFrom(chatroom)
                .join(chatroom.likedUser, user).fetchJoin()
                .where(chatroom.likedUser.id.eq(userId))
                .fetch();

        for (Chatroom chat : chatrooms) {
            Message latestMessageInfo = getLatestMessageInfo(chat);

            ChatSimpleDto dto = mapToChatSimpleDto(chat, latestMessageInfo);

            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    public Chatroom findByUserEmails(String user1Email, String user2Eamil) {
        return queryFactory.selectFrom(chatroom)
                .where(chatroom.likeUser.email.eq(user1Email),
                        chatroom.likedUser.email.eq(user2Eamil))
                .fetchFirst();
    }

    @Override
    @Transactional
    public void updateActive(Long id) {
        queryFactory.update(chatroom)
                .set(chatroom.active, true)
                .where(chatroom.id.eq(id))
                .execute();
    }

    private ChatSimpleDto mapToChatSimpleDto(Chatroom chat, Message message) {
        return ChatSimpleDto.builder()
                .roomId(chat.getId())
                .nickname(chat.getLikeUser().getNickname())
                .profilePath(chat.getLikeUser().getProfilePath())
                .active(chat.getActive())
                .latestMessage(message == null ? "" : message.getContent())
                .checked(message != null && message.isChecked())
                .date(message != null ? message.getCreatedAt().toLocalDate() : chat.getCreatedAt().toLocalDate())
                .build();
    }

    private Message getLatestMessageInfo(Chatroom chat) {
        return queryFactory
                .selectFrom(message)
                .join(message.chatroom, chatroom)
                .where(message.chatroom.id.eq(chat.getId()))
                .orderBy(message.createdAt.desc())
                .fetchFirst();
    }
}
