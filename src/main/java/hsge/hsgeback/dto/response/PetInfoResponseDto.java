package hsge.hsgeback.dto.response;

import hsge.hsgeback.constant.Age;
import hsge.hsgeback.constant.Breed;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class PetInfoResponseDto {
    private Long id;

    private String petName;

    private String age;

    private String gender;

    private String description;

    private Boolean neutralization;

    private List<String> petImg = new ArrayList<>();

    private String breed;

    private PetResponseDto.Tag tag = new PetResponseDto.Tag();

    @Data
    static class Tag {
        private String[] tagLike;

        private String[] tagDisLike;
    }
    public void setTag(String tagLike, String tagDisLike) {
        String[] strArray = getTags(tagLike);
        tag.setTagLike(strArray);
        strArray = getTags(tagDisLike);
        tag.setTagDisLike(strArray);
    }

    private static String[] getTags(String tags) {
        String[] strArray = tags.split("[#,]");
        List<String> strList = new ArrayList<>(Arrays.asList(strArray));
        strList.removeAll(List.of(""));
        strArray  = strList.toArray(new String[0]);
        return strArray;
    }
}
