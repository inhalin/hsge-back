package hsge.hsgeback.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class PetImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String s3Url;
    private String oriImgName;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PETID")
    @JsonIgnore
    private Pet pet;

    @Builder
    public PetImg(Long id, String s3Url, String oriImgName, Pet pet) {
        this.id = id;
        this.s3Url = s3Url;
        this.oriImgName = oriImgName;
        this.pet = pet;
    }

    public void updatePetImg(String oriImgName, String uuid, Long id){
        this.oriImgName = oriImgName;
        this.s3Url = uuid;
        this.id = id;
    }
}
