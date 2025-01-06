package com.pokemonreview.api.repository;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.pokemonreview.api.models.Pokemon;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class PokemonRepositoryTests {

    @Autowired
    private PokemonRepository pokemonRepository;

    @Test
    public void PokemonRepository_SaveAll_ReturnSavedPokemon() {
        //Arrange (Create a pokemon object)
        Pokemon pokemon = Pokemon.builder()
                .name("pikachu")
                .type("electric").build();

        //Act (Save the pokemon to database)
        Pokemon savedPokemon = pokemonRepository.save(pokemon);

        //Assert
        Assertions.assertThat(savedPokemon).isNotNull();

        Assertions.assertThat(savedPokemon.getId()).isGreaterThan(0);

        Assertions.assertThat(savedPokemon.getName()).isEqualTo(pokemon.getName());

        Assertions.assertThat(savedPokemon.getType()).isEqualTo(pokemon.getType());


    }

    @Test
    public void PokemonRepository_GetAll_ReturnsMoreThanOnePokemon(){
        //Arrange (Create fake pokemon objects)
        Pokemon pokemon1 = Pokemon
                .builder()
                .name("pikatchu")
                .type("electric")
                .build();
        Pokemon pokemon2 = Pokemon
                .builder()
                .name("snorlax")
                .type("yellow")
                .build();

        pokemonRepository.save(pokemon1);
        pokemonRepository.save(pokemon2);

        //Act
        List<Pokemon> pokemonList = pokemonRepository.findAll();

        //Assert
        Assertions.assertThat(pokemonList).isNotNull();
        Assertions.assertThat(pokemonList.size()).isEqualTo(2);

    }

    @Test
    public void PokemonRepository_FindById_ReturnsPokemon(){
        //Arrange (Create a pokemon object)
        Pokemon pokemon = Pokemon
                .builder()
                .name("pikatchu")
                .type("electric")
                .build();

        //Save the object to the database
        Pokemon createdPokemon = pokemonRepository.save(pokemon);

        //Act (The actual repository method being used)
        Pokemon retrievedPokemon = pokemonRepository.findById(createdPokemon.getId()).get();

        //Assert (test some cases based on the retrieved object)
        Assertions.assertThat(retrievedPokemon).isNotNull();
        Assertions.assertThat(retrievedPokemon.getId()).isGreaterThan(0);
        Assertions.assertThat(retrievedPokemon.getName()).isEqualTo("pikatchu");
        Assertions.assertThat(retrievedPokemon.getType()).isEqualTo("electric");
    }

    @Test
    public void PokemonRepository_FindByType_ReturnsPokemonNotNull(){
        //Arrange (Create a pookemon object)
        Pokemon pokemon = Pokemon
                .builder()
                .name("pikatchu")
                .type("electric")
                .build();
        //Save the object to the database
        Pokemon createdPokemon = pokemonRepository.save(pokemon);

        //Act (The actual repository method being used)
        Pokemon retrievedPokemon = pokemonRepository.findByType(createdPokemon.getType()).get();

        //Assert (Make sure retrieved pokemons field match the expected fields)
        Assertions.assertThat(retrievedPokemon).isNotNull();
        Assertions.assertThat(retrievedPokemon.getId()).isGreaterThan(0);
        Assertions.assertThat(retrievedPokemon.getName()).isEqualTo(createdPokemon.getName());
        Assertions.assertThat(retrievedPokemon.getType()).isEqualTo(createdPokemon.getType());

    }

    @Test
    public void PokemonRepository_UpdatePokemon_ReturnsPokemonNotNull(){
        //Arrange
        Pokemon pokemon = Pokemon
                .builder()
                .name("pikatchu")
                .type("electric")
                .build();
        //Save the pokemon to database
        Pokemon savedPokemon = pokemonRepository.save(pokemon);

        //Retrieve pokemon from database and update it
        Pokemon retrievedPokemon = pokemonRepository.findById(savedPokemon.getId()).get();
        retrievedPokemon.setName("sarre");
        retrievedPokemon.setType("hmoro");

        //Act (Use the actual repository method that we want to test)
        Pokemon updatedPokemon = pokemonRepository.save(retrievedPokemon);

        //Assert
        Assertions.assertThat(updatedPokemon).isNotNull();
        Assertions.assertThat(updatedPokemon.getId()).isGreaterThan(0);
        Assertions.assertThat(updatedPokemon.getName()).isEqualTo(retrievedPokemon.getName());
        Assertions.assertThat(updatedPokemon.getType()).isEqualTo(retrievedPokemon.getType());
    }

    @Test
    public void PokemonRepository_DeletePokemon_ReturnsPokemonIsEmpty(){
        //Arrange
        Pokemon pokemon = Pokemon
                .builder()
                .name("pikatchu")
                .type("electric")
                .build();
        Pokemon savedPokemon = pokemonRepository.save(pokemon);

        //Act
        pokemonRepository.deleteById(savedPokemon.getId());
        Optional<Pokemon> pokemonReturn = pokemonRepository.findById(savedPokemon.getId());

        //Assert (Make sure that the pokemon we deleted is actually gone)
        Assertions.assertThat(pokemonReturn).isEmpty();

    }

}