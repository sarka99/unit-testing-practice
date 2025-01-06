package com.pokemonreview.api.service;

import com.pokemonreview.api.dto.ReviewDto;
import com.pokemonreview.api.models.Pokemon;
import com.pokemonreview.api.models.Review;
import com.pokemonreview.api.repository.PokemonRepository;
import com.pokemonreview.api.repository.ReviewRepository;
import com.pokemonreview.api.service.impl.ReviewServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTests {

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private PokemonRepository pokemonRepository;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    @Test
    public void ReviewService_CreateReview_ReturnsReviewDto(){
        Review review = Review.builder().id(1).title("title").content("content").stars(5).build();
        Pokemon pokemon = Pokemon.builder().id(1).name("pikatchu").type("electric").build();

        when(pokemonRepository.findById(pokemon.getId())).thenReturn(Optional.ofNullable(pokemon));
        when(reviewRepository.save(Mockito.any(Review.class))).thenReturn(review);

        ReviewDto createdReview =
                reviewService.createReview(
                pokemon.getId(), new ReviewDto(review.getId(),
                review.getTitle(),
                review.getContent(),
                review.getStars()));


        Assertions.assertThat(createdReview).isNotNull();
        Assertions.assertThat(createdReview.getId()).isEqualTo(1);

    }

    @Test
    public void ReviewService_GetReviewsByPokemonId_ReturnsMoreThanOnePokemon(){
        Pokemon pokemon = Pokemon.builder().id(1).name("pikatchu").type("electric").build();
        Review review = Review.builder().id(1).title("title").content("content").stars(5).build();

        when(reviewRepository.findByPokemonId(pokemon.getId())).thenReturn(Arrays.asList(review));

        List<ReviewDto> retrievedReviews = reviewService.getReviewsByPokemonId(pokemon.getId());

        Assertions.assertThat(retrievedReviews).isNotEmpty();

    }

    @Test
    public void ReviewService_GetReviewById_ReturnsReviewDto(){
        Pokemon pokemon = Pokemon.builder().id(1).name("pikatchu").type("electric").build();
        Review review = Review.builder().id(1).title("review").content("content").stars(5).build();

        review.setPokemon(pokemon);

        when(pokemonRepository.findById(pokemon.getId())).thenReturn(Optional.ofNullable(pokemon));
        when(reviewRepository.findById(review.getId())).thenReturn(Optional.ofNullable(review));

        ReviewDto retrievedDto = reviewService.getReviewById(review.getId(),pokemon.getId());

        Assertions.assertThat(retrievedDto).isNotNull();

    }

    @Test
    public void ReviewService_updateReview_ReturnsReviewDto(){
        Pokemon pokemon = Pokemon.builder().id(1).name("pikatchu").type("electric").build();
        Review review = Review.builder().id(1).title("title").content("content").stars(5).build();

        when(pokemonRepository.findById(pokemon.getId())).thenReturn(Optional.ofNullable(pokemon));
        when(reviewRepository.findById(review.getId())).thenReturn(Optional.ofNullable(review));

        pokemon.setReviews(Arrays.asList(review));
        review.setPokemon(pokemon);

        when(reviewRepository.save(review)).thenReturn(review);


        ReviewDto updatedReview = reviewService.updateReview(pokemon.getId(),review.getId(),
                new ReviewDto(review.getId(),
                        review.getTitle(),
                        review.getContent(),
                        review.getStars()));

        Assertions.assertThat(updatedReview).isNotNull();


    }

    @Test
    public void ReviewService_DeleteReview_ReturnsVoid(){
        Review review = Review.builder().id(1).title("title").content("content").stars(5).build();
        Pokemon pokemon = Pokemon.builder().id(1).name("pikatchu").type("electric").build();

        review.setPokemon(pokemon);
        pokemon.setReviews(Arrays.asList(review));

        when(reviewRepository.findById(review.getId())).thenReturn(Optional.ofNullable(review));
        when(pokemonRepository.findById(pokemon.getId())).thenReturn(Optional.ofNullable(pokemon));

        assertAll(() -> reviewService.deleteReview(pokemon.getId(), review.getId()));
    }
}
