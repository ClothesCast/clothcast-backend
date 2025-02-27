package ktb.clothcast.application;

import ktb.clothcast.config.OpenWeatherConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class WeatherService {

    @Autowired
    private OpenWeatherConfig openWeatherConfig;

    @Autowired
    private RestTemplate restTemplate;

    public Map<String, Object> getWeather(double latitude, double longitude) {
        String url = openWeatherConfig.getWeatherUrl(latitude, longitude);
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        // API 응답에서 'list[0].weather[0].main' 데이터를 추출하여 'condition' 추가
        if (response != null && response.containsKey("list")) {
            List<Map<String, Object>> forecastList = (List<Map<String, Object>>) response.get("list");

            if (!forecastList.isEmpty() && forecastList.get(0).containsKey("weather")) {
                List<Map<String, Object>> weatherList = (List<Map<String, Object>>) forecastList.get(0).get("weather");

                if (!weatherList.isEmpty() && weatherList.get(0).containsKey("main")) {
                    String condition = (String) weatherList.get(0).get("main");
                    response.put("condition", condition);  // AI 서버가 요구하는 필드 추가
                }
            }
        }

        return response;
    }

}
