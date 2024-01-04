package com.vvnuts.shop.services;

import com.vvnuts.shop.entities.Review;
import com.vvnuts.shop.entities.ReviewImage;
import com.vvnuts.shop.exceptions.FileIsEmptyException;
import com.vvnuts.shop.repositories.ReviewImageRepository;
import com.vvnuts.shop.repositories.ReviewRepository;
import com.vvnuts.shop.utils.ImageUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewImageServiceTest {
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private ReviewImageRepository repository;
    @InjectMocks
    private ReviewImageService underTest;

    private static final int reviewId = 1;
    private static final int imageId = 1;

    @Test
    void reviewImageService_uploadImage_returnReviewWithImage() throws IOException {
        //given
        MultipartFile file = getFile();
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(getReview()));
        when(reviewRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        //when

        Review upd = underTest.uploadImage(file, reviewId);

        //then
        Assertions.assertThat(upd.getImages()).isNotEmpty();
        Assertions.assertThat(upd.getImages().size()).isEqualTo(1);
        Assertions.assertThat(upd.getImages().get(0).getImage()).isEqualTo(ImageUtils.compressImage(file.getBytes()));
    }

    @Test
    void reviewImageService_uploadImage_returnReviewWithTwoImage() throws IOException {
        //given
        MultipartFile file = getFile();
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(getReviewWithImage()));
        when(reviewRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        //when
        Review upd = underTest.uploadImage(file, reviewId);

        //then
        Assertions.assertThat(upd.getImages()).isNotEmpty();
        Assertions.assertThat(upd.getImages().size()).isEqualTo(2);
    }

    @Test
    void reviewImageService_uploadImage_throwFileIsEmptyException() {
        //given
        MultipartFile file = getEmptyFile();

        //when
        //then
        Assertions.assertThatThrownBy(() -> underTest.uploadImage(file, reviewId))
                .isInstanceOf(FileIsEmptyException.class);
        Mockito.verify(repository, never()).save(any());
    }

    @Test
    void reviewImageService_downloadImage_returnImage() {
        //given
        ReviewImage reviewImage = getReviewImage();
        when(repository.findById(imageId)).thenReturn(Optional.of(reviewImage));

        //when
        byte[] data = underTest.downloadImage(imageId);

        //then
        Assertions.assertThat(data).isNotEmpty();
        Assertions.assertThat(data).isEqualTo(ImageUtils.decompressImage(reviewImage.getImage()));
    }

    @Test
    void reviewImageService_downloadImage_returnNull() {
        //given
        ReviewImage reviewImage = getReviewImage();
        reviewImage.setImage(null);
        when(repository.findById(imageId)).thenReturn(Optional.of(reviewImage));

        //when
        byte[] data = underTest.downloadImage(imageId);

        //then
        Assertions.assertThat(data).isNull();
    }

    @Test
    void reviewImageService_delete_canDelete() {
        //given
        ReviewImage deleteReviewImage = getReviewImage();
        when(repository.findById(imageId)).thenReturn(Optional.of(deleteReviewImage));

        //when
        underTest.deleteImage(imageId);

        //then
        Mockito.verify(repository, times(1)).findById(imageId);
        Mockito.verify(repository, times(1)).delete(deleteReviewImage);
    }

    @Test
    void reviewImageService_delete_throwException() {
        //given
        when(repository.findById(imageId)).thenReturn(Optional.empty());

        //when
        //then
        Mockito.verify(repository, never()).delete(any());
        Assertions.assertThatThrownBy(() -> underTest.deleteImage(imageId))
                .isInstanceOf(NoSuchElementException.class);
    }

    private Review getReview() {
        return Review.builder()
                .id(reviewId)
                .text("text")
                .mark(4)
                .build();
    }

    private Review getReviewWithImage() {
        Review review = Review.builder()
                .id(reviewId)
                .text("text")
                .mark(4)
                .images(new ArrayList<>())
                .build();
        review.getImages().add(getReviewImage());
        return review;
    }

    private ReviewImage getReviewImage() {
        return new ReviewImage(1, ImageUtils.compressImage(new byte[] {12, 11, 13}), null);
    }

    private MockMultipartFile getFile() {
        return new MockMultipartFile("name.png",
                "originalFileName.png", "image/png", new byte[] {11, 101, 12, 13});
    }

    private MockMultipartFile getEmptyFile() {
        return new MockMultipartFile("name.png",
                "originalFileName.png", "image/png", new byte[] {});
    }
}