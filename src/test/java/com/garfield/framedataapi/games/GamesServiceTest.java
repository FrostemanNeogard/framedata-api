package com.garfield.framedataapi.games;

import com.garfield.framedataapi.games.exceptions.GameAlreadyExistsException;
import com.garfield.framedataapi.games.exceptions.GameNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GamesServiceTest {

    private GamesRepository gamesRepo;
    private GamesService gamesService;

    @BeforeEach
    void setUp() {
        gamesRepo = mock(GamesRepository.class);
        gamesService = new GamesService(gamesRepo);
    }

    @Test
    void getGameByIdentifier_uuid_whenGameExists_returnsGame() throws GameNotFoundException {
        Game game = new Game("Tekken");

        when(gamesRepo.getById(game.getId())).thenReturn(game);

        Game result = gamesService.getGameByIdentifier(game.getId());

        assertEquals(game, result);
        verify(gamesRepo).getById(game.getId());
    }

    @Test
    void getGameByIdentifier_uuid_whenGameDoesNotExist_throwsGameNotFoundException() {
        UUID id = UUID.randomUUID();

        when(gamesRepo.getById(id)).thenReturn(null);

        GameNotFoundException ex = assertThrows(GameNotFoundException.class, () -> gamesService.getGameByIdentifier(id));
        assertTrue(ex.getMessage().contains(id.toString()));
        verify(gamesRepo).getById(id);
    }

    @Test
    void getGameByIdentifier_name_whenGameExists_returnsGame() throws GameNotFoundException {
        String name = "Tekken";
        Game game = new Game(name);

        when(gamesRepo.getByName(name)).thenReturn(game);

        Game result = gamesService.getGameByIdentifier(name);

        assertEquals(game, result);
        verify(gamesRepo).getByName(name);
    }

    @Test
    void getGameByIdentifier_name_whenGameDoesNotExist_throwsGameNotFoundException() {
        String name = "Tekken";

        when(gamesRepo.getByName(name)).thenReturn(null);

        GameNotFoundException ex = assertThrows(GameNotFoundException.class, () -> gamesService.getGameByIdentifier(name));
        assertTrue(ex.getMessage().contains(name));
        verify(gamesRepo).getByName(name);
    }

    @Test
    void getAllGames_returnsAllGames() {
        List<Game> games = List.of(new Game("Tekken"), new Game("Street Fighter"));
        when(gamesRepo.findAll()).thenReturn(games);

        List<Game> result = gamesService.getAllGames();

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(g -> g.getName().equals("Tekken")));
        verify(gamesRepo).findAll();
    }

    @Test
    void createGame_whenGameDoesNotExist_savesGame() throws GameNotFoundException {
        Game game = new Game("Tekken");

        when(gamesRepo.getByName(game.getName())).thenReturn(null);

        gamesService.createGame(game);

        verify(gamesRepo).getByName(game.getName());
        verify(gamesRepo).save(game);
    }

    @Test
    void createGame_whenGameExists_throwsGameAlreadyExistsException() throws GameNotFoundException {
        Game game = new Game("Tekken");

        when(gamesRepo.getByName(game.getName())).thenReturn(game);

        GameAlreadyExistsException ex = assertThrows(GameAlreadyExistsException.class, () -> gamesService.createGame(game));
        assertTrue(ex.getMessage().contains(game.getName()));

        verify(gamesRepo).getByName(game.getName());
        verify(gamesRepo, never()).save(any());
    }

}
