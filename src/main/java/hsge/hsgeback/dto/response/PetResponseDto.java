package hsge.hsgeback.dto.response;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@Slf4j
@NoArgsConstructor
public class PetResponseDto {

    private Long petId;

    private String name;

    private List<String> petImg = new ArrayList<>();

    private String breed;

    private String sex;

    private Boolean isNeuter;

    private String age;

    private Tag tag = new Tag();
    @Getter
    @Setter
    @NoArgsConstructor
    public static class Tag {
        private String[] tagLike;

        private String[] tagDisLike;

        public Tag(String tagLike, String tagDisLike) {
            this.tagLike = getTags(tagLike);
            this.tagDisLike = getTags(tagDisLike);
        }
    }

    @Builder
    public PetResponseDto(Long petId, String name, List<String> petImg, String breed, String sex, Boolean isNeuter, String age, Tag tag) {
        this.petId = petId;
        this.name = name;
        this.petImg = petImg;
        this.breed = breed;
        this.sex = sex;
        this.isNeuter = isNeuter;
        this.age = age;
        this.tag = tag;
    }



    public void setTag(String tagLike, String tagDisLike) {
        this.tag.setTagLike(getTags(tagLike));
        this.tag.setTagDisLike(getTags(tagDisLike));
    }

    public static String[] getTags(String tags) {
        String[] strArray = tags.split("[#,]");
        List<String> strList = new ArrayList<>(Arrays.asList(strArray));
        strList.removeAll(List.of(""));
        strArray  = strList.toArray(new String[0]);
        return strArray;
    }

    private String getImgaeUrl(String uuid, String imgName, String path){
        return URLEncoder.encode(path + "/" + uuid + "_" + imgName, StandardCharsets.UTF_8);
    }

}
