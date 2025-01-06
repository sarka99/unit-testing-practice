package com.pokemonreview.api.service;

import com.pokemonreview.api.dto.PokemonDto;
import com.pokemonreview.api.dto.PokemonResponse;
import com.pokemonreview.api.models.Pokemon;
import com.pokemonreview.api.repository.PokemonRepository;
import com.pokemonreview.api.service.impl.PokemonServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PokemonServiceTests {

    @Mock
    private PokemonRepository pokemonRepository;

    @InjectMocks
    private PokemonServiceImpl pokemonService;

    @Test
    public void PokemonService_CreatePokemon_ReturnsPokemonDto(){

        //Arrange
        Pokemon pokemon = Pokemon.builder().id(1).name("pikatchu").type("electric").build();
        PokemonDto pokemonDto = PokemonDto.builder().name("pikatchu").type("electric").build();

        //Act
        when(pokemonRepository.save(Mockito.any(Pokemon.class))).thenReturn(pokemon);
        PokemonDto savedPokemon = pokemonService.createPokemon(pokemonDto);

        //Assert
        Assertions.assertThat(savedPokemon).isNotNull();
        Assertions.assertThat(savedPokemon.getId()).isGreaterThan(0);
        Assertions.assertThat(savedPokemon.getName()).isEqualTo("pikatchu");
        Assertions.assertThat(savedPokemon.getType()).isEqualTo("electric");

    }

    @Test
    public void PokemonService_GetAllPokemon_ReturnsResponseDto(){
        //Creates a mock Page<Pokemon> object to simulate the database response
        Page<Pokemon> pokemons = Mockito.mock(Page.class);

        //Configures the mocked pokemonRepository to return the mocked Page<Pokemon> when findAll is called with any Pageable argument
        when(pokemonRepository.findAll(Mockito.any(Pageable.class))).thenReturn(pokemons);

        PokemonResponse savedPokemon = pokemonService.getAllPokemon(1,10);

        //Assert
        Assertions.assertThat(savedPokemon).isNotNull();

    }

    @Test
    public void PokemonService_GetPokemonById_ReturnsPokemonDto(){
        Pokemon pokemon = Pokemon.builder().id(1).name("pikatchu").type("electric").build();

        when(pokemonRepository.findById(Mockito.any(Integer.class))).thenReturn(Optional.ofNullable(pokemon));

        PokemonDto retrievedPokemon = pokemonService.getPokemonById(1);

        Assertions.assertThat(retrievedPokemon).isNotNull();
    }

    @Test
    public void PokemonService_UpdatePokemonById_ReturnsPokemonDto(){
        //Mock a pokemon object so we can use it to "save" pokemon object then find the object
        Pokemon pokemon = Pokemon.builder().id(1).name("pikatchu").type("electric").build();
        PokemonDto pokemonDto = PokemonDto.builder().id(1).name("pikatchu").type("electric").build();

        when(pokemonRepository.findById(Mockito.any(Integer.class))).thenReturn(Optional.ofNullable(pokemon));
        when(pokemonRepository.save(Mockito.any(Pokemon.class))).thenReturn(pokemon);

        PokemonDto savedPokemon = pokemonService.updatePokemon(pokemonDto,1);

        //Assert
        Assertions.assertThat(savedPokemon).isNotNull();
    }

    @Test
    public void PokemonService_DeletePokemonById_ReturnsNothing(){
        Pokemon pokemon = Pokemon.builder().id(1).name("pikatchu").type("electric").build();

        when(pokemonRepository.findById(Mockito.any(Integer.class))).thenReturn(Optional.ofNullable(pokemon));

        assertAll(() -> pokemonService.deletePokemonId(1));
    }
}
