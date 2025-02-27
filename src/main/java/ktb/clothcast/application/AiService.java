package ktb.clothcast.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class AiService {

    @Value("${ai.server.url}")  // application.properties에서 AI 서버 URL 설정
    private String aiServerUrl;

    private final RestTemplate restTemplate;

    public AiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getClothingRecommendation(Map<String, Object> requestData) {
        String url = aiServerUrl + "/recommend"; // FastAPI 엔드포인트

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestData, headers);
        ResponseEntity<Map> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Map.class);

        // 응답에서 추천 텍스트 추출
        if (responseEntity.getStatusCode() == HttpStatus.OK && responseEntity.getBody() != null) {
            Object recommendationObj = responseEntity.getBody().get("recommendation");

            if (recommendationObj instanceof String) {
                return (String) recommendationObj;  // 단일 문자열일 경우 그대로 반환
            } else if (recommendationObj instanceof List) {
                List<?> recommendationList = (List<?>) recommendationObj;
                return String.join(", ", recommendationList.stream().map(Object::toString).toArray(String[]::new));  // 리스트를 문자열로 변환
            }
        }
        return "추천 결과를 받아오지 못했습니다.";
    }
}
