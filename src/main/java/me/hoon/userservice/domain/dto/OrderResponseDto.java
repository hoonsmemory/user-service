package me.hoon.userservice.domain.dto;

import lombok.Data;

import java.util.Date;

@Data
public class OrderResponseDto {
    private String productId;
    private Integer qty;
    private Integer unitPrice;
    private Integer totalPrice;
    private Date createdAt;

    private String orderId;
}
