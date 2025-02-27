package ktb.clothcast.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import ktb.clothcast.dto.RecommendationRequest;
import ktb.clothcast.application.ClothesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user/preferences")
public class ClothesController {

    @Autowired
    private ClothesService clothesService;

    @Operation(
            summary = "사용자의 보유한 옷을 저장하고 AI 추천 결과를 받아오는 API",
            description = "사용자의 스타일, 위치, 보유한 옷 정보를 저장하고, 날씨 정보를 가져온 후 AI 서버에 추천을 요청합니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "성공적으로 보유한 옷이 저장되고 AI 추천 결과가 반환됨",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Map.class))
    )
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식")
    @ApiResponse(responseCode = "500", description = "서버 내부 오류")

    @PostMapping("/recommendation")
    public Map<String, Object> saveClothesAndFetchRecommendation(
            @RequestBody(description = "사용자의 스타일, 위치 정보, 보유한 옷 목록")
            @org.springframework.web.bind.annotation.RequestBody RecommendationRequest request
    ) {
        return clothesService.saveUserDataAndGetRecommendation(request);
    }
}
