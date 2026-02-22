package com.example.project_rent_yard.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExtraService {

    @Transactional
    public void createSomething(Integer bookingId) {
        ;
    }
}
