package com.vvnuts.shop.services.implementation;

import com.vvnuts.shop.entities.Item;
import com.vvnuts.shop.entities.Review;
import com.vvnuts.shop.entities.User;
import com.vvnuts.shop.repositories.ReviewRepository;
import com.vvnuts.shop.services.interfaces.ItemService;
import com.vvnuts.shop.services.interfaces.ReviewService;
import com.vvnuts.shop.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewServiceImplementation extends AbstractCrudService<Review, Integer> implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final ItemService itemService;
    private final UserService userService;
    @Override
    JpaRepository<Review, Integer> getRepository() {
        return reviewRepository;
    }

    @Override
    public void create(Review entity) {
        Item item = itemService.findById(entity.getItem().getItemId());
        entity.setItem(item);
        User user = userService.findById(entity.getUser().getUserId());
        entity.setUser(user);
        super.create(entity);
    }
}
