package ktb.clothcast.application;

import ktb.clothcast.domain.*;
import ktb.clothcast.dto.RecommendationRequest;
import ktb.clothcast.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ClothesService {

    private static final Logger logger = LoggerFactory.getLogger(ClothesService.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OuterwearRepository outerwearRepository;

    @Autowired
    private TopwearRepository topwearRepository;

    @Autowired
    private BottomwearRepository bottomwearRepository;

    @Autowired
    private ShoesRepository shoesRepository;

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private AiService aiService;

    @Transactional
    public Map<String, Object> saveUserDataAndGetRecommendation(RecommendationRequest request) {
        logJson("Received request", request);

        if (request.getOwnedClothes() == null) {
            throw new IllegalArgumentException("ownedClothes가 null입니다. 요청을 확인하세요!");
        }

        // 1. 다음 유저 가져오기 (매 실행마다 ID 증가)
        User user = createNewUser();
        logger.info("선택된 사용자 ID: {}", user.getUserId());

        // 2. 보유한 옷 정보 저장
        saveOwnedClothes(request, user);

        // 3. 날씨 정보 가져오기
        Map<String, Object> weatherData = fetchWeatherData(request);
        logJson("변환된 날씨 데이터", weatherData);

        // 4. AI 서버 요청 데이터 준비
        Map<String, Object> aiRequestData = prepareAiRequestData(request, user, weatherData);
        logJson("AI 서버로 전송할 데이터", aiRequestData);

        // 5. AI 서버 요청 후 추천 결과 받기
        String recommendation = fetchAiRecommendation(aiRequestData);
        logJson("최종 응답 데이터", recommendation);

//        return Map.of("recommendation", recommendation, "weather", weatherData);
        return Map.of("recommendation", recommendation);
    }

    private void logJson(String message, Object data) {
        try {
            String jsonData = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
            logger.info("{}: {}", message, jsonData);
        } catch (JsonProcessingException e) {
            logger.error("JSON 변환 중 오류 발생", e);
        }
    }

    private User createNewUser() {
        User newUser = new User();
        newUser.setUsername("user_" + System.currentTimeMillis()); // 임시 사용자명
        return userRepository.save(newUser);
    }

    // 보유한 옷 정보 저장
    private void saveOwnedClothes(RecommendationRequest request, User user) {
        saveTopwear(request.getOwnedClothes().getTopwear(), user);
        saveBottomwear(request.getOwnedClothes().getBottomwear(), user);
        saveOuterwear(request.getOwnedClothes().getOuterwear(), user);
        saveShoes(request.getOwnedClothes().getShoes(), user);
    }

    // 날씨 정보 가져오기
    private Map<String, Object> fetchWeatherData(RecommendationRequest request) {
        return weatherService.getWeather(
                request.getLocation().getLatitude(),
                request.getLocation().getLongitude()
        );
    }

    // AI 서버 요청 데이터 준비
    private Map<String, Object> prepareAiRequestData(RecommendationRequest request, User user, Map<String, Object> weatherData) {
        Map<String, Object> filteredOwnedClothes = getUserOwnedClothes(user);
        logJson("AI 서버로 보낼 보유 옷 데이터 (필터링 후)", filteredOwnedClothes);
        Map<String, Object> aiRequestData = Map.of(
                "style", request.getStyle(),
                "location", request.getLocation(),
                "ownedClothes", filteredOwnedClothes,
                "weather", weatherData
        );
        logJson("AI 요청 데이터 최종 형태", aiRequestData);
        return aiRequestData;
    }

    // AI 서버 요청 후 추천 결과 받기
    private String fetchAiRecommendation(Map<String, Object> aiRequestData) {
        return aiService.getClothingRecommendation(aiRequestData);
    }

    // DB에서 사용자가 보유한 옷 중 True 값만 필터링하여 반환
    private Map<String, Object> getUserOwnedClothes(User user) {
        return Map.of(
                "topwear", filterTrueValues(topwearRepository.findByUser(user)
                        .map(t -> Map.of(
                                "knit", t.getKnit(),
                                "mantoman", t.getMantoman(),
                                "hoodt", t.getHoodt(),
                                "shirt", t.getShirt()
                        )).orElse(Map.of())),

                "bottomwear", filterTrueValues(bottomwearRepository.findByUser(user)
                        .map(b -> Map.of(
                                "denimPants", b.getDenimPants(),
                                "cottonPants", b.getCottonPants(),
                                "shortPants", b.getShortPants(),
                                "slacks", b.getSlacks(),
                                "miniSkirt", b.getMiniSkirt(),
                                "longSkirt", b.getLongSkirt()
                        )).orElse(Map.of())),

                "outerwear", filterTrueValues(outerwearRepository.findByUser(user)
                        .map(o -> Map.of(
                                "shortPadding", o.getShortPadding(),
                                "longPadding", o.getLongPadding(),
                                "coat", o.getCoat(),
                                "leatherJacket", o.getLeatherJacket(),
                                "cardigan", o.getCardigan(),
                                "hoodZipUp", o.getHoodZipUp()
                        )).orElse(Map.of())),

                "shoes", filterTrueValues(shoesRepository.findByUser(user)
                        .map(s -> Map.of(
                                "sneakers", s.getSneakers(),
                                "boots", s.getBoots(),
                                "sandals", s.getSandals(),
                                "sportsShoes", s.getSportsShoes()
                        )).orElse(Map.of()))
        );
    }

    // True 값만 필터링
    private Map<String, Boolean> filterTrueValues(Map<String, Boolean> clothes) {
        return clothes.entrySet().stream()
                .filter(Map.Entry::getValue)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    // 각 카테고리별 보유한 옷 저장 메서드
    private void saveTopwear(Map<String, Boolean> topwearData, User user) {
        if (topwearData != null) {
            Topwear topwear = new Topwear();
            topwear.setUser(user);
            topwear.setKnit(topwearData.getOrDefault("knit", false));
            topwear.setMantoman(topwearData.getOrDefault("mantoman", false));
            topwear.setHoodt(topwearData.getOrDefault("hoodt", false));
            topwear.setShirt(topwearData.getOrDefault("shirt", false));
            topwearRepository.save(topwear);
        }
    }

    private void saveBottomwear(Map<String, Boolean> bottomwearData, User user) {
        if (bottomwearData != null) {
            Bottomwear bottomwear = new Bottomwear();
            bottomwear.setUser(user);
            bottomwear.setDenimPants(bottomwearData.getOrDefault("denimPants", false));
            bottomwear.setCottonPants(bottomwearData.getOrDefault("cottonPants", false));
            bottomwear.setShortPants(bottomwearData.getOrDefault("shortPants", false));
            bottomwear.setSlacks(bottomwearData.getOrDefault("slacks", false));
            bottomwear.setMiniSkirt(bottomwearData.getOrDefault("miniSkirt", false));
            bottomwear.setLongSkirt(bottomwearData.getOrDefault("longSkirt", false));
            bottomwearRepository.save(bottomwear);
        }
    }

    private void saveOuterwear(Map<String, Boolean> outerwearData, User user) {
        if (outerwearData != null) {
            Outerwear outerwear = new Outerwear();
            outerwear.setUser(user);
            outerwear.setShortPadding(outerwearData.getOrDefault("shortPadding", false));
            outerwear.setLongPadding(outerwearData.getOrDefault("longPadding", false));
            outerwear.setCoat(outerwearData.getOrDefault("coat", false));
            outerwear.setLeatherJacket(outerwearData.getOrDefault("leatherJacket", false));
            outerwear.setCardigan(outerwearData.getOrDefault("cardigan", false));
            outerwear.setHoodZipUp(outerwearData.getOrDefault("hoodZipUp", false));
            outerwearRepository.save(outerwear);
        }
    }

    private void saveShoes(Map<String, Boolean> shoesData, User user) {
        if (shoesData != null) {
            Shoes shoes = new Shoes();
            shoes.setUser(user);
            shoes.setSneakers(shoesData.getOrDefault("sneakers", false));
            shoes.setBoots(shoesData.getOrDefault("boots", false));
            shoes.setSandals(shoesData.getOrDefault("sandals", false));
            shoes.setSportsShoes(shoesData.getOrDefault("sportsShoes", false));
            shoesRepository.save(shoes);
        }
    }
}
