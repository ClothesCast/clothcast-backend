package ktb.clothcast.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OpenWeatherConfig {

    @Value("${openweather.api.key}")
    private String apiKey;

    public String getWeatherUrl(double latitude, double longitude) {
        return "https://api.openweathermap.org/data/2.5/forecast?lat=" + latitude +
                "&lon=" + longitude + "&appid=" + apiKey + "&units=metric";
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
