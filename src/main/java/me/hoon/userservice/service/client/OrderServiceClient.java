package me.hoon.userservice.service.client;

import me.hoon.userservice.domain.dto.OrderResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "order-service")// 요청할 서비스명 입력
public interface OrderServiceClient {

    @GetMapping("/order-service/orders/{userId}")
    List<OrderResponseDto> getOrderByUserId(@PathVariable("userId") String userId);

}
