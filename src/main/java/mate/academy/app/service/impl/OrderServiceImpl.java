    package mate.academy.app.service.impl;

    import jakarta.transaction.Transactional;
    import lombok.RequiredArgsConstructor;
    import mate.academy.app.dto.order.OrderItemResponseDto;
    import mate.academy.app.dto.order.OrderResponseDto;
    import mate.academy.app.dto.order.PlaceOrderRequestDto;
    import mate.academy.app.dto.order.UpdateOrderStatusRequestDto;
    import mate.academy.app.exception.EmptyShoppingCartException;
    import mate.academy.app.exception.EntityNotFoundException;
    import mate.academy.app.mapper.OrderItemMapper;
    import mate.academy.app.mapper.OrderMapper;
    import mate.academy.app.model.Order;
    import mate.academy.app.model.OrderItem;
    import mate.academy.app.model.ShoppingCart;
    import mate.academy.app.model.User;
    import mate.academy.app.model.enums.OrderStatus;
    import mate.academy.app.repository.OrderRepository;
    import mate.academy.app.repository.ShoppingCartRepository;
    import mate.academy.app.repository.UserRepository;
    import mate.academy.app.service.OrderService;
    import mate.academy.app.service.ShoppingCartService;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Pageable;
    import org.springframework.stereotype.Service;

    import java.math.BigDecimal;
    import java.time.LocalDateTime;
    import java.util.List;
    import java.util.Set;
    import java.util.stream.Collectors;

    @Service
    @RequiredArgsConstructor
    public class OrderServiceImpl implements OrderService {
        private final OrderItemMapper orderItemMapper;
        private final OrderRepository orderRepository;
        private final UserRepository userRepository;
        private final ShoppingCartRepository shoppingCartRepository;
        private final ShoppingCartService shoppingCartService;
        private final OrderMapper orderMapper;

        @Override
        @Transactional
        public void createNewOrder(Long userId, PlaceOrderRequestDto orderRequestDto) {
            User user = userRepository.getReferenceById(userId);
            ShoppingCart shoppingCart = shoppingCartRepository.findByUserIdWithBooksAndCartItems(userId).orElseThrow(
                    () -> new EntityNotFoundException("Shopping cart not found for user with id: " + userId)
            );
            if(shoppingCart.getCartItems().isEmpty()){
              throw new EmptyShoppingCartException("Shopping cart is empty. Cannot create order");
            }
            Order order = createOrder(user, orderRequestDto, shoppingCart);
            orderRepository.save(order);
            shoppingCartService.clearShoppingCart(userId);
        }

        @Override
        public Page<OrderResponseDto> getAllOrdersByUserId(Long userId, Pageable pageable) {
            Page<Order> ordersPage = orderRepository.findAllByUserIdWithItemsAndBooks(userId, pageable);
            return ordersPage.map(orderMapper::toDto);
        }

        @Override
        @Transactional
        public void updateOrderStatus(UpdateOrderStatusRequestDto requestDto, Long orderId) {
            Order order = orderRepository.findById(orderId).orElseThrow(
                    () -> new EntityNotFoundException("Order with id: " + orderId + " not found")
            );
            order.setOrderStatus(requestDto.getStatus());

        }

        @Override
        public Page<OrderItemResponseDto> getAllItems(Long userId, Long orderId, Pageable pageable) {
            Page<OrderItem> orderItemPage = orderRepository
                    .findAllByOrderIdAndUserId(orderId, userId, pageable);
            return orderItemPage.map(orderItemMapper::toDto);
        }

        private Order createOrder(User user,
                                  PlaceOrderRequestDto orderRequestDto,
                                  ShoppingCart shoppingCart) {
            Order order = new Order();
            BigDecimal totalAmount = shoppingCart.getCartItems().stream()
                    .map(cartItem -> cartItem.getBook().getPrice()
                            .multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            order.setUser(user);
            order.setShippingAddress(orderRequestDto.getShippingAddress());
            order.setOrderDate(LocalDateTime.now());
            order.setOrderStatus(OrderStatus.PENDING);
            order.setTotal(totalAmount);
            Set<OrderItem> orderItems = shoppingCart.getCartItems().stream()
                    .map(cartItem -> orderItemMapper.map(cartItem, order))
                    .collect(Collectors.toSet());
            order.setOrderItems(orderItems);
            return order;
        }
    }
