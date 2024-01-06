package com.vvnuts.shop.services;

import com.vvnuts.shop.dtos.requests.OrderRequest;
import com.vvnuts.shop.entities.Item;
import com.vvnuts.shop.entities.Order;
import com.vvnuts.shop.entities.OrderItem;
import com.vvnuts.shop.entities.enums.Status;
import com.vvnuts.shop.repositories.ItemRepository;
import com.vvnuts.shop.repositories.OrderRepository;
import com.vvnuts.shop.utils.mappers.OrderMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderRepository repository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private OrderMapper mapper;
    @Mock
    private BucketService bucketService;
    @InjectMocks
    private OrderService underTest;

    private static final UUID ORDER_ID = UUID.randomUUID();

    @Test
    void orderService_create_returnOrder() {
        //given
        OrderRequest request = createRequest();
        Order order = createOrder();
        when(mapper.transferToCreateEntity(request)).thenReturn(order);
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        //when
        Order result = underTest.create(request);

        //then
        Mockito.verify(repository, times(1)).save(any());
        Assertions.assertThat(result.getTotalPrice()).isEqualTo(BigDecimal.valueOf(8500.0));
        Assertions.assertThat(result.getTotalQuantity()).isEqualTo(15);
    }

    @Test
    void orderService_approveOrder_returnOrderWithStatusSuccess() {
        //given
        Order order = createOrder();
        when(repository.findById(ORDER_ID)).thenReturn(Optional.of(order));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        //when
        Order result = underTest.approveOrder(ORDER_ID);

        //then
        Mockito.verify(itemRepository, times(order.getOrderItems().size())).save(any());
        Mockito.verify(bucketService, times(1)).removeItemFromBucket(any());
        Mockito.verify(repository, times(1)).save(any());
        Assertions.assertThat(order.getOrderItems().get(0).getItem().getQuantity()).isEqualTo(2);
        Assertions.assertThat(order.getOrderItems().get(1).getItem().getQuantity()).isEqualTo(5);
        Assertions.assertThat(result.getStatus()).isEqualTo(Status.SUCCESS);
    }

    @Test
    void orderService_approveOrder_throwException() {
        //given
        when(repository.findById(ORDER_ID)).thenReturn(Optional.empty());

        //when
        //then
        Assertions.assertThatThrownBy(() -> underTest.approveOrder(ORDER_ID))
                .isInstanceOf(NoSuchElementException.class);
        Mockito.verify(itemRepository, never()).save(any());
        Mockito.verify(bucketService, never()).removeItemFromBucket(any());
        Mockito.verify(repository, never()).save(any());
    }

    @Test
    void orderService_cancelingOrder_returnOrderWithStatusCancel() {
        //given
        Order order = createOrder();
        when(repository.findById(ORDER_ID)).thenReturn(Optional.of(order));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        //when
        Order result = underTest.cancelingOrder(ORDER_ID);

        //then
        Mockito.verify(repository, times(1)).findById(ORDER_ID);
        Mockito.verify(repository, times(1)).save(any());
        Assertions.assertThat(result.getStatus()).isEqualTo(Status.CANCEL);
    }

    @Test
    void orderService_delete_canDelete() {
        //given
        Order order = createOrder();
        when(repository.findById(ORDER_ID)).thenReturn(Optional.of(order));

        //when
        underTest.delete(ORDER_ID);

        //then
        Mockito.verify(repository, times(1)).delete(any());
    }

    @Test
    void orderService_findById_returnOrder() {
        //given
        Order order = createOrder();
        when(repository.findById(ORDER_ID)).thenReturn(Optional.of(order));

        //when
        Order result = underTest.findById(ORDER_ID);

        //then
        Mockito.verify(repository, times(1)).findById(ORDER_ID);
        Assertions.assertThat(result.getId()).isNotNull();
    }

    @Test
    void orderService_findById_throwException() {
        //given
        when(repository.findById(ORDER_ID)).thenReturn(Optional.empty());

        //when
        //then
        Assertions.assertThatThrownBy(() -> underTest.approveOrder(ORDER_ID))
                .isInstanceOf(NoSuchElementException.class);
    }

    private OrderRequest createRequest() {
        return OrderRequest.builder()
                .orderItemRequests(new ArrayList<>())
                .email("m@mail.ru")
                .build();
    }

    private Order createOrder() {
        return Order.builder()
                .id(ORDER_ID)
                .email("m@mail.ru")
                .orderItems(createListWithTwoItem())
                .build();
    }

    private List<OrderItem> createListWithTwoItem() {
        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(OrderItem.builder()
                .quantity(10)
                .item(new Item(12, 1000, 0.2))
                .build());
        orderItems.add(OrderItem.builder()
                .quantity(5)
                .item(new Item(10, 100, 0.))
                .build());
        return orderItems;
    }
}