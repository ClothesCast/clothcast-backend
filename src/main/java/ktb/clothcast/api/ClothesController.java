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
            summary = "사용자의 보유한 옷을 저장하고 날씨 정보를 조회하는 API",
            description = "사용자의 스타일, 위치, 보유한 옷 정보를 받아서 DB에 저장하고, OpenWeather API를 통해 날씨 정보를 가져옵니다."
    )
    @ApiResponse(
            responseCode = "200",
            description = "성공적으로 보유한 옷이 저장되고 날씨 정보가 반환됨",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Map.class))
    )
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식")
    @ApiResponse(responseCode = "500", description = "서버 내부 오류")

    @PostMapping("/recommendation")
    public Map<String, Object> saveClothesAndFetchWeather(
            @RequestBody(description = "사용자의 스타일, 위치 정보, 보유한 옷 목록")
            @org.springframework.web.bind.annotation.RequestBody RecommendationRequest request
    ) {
        return clothesService.saveUserDataAndGetWeather(request);
    }
}
