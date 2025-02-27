package ktb.clothcast.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RecommendationRequest {

    private String style;
    private LocationDto location;

    @JsonProperty("owned_clothes") // JSON 키 매핑 (스네이크 케이스 -> 카멜 케이스)
    private OwnedClothesDto ownedClothes;
}
