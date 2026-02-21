package com.example.project_rent_yard.service;

import com.example.project_rent_yard.dto.ForeCastItem;
import com.example.project_rent_yard.dto.ForeCastResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class WeatherService {

    @Value("${openweather.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public WeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ForeCastResponse getForecast() {
        System.out.println("API KEY = " + apiKey);

        String url = "https://api.openweathermap.org/data/2.5/forecast"
                + "?q=Da Nang"
                + "&units=metric"
                + "&appid=" + apiKey;
        System.out.println(url);

        return restTemplate.getForObject(url, ForeCastResponse.class);
    }

    public List<ForeCastItem> getDailyForecast() {

        ForeCastResponse response = getForecast();
        if (response == null || response.getList() == null) {
            System.out.println("Forecast NULL");
            return List.of();
        }

        return getForecast().getList().stream()
//                .filter(item -> item.getDt_txt().contains("12:00:00"))
                .limit(5)
                .toList();
    }
}
