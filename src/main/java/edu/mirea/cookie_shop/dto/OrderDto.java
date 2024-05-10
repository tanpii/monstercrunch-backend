package edu.mirea.cookie_shop.dto;

import java.time.OffsetDateTime;
import java.util.List;

public record OrderDto(List<OrderProductDto> orderProduct, long price, long orderId, OffsetDateTime orderTime) {
}
