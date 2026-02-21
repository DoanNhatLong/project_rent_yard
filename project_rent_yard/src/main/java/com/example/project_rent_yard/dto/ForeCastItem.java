package com.example.project_rent_yard.dto;

import lombok.Data;

import java.util.List;

@Data
public class ForeCastItem {
    List<Weather> weather;
    MainWeather main;
    private String dt_txt;
}
