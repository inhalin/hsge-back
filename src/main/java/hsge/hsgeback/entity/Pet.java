package hsge.hsgeback.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sex;

    private String description;

    private Boolean neutralization;

    private String picture;

    private String breed;

    @ManyToOne
    private User user;
    
}
