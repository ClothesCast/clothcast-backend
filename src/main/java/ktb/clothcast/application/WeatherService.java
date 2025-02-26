package ktb.clothcast.application;

import ktb.clothcast.config.OpenWeatherConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class WeatherService {

    @Autowired
    private OpenWeatherConfig openWeatherConfig;

    @Autowired
    private RestTemplate restTemplate;

    public Map<String, Object> getWeather(double latitude, double longitude) {
        String url = openWeatherConfig.getWeatherUrl(latitude, longitude);
        return restTemplate.getForObject(url, Map.class);
    }
}
