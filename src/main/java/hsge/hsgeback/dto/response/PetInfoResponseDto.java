package hsge.hsgeback.dto.response;

import hsge.hsgeback.constant.Age;
import hsge.hsgeback.constant.Breed;
import lombok.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PetInfoResponseDto {
    private Long id;

    private String petName;

    private String age;

    private String gender;

    private String description;

    private Boolean neutralization;

    private List<String> petImg = new ArrayList<>();

    private String breed;

    private BreedDto breedDto;

    private AgeDto ageDto;

    private Tag tag = new Tag();

    @NoArgsConstructor
    @Getter
    @Setter
    public static class Tag {
        private String[] tagLike;

        private String[] tagDisLike;

        public Tag(String tagLike, String tagDisLike) {
            this.tagLike = getTags(tagLike);
            this.tagDisLike = getTags(tagDisLike);
        }
    }

    @Builder
    public PetInfoResponseDto(Long id, String petName, String age, String gender, String description, Boolean neutralization, List<String> petImg, String breed, BreedDto breedDto, AgeDto ageDto, Tag tag) {
        this.id = id;
        this.petName = petName;
        this.age = age;
        this.gender = gender;

        this.description = description;
        this.neutralization = neutralization;
        this.petImg = petImg;
        this.breed = breed;
        this.breedDto = breedDto;
        this.ageDto = ageDto;
        this.tag = tag;
    }

    private static String[] getTags(String tags) {
        String[] strArray = tags.split("[#,]");
        List<String> strList = new ArrayList<>(Arrays.asList(strArray));
        strList.removeAll(List.of(""));
        strArray  = strList.toArray(new String[0]);
        return strArray;
    }
}
