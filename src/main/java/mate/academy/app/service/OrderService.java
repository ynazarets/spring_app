package mate.academy.app.service;

import mate.academy.app.dto.order.OrderItemResponseDto;
import mate.academy.app.dto.order.OrderResponseDto;
import mate.academy.app.dto.order.PlaceOrderRequestDto;
import mate.academy.app.dto.order.UpdateOrderStatusRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    void createNewOrder(Long userId, PlaceOrderRequestDto orderRequestDto);

    Page<OrderResponseDto> getAllOrdersByUserId(Long userId, Pageable pageable);

    void updateOrderStatus(UpdateOrderStatusRequestDto requestDto, Long orderId);

    OrderItemResponseDto getOrderItemById(Long userId, Long orderId,
                                           Long itemId);
}
