package com.pokemonreview.api.repository;

import com.pokemonreview.api.models.Pokemon;
import com.pokemonreview.api.models.Review;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ReviewRepositoryTests {
    private ReviewRepository reviewRepository;

    @Autowired
    public ReviewRepositoryTests(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Test
    public void ReviewRepository_SaveAll_ReturnsSavedReview(){

        //Arrange (Create an arrange object)
        Review review = Review
                .builder()
                .title("title")
                .content("content 1")
                .stars(5)
                .build();

        //Act (Save the object using repository save method)
        Review savedReview = reviewRepository.save(review);

        //Assert
        Assertions.assertThat(savedReview).isNotNull();

        Assertions.assertThat(savedReview.getId()).isGreaterThan(0);

        Assertions.assertThat(savedReview.getTitle()).isEqualTo("title");

        Assertions.assertThat(savedReview.getContent()).isEqualTo("content 1");

        Assertions.assertThat(savedReview.getStars()).isEqualTo(5);


    }

    @Test
    public void ReviewRepository_GetAll_ReturnsMoreThanOneReview(){

        //Arrange (Create and save two reviews)
        Review review1 = Review
                .builder()
                .title("title1")
                .content("content1")
                .stars(5)
                .build();
        Review review2 = Review
                .builder()
                .title("title2")
                .content("content2")
                .stars(4)
                .build();
        reviewRepository.save(review1);
        reviewRepository.save(review2);

        //Act (Retrieve all reviews using the findAll repository method)
        List<Review> retrievedReviews = reviewRepository.findAll();

        //Assert
        Assertions.assertThat(retrievedReviews).isNotEmpty();

        Assertions.assertThat(retrievedReviews.size()).isEqualTo(2);

    }

    @Test
    public void ReviewRepository_FindById_ReturnsSavedReview(){

        //Arrange
        Review review = Review
                .builder()
                .title("title")
                .content("content")
                .stars(5)
                .build();
        Review savedReview = reviewRepository.save(review);

        //Act
        Review retrievedReview = reviewRepository.findById(savedReview.getId()).get();

        //Assert
        Assertions.assertThat(retrievedReview).isNotNull();

        Assertions.assertThat(retrievedReview.getId()).isGreaterThan(0);

        Assertions.assertThat(retrievedReview.getTitle()).isEqualTo(review.getTitle());

        Assertions.assertThat(retrievedReview.getContent()).isEqualTo(review.getContent());

        Assertions.assertThat(retrievedReview.getStars()).isEqualTo(review.getStars());

    }

    @Test
    public void ReviewRepository_UpdateReview_ReturnReview(){

        //Arrange
        Review review = Review
                .builder()
                .title("title")
                .content("content")
                .stars(5)
                .build();
        Review savedReview = reviewRepository.save(review);
        Review updatedReview = reviewRepository.findById(savedReview.getId()).get();
        //Act
        updatedReview.setStars(1);
        updatedReview.setContent("great content");
        reviewRepository.save(updatedReview);

        //Assert
        Assertions.assertThat(updatedReview).isNotNull();

        Assertions.assertThat(updatedReview.getId()).isGreaterThan(0);

        Assertions.assertThat(updatedReview.getTitle()).isEqualTo("title");

        Assertions.assertThat(updatedReview.getContent()).isEqualTo("great content");

        Assertions.assertThat(updatedReview.getStars()).isEqualTo(1);
    }

    @Test
    public void ReviewRepository_DeleteReview_ReturnsReviewIsEmpty(){

        //Arrange
        Review review = Review
                .builder()
                .title("title")
                .content("content")
                .stars(5)
                .build();
        Review savedReview = reviewRepository.save(review);

        //Act
        reviewRepository.deleteById(savedReview.getId());
        Optional<Review> reviewReturn = reviewRepository.findById(savedReview.getId());
        //Assert
        Assertions.assertThat(reviewReturn).isEmpty();

    }
}
