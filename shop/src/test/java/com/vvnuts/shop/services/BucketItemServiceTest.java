package com.vvnuts.shop.services;

import com.vvnuts.shop.entities.Bucket;
import com.vvnuts.shop.entities.BucketItem;
import com.vvnuts.shop.entities.Item;
import com.vvnuts.shop.repositories.BucketItemRepository;
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
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class BucketItemServiceTest {
    @Mock
    private BucketItemRepository repository;
    @InjectMocks
    private BucketItemService underTest;

    private static final int[] BUCKET_ITEMS_ID = {2, 4, 6};

    @Test
    void bucketItemService_linkBucket_linkBucketToBucketItem() {
        //given
        Bucket bucket = createBucket();

        //when
        underTest.linkBucket(bucket);

        //then
        Mockito.verify(repository, Mockito.times(bucket.getBucketItems().size())).save(any());
        Assertions.assertThat(bucket.getBucketItems().get(0).getBucket()).isEqualTo(bucket);
        Assertions.assertThat(bucket.getBucketItems().get(1).getBucket()).isEqualTo(bucket);
        Assertions.assertThat(bucket.getBucketItems().get(2).getBucket()).isEqualTo(bucket);
    }

    @Test
    void bucketItemService_linkBucket_notLinkBucketToBucketItem() {
        //given
        Bucket bucket = createBucket();
        bucket.setBucketItems(null);

        //when
        underTest.linkBucket(bucket);

        //then
        Mockito.verify(repository, Mockito.never()).save(any());
    }

    @Test
    void bucketItemService_removeBucket_returnBucketItemWithoutBucket() {
        //given
        BucketItem bucketItem = createBucketItem();

        //when
        underTest.removeBucket(bucketItem);

        //then
        Mockito.verify(repository, Mockito.times(1)).save(any());
        Assertions.assertThat(bucketItem.getBucket()).isNull();
    }

    @Test
    void bucketItemService_removeBucket_returnBucket() {
        //given
        BucketItem bucketItem = createBucketItem();
        bucketItem.setBucket(null);

        //when
        underTest.removeBucket(bucketItem);

        //then
        Mockito.verify(repository, Mockito.never()).save(any());
        Assertions.assertThat(bucketItem.getBucket()).isNull();
    }

    private Bucket createBucket() {
        return Bucket.builder()
                .bucketItems(createListOfBucketItem())
                .totalPrice(BigDecimal.ONE)
                .totalQuantity(3)
                .build();
    }

    private BucketItem createBucketItem() {
        return BucketItem.builder()
                .bucket(new Bucket())
                .build();
    }

    private List<BucketItem> createListOfBucketItem() {
        List<BucketItem> bucketItems = new ArrayList<>();
        IntStream.of(BucketItemServiceTest.BUCKET_ITEMS_ID).forEach(id -> bucketItems.add(BucketItem.builder()
                .quantity(id * 3)
                .item(Item.builder().price(100 * id).sale((5 * id)/100.).build())
                .build()));
        return bucketItems;
    }
}