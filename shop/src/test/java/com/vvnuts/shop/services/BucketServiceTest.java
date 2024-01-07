package com.vvnuts.shop.services;

import com.vvnuts.shop.dtos.requests.BucketRequest;
import com.vvnuts.shop.dtos.requests.OrderItemRequest;
import com.vvnuts.shop.dtos.responses.BucketResponse;
import com.vvnuts.shop.entities.*;
import com.vvnuts.shop.repositories.BucketRepository;
import com.vvnuts.shop.utils.mappers.BucketItemMapper;
import com.vvnuts.shop.utils.mappers.BucketMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BucketServiceTest {
    @Mock
    private BucketRepository repository;
    @Mock
    private BucketMapper mapper;
    @Mock
    private BucketItemMapper bucketItemMapper;
    @Mock
    private BucketItemService bucketItemService;
    @Mock
    private UserService userService;
    @InjectMocks
    private BucketService underTest;

    private static final int USER_ID = 1;
    private static final int[] BUCKET_ITEMS_ID = {2, 4, 6};

    @Test
    void bucketService_create_returnNewBucket() {
        //given
        BucketRequest request = createRequest();
        List<BucketItem> bucketItems = createListOfBucketItem(BUCKET_ITEMS_ID);
        when(userService.findById(any())).thenReturn(new User());
        when(bucketItemMapper.transferBucketItemDtoToList(request.getOrderItem())).thenReturn(bucketItems);
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        //when
        Bucket result = underTest.create(request);

        //then
        Mockito.verify(repository, times(1)).save(any());
        Mockito.verify(bucketItemService, times(1)).linkBucket(any());
        Assertions.assertThat(result.getTotalQuantity()).isEqualTo(36);
        Assertions.assertThat(result.getTotalPrice()).isEqualTo(BigDecimal.valueOf(12480.0));
    }

    @Test
    void bucketService_findOne_returnResponse() {
        //given
        Bucket bucket = createBucket();
        when(userService.findById(USER_ID)).thenReturn(createUser());
        when(repository.findByUser(any())).thenReturn(Optional.of(bucket));
        when(mapper.convertEntityToResponse(bucket)).thenReturn(new BucketResponse());

        //when
        BucketResponse result = underTest.findOne(USER_ID);

        //then
        Assertions.assertThat(result).isNotNull();
    }

    @Test
    void bucketService_findById_returnBucket() {
        //given
        when(userService.findById(USER_ID)).thenReturn(createUser());
        when(repository.findByUser(any())).thenReturn(Optional.of(createBucket()));

        //when
        Bucket result = underTest.findBucketByUserId(USER_ID);

        //then
        Mockito.verify(repository, times(1)).findByUser(any());
        Assertions.assertThat(result).isNotNull();
    }

    @Test
    void bucketService_findById_throwException() {
        //given
        when(userService.findById(USER_ID)).thenReturn(createUser());
        when(repository.findByUser(any())).thenReturn(Optional.empty());

        //when
        //then
        Assertions.assertThatThrownBy(() -> underTest.findBucketByUserId(USER_ID))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void bucketService_update_returnUpdateBucket() {
        //given
        BucketRequest request = createRequest();
        Bucket updateBucket = createBucket();
        List<BucketItem> newBucketItems = createListOfBucketItem(new int[] {1, 3, 5});
        when(bucketItemMapper.transferBucketItemDtoToList(anyList())).thenReturn(newBucketItems);
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        //when
        Bucket result = underTest.update(request, updateBucket);

        //then
        Mockito.verify(bucketItemService, times(3)).removeBucket(any());
        Mockito.verify(bucketItemService, times(1)).linkBucket(any());
        Assertions.assertThat(result.getTotalQuantity()).isEqualTo(27);
        Assertions.assertThat(result.getTotalPrice()).isEqualTo(BigDecimal.valueOf(8205.0));
    }

    @Test
    void bucketService_removeItemFromBucket_removeBucketItems() {
        //given
        Bucket bucket = createBucket();
        User user = createUser();
        when(repository.findByUser(user)).thenReturn(Optional.of(bucket));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        //when
        Bucket result = underTest.removeItemFromBucket(user);

        //then
        Mockito.verify(bucketItemService, times(bucket.getBucketItems().size())).removeBucket(any());
        Assertions.assertThat(result.getTotalPrice()).isEqualTo(BigDecimal.ZERO);
        Assertions.assertThat(result.getTotalQuantity()).isEqualTo(0);
    }

    @Test
    void bucketService_removeItemFromBucket_returnBucketWithZeroPriceAndQuantity() {
        //given
        Bucket bucket = createBucket();
        bucket.setBucketItems(null);
        User user = createUser();
        when(repository.findByUser(user)).thenReturn(Optional.of(bucket));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        //when
        Bucket result = underTest.removeItemFromBucket(user);

        //then
        Mockito.verify(bucketItemService, never()).removeBucket(any());
        Assertions.assertThat(result.getTotalPrice()).isEqualTo(BigDecimal.ZERO);
        Assertions.assertThat(result.getTotalQuantity()).isEqualTo(0);
    }

    @Test
    void bucketService_removeItemFromBucket_returnException() {
        //given
        User user = createUser();
        when(repository.findByUser(user)).thenReturn(Optional.empty());

        //when
        //then
        Assertions.assertThatThrownBy(() -> underTest.removeItemFromBucket(user))
                .isInstanceOf(NoSuchElementException.class);
    }

    private BucketRequest createRequest()  {
        return BucketRequest.builder()
                .user(3)
                .orderItem(createListOfOrderItemRequest(BUCKET_ITEMS_ID))
                .build();
    }

    private Bucket createBucket() {
        return Bucket.builder()
                .id(USER_ID)
                .bucketItems(createListOfBucketItem(BUCKET_ITEMS_ID))
                .totalPrice(BigDecimal.ONE)
                .totalQuantity(3)
                .build();
    }

    private User createUser() {
        return User.builder()
                .userId(USER_ID)
                .firstname("Ivan")
                .build();
    }

    private List<BucketItem> createListOfBucketItem(int[] ids) {
        List<BucketItem> bucketItems = new ArrayList<>();
        IntStream.of(ids).forEach(id -> bucketItems.add(BucketItem.builder()
                .quantity(id * 3)
                .item(Item.builder().price(100 * id).sale((5 * id)/100.).build())
                .build()));
        return bucketItems;
    }

    private List<OrderItemRequest> createListOfOrderItemRequest(int[] ids) {
        List<OrderItemRequest> orderItemRequests = new ArrayList<>();
        IntStream.of(ids).forEach(id -> orderItemRequests.add(OrderItemRequest.builder()
                .quantity(id * 3)
                .item(id)
                .build()));
        return orderItemRequests;
    }
}