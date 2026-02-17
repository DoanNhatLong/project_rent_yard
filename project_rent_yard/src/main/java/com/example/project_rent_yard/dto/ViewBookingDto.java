package com.example.project_rent_yard.dto;

import com.example.project_rent_yard.view.DisplayItem;
import com.example.project_rent_yard.view.ViewRenderAble;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViewBookingDto implements ViewRenderAble {
    private Integer id;

    private String userName;
    private String userPhone;

    private Integer fieldId;

    private LocalTime startTime;
    private LocalTime endTime;

    private String status;

    private double depositAmount;

    private LocalDate bookingDate;

    @Override
    public List<DisplayItem> render() {
        return List.of(
                new DisplayItem("Mã đặt sân", id),
                new DisplayItem("Tên người đặt", userName),
                new DisplayItem("Điện thoại", userPhone),
                new DisplayItem("Field ID", fieldId),
                new DisplayItem("Start Time", startTime),
                new DisplayItem("End Time", endTime),
                new DisplayItem("Status", status),
                new DisplayItem("Deposit Amount", depositAmount),
                new DisplayItem("Booking Date", bookingDate)
        );

    }
}
