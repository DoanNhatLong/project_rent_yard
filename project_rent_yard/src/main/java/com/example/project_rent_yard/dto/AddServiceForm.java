package com.example.project_rent_yard.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class AddServiceForm {

    private Integer bookingId;

    private List<Integer> specialServiceIds;

    private Map<Integer, Integer> quantities;
}
