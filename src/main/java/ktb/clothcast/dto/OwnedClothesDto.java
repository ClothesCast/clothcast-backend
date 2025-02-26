package ktb.clothcast.dto;

import lombok.Data;
import java.util.Map;

@Data
public class OwnedClothesDto {
    private Map<String, Boolean> topwear;
    private Map<String, Boolean> bottomwear;
    private Map<String, Boolean> outerwear;
    private Map<String, Boolean> shoes;
}
