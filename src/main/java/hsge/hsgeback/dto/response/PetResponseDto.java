package hsge.hsgeback.dto.response;

import hsge.hsgeback.constant.Age;
import hsge.hsgeback.constant.Breed;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Data
@Slf4j
public class PetResponseDto {

    private String name;

    private String imgUrl;

    private String breed;

    private String sex;

    private Boolean isNeuter;

    private String age;

    private Tag tag = new Tag();

    @Data
    static class Tag {
        private String[] tagLike;

        private String[] tagDisLike;
    }

    public void setTag(String tagLike, String tagDisLike) {
        tag.setTagLike(tagLike.split("#"));
        tag.setTagDisLike(tagDisLike.split("#"));
    }
}
