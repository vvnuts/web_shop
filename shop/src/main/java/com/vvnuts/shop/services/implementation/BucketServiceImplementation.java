package com.vvnuts.shop.services.implementation;

import com.vvnuts.shop.dtos.BucketDTO;
import com.vvnuts.shop.entities.Bucket;
import com.vvnuts.shop.entities.BucketItem;
import com.vvnuts.shop.repositories.BucketRepository;
import com.vvnuts.shop.services.BucketItemService;
import com.vvnuts.shop.services.interfaces.BucketService;
import com.vvnuts.shop.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class BucketServiceImplementation extends AbstractCrudService<Bucket, BucketDTO, Integer> implements BucketService {
    private final BucketRepository bucketRepository;
    private final BucketItemService bucketItemService;
    private final UserService userService;
    @Override
    JpaRepository<Bucket, Integer> getRepository() {
        return bucketRepository;
    }

    @Override
    Bucket transferToUpdateEntity(BucketDTO dto, Bucket updateEntity) { //TODO переписать controller on update
        updateEntity.setBucketItems(bucketItemService.transferBucketItemDtoToList(dto.getOrderItemDTOs()));
        return updateEntity;
    }

    @Override
    public void update(BucketDTO dtoEntity, Integer id) {
        Bucket updateEntity = findBucketByUserId(id);
        Bucket bucket = transferToUpdateEntity(dtoEntity, updateEntity);
        calculationQuantityAndPrice(bucket);
        bucketRepository.save(bucket);
    }

    @Override
    Bucket transferToCreateEntity(BucketDTO dto) {
        Bucket bucket = Bucket.builder()
                .user(userService.findById(dto.getUser().getUserId()))
                .bucketItems(bucketItemService.transferBucketItemDtoToList(dto.getOrderItemDTOs()))
                .build();
        calculationQuantityAndPrice(bucket);
        return bucket;
    }

    @Override
    public Bucket findBucketByUserId(Integer userId) {
        return bucketRepository.findByUser(userService.findById(userId));
    }

    private void calculationQuantityAndPrice(Bucket bucket) {
        BigDecimal totalSum = BigDecimal.ZERO;
        Integer totalQuantity = 0;
        for (BucketItem bucketItem: bucket.getBucketItems()) {
            totalSum = totalSum.add(BigDecimal.valueOf(bucketItem.getItem().getPrice() * (1 + bucketItem.getItem().getSale())));
            totalQuantity += bucketItem.getQuantity();
        }
        bucket.setTotalPrice(totalSum);
        bucket.setTotalQuantity(totalQuantity);
    }
}
