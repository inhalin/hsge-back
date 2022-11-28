package hsge.hsgeback.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@NoArgsConstructor
@Getter
@Entity
public class Participant extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //이게 맞는지 모르겠는데 참여일 나간 시간 밖에 없어서 일단 이렇게 해놨는데 좀 더 봐야 될 것 같읍니다.
}
