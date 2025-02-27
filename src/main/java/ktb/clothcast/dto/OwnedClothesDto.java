package ktb.clothcast.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class OwnedClothesDto {

    @JsonProperty("topwear")
    private Map<String, Boolean> topwear = new HashMap<>();

    @JsonProperty("bottomwear")
    private Map<String, Boolean> bottomwear = new HashMap<>();

    @JsonProperty("outerwear")
    private Map<String, Boolean> outerwear = new HashMap<>();

    @JsonProperty("shoes")
    private Map<String, Boolean> shoes = new HashMap<>();
}
