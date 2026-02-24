package com.example.project_rent_yard.repository.projection;

public interface ServiceBookingView {
    Integer getId();
    Integer getQuantity();
    String getFieldName();
    String getServiceName();
    Double getFieldPrice();
    Double getServicePrice();
}
