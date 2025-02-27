package ktb.clothcast.application;

import ktb.clothcast.domain.*;
import ktb.clothcast.dto.RecommendationRequest;
import ktb.clothcast.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;

@Service
public class ClothesService {

    private static final Logger logger = LoggerFactory.getLogger(ClothesService.class);

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
        logger.info("Received request: {}", request);

        if (request.getOwnedClothes() == null) {
            throw new IllegalArgumentException("ownedClothes가 null입니다. 요청을 확인하세요!");
        }

        // 1️⃣ 사용자 저장 또는 조회
        Optional<User> existingUser = userRepository.findById(1L);
        User user = existingUser.orElseGet(() -> {
            User newUser = new User();
            newUser.setUsername("testUser"); // 임시 사용자 (실제 로그인 연동 필요)
            return userRepository.save(newUser);
        });

        // 2️⃣ 보유한 옷 정보 저장
        saveTopwear(request.getOwnedClothes().getTopwear(), user);
        saveBottomwear(request.getOwnedClothes().getBottomwear(), user);
        saveOuterwear(request.getOwnedClothes().getOuterwear(), user);
        saveShoes(request.getOwnedClothes().getShoes(), user);

        // 3️⃣ 날씨 정보 가져오기
        Map<String, Object> weatherData = weatherService.getWeather(
                request.getLocation().getLatitude(),
                request.getLocation().getLongitude()
        );

        logger.info("변환된 날씨 데이터: {}", weatherData);

        // 4️⃣ AI 서버로 보낼 데이터 구성
        Map<String, Object> aiRequestData = Map.of(
                "style", request.getStyle(),
                "location", request.getLocation(),
                "ownedClothes", request.getOwnedClothes(),
                "weather", weatherData
        );

        logger.info("AI 서버로 전송할 데이터: {}", aiRequestData);

        // 5️⃣ AI 서버로 요청하여 추천 결과 받기
        String recommendation = aiService.getClothingRecommendation(aiRequestData);

        logger.info("최종 응답 데이터: {}", recommendation);

        // 최종 응답 반환
        return Map.of(
                "weather", weatherData,
                "recommendation", recommendation
        );
    }

    // DB에서 사용자가 보유한 옷 중 True 값만 필터링하여 반환
    private Map<String, Object> getUserOwnedClothes(User user) {
        Map<String, Boolean> topwear = topwearRepository.findByUser(user)
                .map(t -> Map.of(
                        "knit", Boolean.TRUE.equals(t.getKnit()),
                        "mantoman", Boolean.TRUE.equals(t.getMantoman()),
                        "hoodt", Boolean.TRUE.equals(t.getHoodt()),
                        "shirt", Boolean.TRUE.equals(t.getShirt())
                ))
                .orElse(Map.of());

        Map<String, Boolean> bottomwear = bottomwearRepository.findByUser(user)
                .map(b -> Map.of(
                        "denimPants", Boolean.TRUE.equals(b.getDenimPants()),
                        "cottonPants", Boolean.TRUE.equals(b.getCottonPants()),
                        "shortPants", Boolean.TRUE.equals(b.getShortPants()),
                        "slacks", Boolean.TRUE.equals(b.getSlacks()),
                        "miniSkirt", Boolean.TRUE.equals(b.getMiniSkirt()),
                        "longSkirt", Boolean.TRUE.equals(b.getLongSkirt())
                ))
                .orElse(Map.of());

        Map<String, Boolean> outerwear = outerwearRepository.findByUser(user)
                .map(o -> Map.of(
                        "shortPadding", Boolean.TRUE.equals(o.getShortPadding()),
                        "longPadding", Boolean.TRUE.equals(o.getLongPadding()),
                        "coat", Boolean.TRUE.equals(o.getCoat()),
                        "leatherJacket", Boolean.TRUE.equals(o.getLeatherJacket()),
                        "cardigan", Boolean.TRUE.equals(o.getCardigan()),
                        "hoodZipUp", Boolean.TRUE.equals(o.getHoodZipUp())
                ))
                .orElse(Map.of());

        Map<String, Boolean> shoes = shoesRepository.findByUser(user)
                .map(s -> Map.of(
                        "sneakers", Boolean.TRUE.equals(s.getSneakers()),
                        "boots", Boolean.TRUE.equals(s.getBoots()),
                        "sandals", Boolean.TRUE.equals(s.getSandals()),
                        "sportsShoes", Boolean.TRUE.equals(s.getSportsShoes())
                ))
                .orElse(Map.of());

        // ✅ True 값만 필터링하여 반환
        return Map.of(
                "topwear", topwear.entrySet().stream()
                        .filter(Map.Entry::getValue)
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)),

                "bottomwear", bottomwear.entrySet().stream()
                        .filter(Map.Entry::getValue)
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)),

                "outerwear", outerwear.entrySet().stream()
                        .filter(Map.Entry::getValue)
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)),

                "shoes", shoes.entrySet().stream()
                        .filter(Map.Entry::getValue)
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
        );
    }

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
