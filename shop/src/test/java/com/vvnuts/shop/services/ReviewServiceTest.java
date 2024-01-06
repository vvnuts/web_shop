package com.vvnuts.shop.services;

import com.vvnuts.shop.dtos.requests.ReviewRequest;
import com.vvnuts.shop.dtos.requests.ReviewUpdateRequest;
import com.vvnuts.shop.dtos.responses.ReviewResponse;
import com.vvnuts.shop.entities.Review;
import com.vvnuts.shop.repositories.ReviewRepository;
import com.vvnuts.shop.utils.mappers.ReviewMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {
    @Mock
    private ReviewRepository repository;
    @Mock
    private ItemService itemService;
    @Mock
    private ReviewMapper mapper;
    @InjectMocks
    private ReviewService underTest;

    private static final Integer REVIEW_ID = 1;

    @Test
    void reviewService_create_returnSavedReview() {
        //given
        ReviewRequest request = ReviewRequest.builder()
                .mark(4)
                .text("text")
                .build();
        Review review = Review.builder()
                .mark(4)
                .text("text")
                .build();
        Review sReview = Review.builder()
                .id(1)
                .mark(4)
                .text("text")
                .build();
        //when
        when(mapper.transferDtoToEntity(any(ReviewRequest.class))).thenReturn(review);
        when(repository.save(any(Review.class))).thenReturn(sReview);
        Review save = underTest.create(request);

        //then
        Mockito.verify(mapper, Mockito.times(1)).transferDtoToEntity(any());
        Mockito.verify(repository, Mockito.times(1)).save(any());
        Mockito.verify(itemService, Mockito.times(1)).calculateRatingItem(any());
        Assertions.assertThat(save).isNotNull();
        Assertions.assertThat(save.getId()).isGreaterThan(0);
    }

    @Test
    void reviewService_create_throwException() {
        //given
        ReviewRequest request = ReviewRequest.builder()
                .mark(4)
                .text("text")
                .build();
        //when
        when(mapper.transferDtoToEntity(any(ReviewRequest.class))).thenThrow(NoSuchElementException.class);

        //then
        Mockito.verify(repository, Mockito.never()).save(any());
        Mockito.verify(itemService, Mockito.never()).calculateRatingItem(any());
        Assertions.assertThatThrownBy(() -> underTest.create(request)).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void reviewService_findOne_returnResponse() {
        //given
        Review review = Review.builder()
                .id(REVIEW_ID)
                .mark(4)
                .text("text")
                .build();
        ReviewResponse response = ReviewResponse.builder()
                .id(REVIEW_ID)
                .mark(4)
                .text("text")
                .build();
        when(repository.findById(REVIEW_ID)).thenReturn(Optional.of(review));
        when(mapper.convertEntityToResponse(review)).thenReturn(response);

        //when
        ReviewResponse result = underTest.findOne(REVIEW_ID);

        //then
        Mockito.verify(repository, Mockito.times(1)).findById(REVIEW_ID);
        Mockito.verify(mapper, Mockito.times(1)).convertEntityToResponse(review);
        Assertions.assertThat(result).isNotNull();
    }

    @Test
    void reviewService_findOne_throwException() {
        //given
        when(repository.findById(REVIEW_ID)).thenReturn(Optional.empty());

        //when
        //then
        Assertions.assertThatThrownBy(() -> underTest.findOne(REVIEW_ID))
                .isInstanceOf(NoSuchElementException.class);
        Mockito.verify(mapper, Mockito.never()).convertEntityToResponse(any());
    }

    @Test
    void reviewService_findById_returnReview() {
        //given
        Review review = Review.builder()
                .id(REVIEW_ID)
                .mark(4)
                .text("text")
                .build();
        when(repository.findById(REVIEW_ID)).thenReturn(Optional.of(review));

        //when
        Review result = underTest.findById(REVIEW_ID);

        //then
        Mockito.verify(repository, Mockito.times(1)).findById(REVIEW_ID);
        Assertions.assertThat(result.getId()).isEqualTo(REVIEW_ID);
    }

    @Test
    void reviewService_findById_throwException() {
        //given
        when(repository.findById(REVIEW_ID)).thenReturn(Optional.empty());

        //when
        //then
        Assertions.assertThatThrownBy(() -> underTest.findById(REVIEW_ID))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void reviewService_findAll_returnListResponse() {
        //given
        List<Review> reviews = List.of(new Review(), new Review());
        List<ReviewResponse> responses = List.of(new ReviewResponse(), new ReviewResponse());
        when(repository.findAll()).thenReturn(reviews);
        when(mapper.convertListEntityToResponse(reviews)).thenReturn(responses);

        //when
        List<ReviewResponse> result = underTest.findAll();

        //then
        Mockito.verify(repository, Mockito.times(1)).findAll();
        Mockito.verify(mapper, Mockito.times(1)).convertListEntityToResponse(reviews);
        Assertions.assertThat(result.size()).isEqualTo(reviews.size());
    }

    @Test
    void reviewService_update_returnUpdateReviewWithNewMarkAndText() {
        //given
        Review oldRequest = Review.builder()
                .id(REVIEW_ID)
                .mark(2)
                .text("old")
                .build();
        ReviewUpdateRequest request = ReviewUpdateRequest.builder()
                .mark(3)
                .text("update")
                .build();
        when(repository.findById(REVIEW_ID)).thenReturn(Optional.of(oldRequest));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        //when
        Review result = underTest.update(request, REVIEW_ID);

        //then
        Mockito.verify(repository, Mockito.times(1)).findById(any());
        Mockito.verify(repository, Mockito.times(1)).save(any());
        Assertions.assertThat(result.getText()).isEqualTo(request.getText());
        Assertions.assertThat(result.getMark()).isEqualTo(request.getMark());
    }

    @Test
    void reviewService_update_returnUpdateReviewWithNewText() {
        //given
        Review oldRequest = Review.builder()
                .id(REVIEW_ID)
                .mark(3)
                .text("old")
                .build();
        ReviewUpdateRequest request = ReviewUpdateRequest.builder()
                .mark(3)
                .text("update")
                .build();
        when(repository.findById(REVIEW_ID)).thenReturn(Optional.of(oldRequest));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        //when
        Review result = underTest.update(request, REVIEW_ID);

        //then
        Mockito.verify(repository, Mockito.times(1)).findById(any());
        Mockito.verify(repository, Mockito.times(1)).save(any());
        Assertions.assertThat(result.getText()).isEqualTo(request.getText());
        Assertions.assertThat(result.getMark()).isEqualTo(oldRequest.getMark());
    }

    @Test
    void reviewService_update_returnUpdateReviewWithNewMark() {
        //given
        Review oldRequest = Review.builder()
                .id(REVIEW_ID)
                .mark(3)
                .text("old")
                .build();
        ReviewUpdateRequest request = ReviewUpdateRequest.builder()
                .mark(4)
                .text("old")
                .build();
        when(repository.findById(REVIEW_ID)).thenReturn(Optional.of(oldRequest));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        //when
        Review result = underTest.update(request, REVIEW_ID);

        //then
        Mockito.verify(repository, Mockito.times(1)).findById(any());
        Mockito.verify(repository, Mockito.times(1)).save(any());
        Assertions.assertThat(result.getText()).isEqualTo(oldRequest.getText());
        Assertions.assertThat(result.getMark()).isEqualTo(request.getMark());
    }

    @Test
    void reviewService_update_returnOldReview() {
        //given
        Review oldRequest = Review.builder()
                .id(REVIEW_ID)
                .mark(3)
                .text("old")
                .build();
        ReviewUpdateRequest request = ReviewUpdateRequest.builder()
                .mark(3)
                .text("old")
                .build();
        when(repository.findById(REVIEW_ID)).thenReturn(Optional.of(oldRequest));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        //when
        Review result = underTest.update(request, REVIEW_ID);

        //then
        Mockito.verify(repository, Mockito.times(1)).findById(any());
        Mockito.verify(repository, Mockito.times(1)).save(any());
        Assertions.assertThat(result.getText()).isEqualTo(oldRequest.getText());
        Assertions.assertThat(result.getMark()).isEqualTo(oldRequest.getMark());
    }

    @Test
    void reviewService_delete_canDelete() {
        //given
        Review deleteReview = Review.builder()
                .id(REVIEW_ID)
                .build();
        when(repository.findById(REVIEW_ID)).thenReturn(Optional.of(deleteReview));

        //when
        underTest.delete(REVIEW_ID);

        //then
        Mockito.verify(repository, times(1)).findById(REVIEW_ID);
        Mockito.verify(repository, times(1)).delete(deleteReview);
    }

    @Test
    void reviewService_delete_throwException() {
        //given
        when(repository.findById(REVIEW_ID)).thenReturn(Optional.empty());

        //when
        //then
        Mockito.verify(repository, never()).delete(any());
        Assertions.assertThatThrownBy(() -> underTest.delete(REVIEW_ID))
                .isInstanceOf(NoSuchElementException.class);
    }
}