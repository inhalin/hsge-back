package hsge.hsgeback.dto.response;

import hsge.hsgeback.entity.User;
import lombok.Data;

import java.util.List;

@Data
public class LocationUserResponse {
    private List<User> data;
}