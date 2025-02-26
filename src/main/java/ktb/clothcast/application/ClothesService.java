package ktb.clothcast.application;

import ktb.clothcast.domain.*;
import ktb.clothcast.dto.RecommendationRequest;
import ktb.clothcast.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
public class ClothesService {

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

    @Transactional
    public Map<String, Object> saveUserDataAndGetWeather(RecommendationRequest request) {
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
        return weatherService.getWeather(request.getLocation().getLatitude(), request.getLocation().getLongitude());
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
