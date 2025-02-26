package ktb.clothcast.dto;

import lombok.Data;

@Data
public class RecommendationRequest {
    private String style;
    private LocationDto location;
    private OwnedClothesDto ownedClothes;
}
