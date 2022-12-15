package hsge.hsgeback.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@ToString
@Setter
public class UrlDto {

    private String imageUrl;

    private String getImageURL(String s3Url) {
        return s3Url;
    }

    public UrlDto(String s3Url) {
        this.imageUrl = getImageURL(s3Url);
    }
}
