package mate.academy.app.service;

import mate.academy.app.dto.order.OrderItemResponseDto;
import mate.academy.app.dto.order.OrderResponseDto;
import mate.academy.app.dto.order.PlaceOrderRequestDto;
import mate.academy.app.mapper.OrderItemMapper;
import mate.academy.app.mapper.OrderMapper;
import mate.academy.app.model.Book;
import mate.academy.app.model.CartItem;
import mate.academy.app.model.Order;
import mate.academy.app.model.OrderItem;
import mate.academy.app.model.ShoppingCart;
import mate.academy.app.model.User;
import mate.academy.app.repository.OrderRepository;
import mate.academy.app.repository.ShoppingCartRepository;
import mate.academy.app.repository.UserRepository;
import mate.academy.app.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderTest {

    @InjectMocks
    private OrderServiceImpl orderService;
    @Mock
    private OrderItemMapper orderItemMapper;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private ShoppingCartService shoppingCartService;
    @Mock
    private OrderMapper orderMapper;

    @DisplayName("""
            ПCreate order: should successfully create order from shopping cart and clear the cart after saving
            """)
    @Test
    public void createNewOrder_ValidInput_ShouldCreateNewOrder() {

        PlaceOrderRequestDto placeOrderRequestDto = new PlaceOrderRequestDto();
        placeOrderRequestDto.setShippingAddress("address");

        User user = new User();
        user.setId(1L);

        Book book = new Book();
        book.setId(1L);
        book.setPrice(BigDecimal.TEN);

        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setQuantity(1);
        cartItem.setBook(book);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCart.setId(user.getId());
        shoppingCart.getCartItems().add(cartItem);

        OrderItem orderItem = new OrderItem();

        when(userRepository.getReferenceById(user.getId())).thenReturn(user);
        when(shoppingCartRepository.findByUserIdWithBooksAndCartItems(user.getId()))
                .thenReturn(Optional.of(shoppingCart));
        when(orderItemMapper.map(any(CartItem.class), any(Order.class))).thenReturn(orderItem);

        orderService.createNewOrder(user.getId(),placeOrderRequestDto);

        verify(orderRepository, times(1)).save(any(Order.class));
        verify(shoppingCartService, times(1)).clearShoppingCart(user.getId());
    }

    @DisplayName("""
            Get all orders: should return a paginated list of OrderResponseDto for a valid user ID
            """)
    @Test
    public void getAllOrdersByUserId_ValidId_ShouldGetAllOrdersByUserId() {
        User user = new User();
        user.setId(1L);

        Pageable pageable = PageRequest.of(0, 10);

        Order order = new Order();
        order.setUser(user);
        order.setId(10L);

        OrderResponseDto responseDto = new OrderResponseDto();
        responseDto.setId(10L);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCart.getCartItems().add(new CartItem());

        Page<Order> orderPage = new PageImpl<>(List.of(order));

        when(orderRepository.findAllByUserIdWithItemsAndBooks(user.getId(), pageable)).thenReturn(orderPage);
        when(orderMapper.toDto(order)).thenReturn(responseDto);

        Page<OrderResponseDto> resultPage = orderService.getAllOrdersByUserId(user.getId(), pageable);

        assertNotNull(resultPage);
        assertThat(resultPage.getContent()).hasSize(1);
        assertThat(resultPage.getContent().get(0)).isEqualTo(responseDto);
        verify(orderRepository, times(1))
                .findAllByUserIdWithItemsAndBooks(user.getId(), pageable);
    }

    @DisplayName("""
            Get order item: should return specific OrderItemDto when valid user, order, and item IDs are provided
            """)
    @Test
    public void getOrderItemById_ValidId_ShouldGetOrderItemById() {
        Long userId = 1L;
        Long orderId = 10L;
        Long itemId = 20L;

        OrderItem orderItem = new OrderItem();
        orderItem.setId(itemId);

        OrderItemResponseDto expectedDto = new OrderItemResponseDto();
        expectedDto.setId(itemId);

        when(orderRepository.findOrderItemByIdAndOrderIdAndOrderUserId(itemId, orderId, userId))
                .thenReturn(Optional.of(orderItem));
        when(orderItemMapper.toDto(orderItem)).thenReturn(expectedDto);

        OrderItemResponseDto actual = orderService.getOrderItemById(
                userId,
                orderId,
                itemId
        );

        assertNotNull(actual);
        assertEquals(actual, expectedDto);

        verify(orderRepository, times(1))
                .findOrderItemByIdAndOrderIdAndOrderUserId(itemId, orderId, userId);
    }
}
