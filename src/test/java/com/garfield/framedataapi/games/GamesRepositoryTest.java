package com.garfield.framedataapi.games;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GamesRepositoryTest {

    private GamesRepository repository;

    @BeforeEach
    void setUp() {
        repository = mock(GamesRepository.class);
    }

    @Test
    void getByName_shouldReturnMockedGame() {
        Game mockGame = new Game("Tekken");
        when(repository.getByName("Tekken")).thenReturn(mockGame);

        Game result = repository.getByName("Tekken");

        assertNotNull(result);
        assertEquals("Tekken", result.getName());
        verify(repository, times(1)).getByName("Tekken");
    }

    @Test
    void getById_shouldReturnMockedGame() {
        Game mockGame = new Game("Street Fighter");
        when(repository.getById(mockGame.getId())).thenReturn(mockGame);

        Game result = repository.getById(mockGame.getId());

        assertNotNull(result);
        assertEquals(mockGame.getId(), result.getId());
        verify(repository, times(1)).getById(mockGame.getId());
    }

    @Test
    void findAll_shouldReturnMockedList() {
        List<Game> games = List.of(
                new Game("Tekken 8"),
                new Game("Street Fighter 6")
        );
        when(repository.findAll()).thenReturn(games);

        List<Game> result = repository.findAll();

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(g -> g.getName().equals("Tekken 8")));
        verify(repository, times(1)).findAll();
    }

}
