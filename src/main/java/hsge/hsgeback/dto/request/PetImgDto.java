package hsge.hsgeback.dto.request;

import hsge.hsgeback.entity.Pet;
import hsge.hsgeback.entity.PetImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.UUID;

@Getter
@Setter
public class PetImgDto {

    private Long id;

    private String s3Url;

    private String oriImgName;

    private Pet pet;

    public PetImg toPetImgEntity(Pet pet){
        return PetImg.builder()
                .s3Url(getS3Url())
                .oriImgName(getOriImgName())
                .pet(pet)
                .build();
    }
}
