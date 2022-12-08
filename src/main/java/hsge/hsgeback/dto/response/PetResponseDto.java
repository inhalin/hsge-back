package hsge.hsgeback.dto.response;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@Slf4j
public class PetResponseDto {

    private String name;

    private String picture;

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
