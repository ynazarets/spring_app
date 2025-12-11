package mate.academy.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.app.dto.order.OrderItemResponseDto;
import mate.academy.app.dto.order.OrderResponseDto;
import mate.academy.app.dto.order.PlaceOrderRequestDto;
import mate.academy.app.dto.order.UpdateOrderStatusRequestDto;
import mate.academy.app.model.User;
import mate.academy.app.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Create new Order",
            description = "Create new order with shipping address")
    void createOrder(@AuthenticationPrincipal UserDetails userDetails,
                     @RequestBody @Valid PlaceOrderRequestDto requestDto) {
        Long userId = ((User) userDetails).getId();
        orderService.createNewOrder(userId, requestDto);
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get order history",
            description = "Retrieve user's order history by User id")
    Page<OrderResponseDto> getAllOrder(@AuthenticationPrincipal UserDetails userDetails,
                                       Pageable pageable) {
        Long userId = ((User) userDetails).getId();
        return orderService.getAllOrdersByUserId(userId, pageable);
    }

    @PatchMapping("/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update order status.",
            description = "Update status from admin by id")
    void updateOrderStatus(@RequestBody @Valid UpdateOrderStatusRequestDto requestDto,
                           @PathVariable Long orderId) {
        orderService.updateOrderStatus(requestDto, orderId);
    }

    @GetMapping("/{orderId}/items/{itemId}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Retrieve a specific OrderItem",
            description = "Retrieve a specific OrderItem within an order by its ID")
    OrderItemResponseDto getOrderItemById(@AuthenticationPrincipal UserDetails userDetails,
                                          @PathVariable Long orderId,
                                          @PathVariable Long itemId) {
        Long userId = ((User) userDetails).getId();
        return orderService.getOrderItemById(userId, orderId, itemId);
    }
}
