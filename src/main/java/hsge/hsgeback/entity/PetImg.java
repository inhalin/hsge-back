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
    private String uuid;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PETID")
    @JsonIgnore
    private Pet pet;

    @Builder
    public PetImg(Long id, String s3Url, String oriImgName, Pet pet, String uuid) {
        this.id = id;
        this.s3Url = s3Url;
        this.oriImgName = oriImgName;
        this.pet = pet;
        this.uuid = uuid;
    }
}
