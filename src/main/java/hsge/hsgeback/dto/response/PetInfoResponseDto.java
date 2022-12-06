package hsge.hsgeback.dto.response;

import hsge.hsgeback.constant.Age;
import hsge.hsgeback.constant.Breed;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PetInfoResponseDto {
    private Long id;

    private String petName;

    private String age;

    private String gender;

    private String description;

    private Boolean neutralization;

    private String picture;

    private String breed;

    private PetResponseDto.Tag tag = new PetResponseDto.Tag();

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
