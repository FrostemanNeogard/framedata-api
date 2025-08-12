package com.garfield.framedataapi.games;

import com.garfield.framedataapi.exceptionhandler.ApiResponse;
import com.garfield.framedataapi.games.dto.CreateGameDto;
import com.garfield.framedataapi.games.dto.GameDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GamesControllerTest {

    private GamesService gamesService;
    private GamesController gamesController;

    @BeforeEach
    void setUp() {
        gamesService = mock(GamesService.class);

        gamesController = new GamesController(gamesService) {
            @Override
            protected URI createControllerUri(String path) {
                return URI.create(String.format("/api/v1/%s/%s", getRequestMapping(), path));
            }
        };
    }

    @Test
    void getAllGames_returnsListOfGameDtos() {
        Game game1 = new Game("Tekken");
        Game game2 = new Game("Street Fighter");
        when(gamesService.getAllGames()).thenReturn(List.of(game1, game2));

        ResponseEntity<ApiResponse<List<GameDto>>> response = gamesController.getAllGames();

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertNotNull(response.getBody());
        List<GameDto> dtos = response.getBody().getData();
        assertEquals(2, dtos.size());
        assertTrue(dtos.stream().anyMatch(dto -> dto.name().equals("Tekken")));
        verify(gamesService).getAllGames();
    }

    @Test
    void getGameByNameOrUuid_withUuid_callsServiceById() {
        UUID id = UUID.randomUUID();
        Game game = new Game("Tekken");
        setId(game, id);
        when(gamesService.getGameByIdentifier(id)).thenReturn(game);

        ResponseEntity<ApiResponse<GameDto>> response = gamesController.getGameByNameOrUuid(id.toString());

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertNotNull(response.getBody());
        GameDto dto = response.getBody().getData();
        assertEquals("Tekken", dto.name());
        verify(gamesService).getGameByIdentifier(id);
        verify(gamesService, never()).getGameByIdentifier(anyString());
    }

    @Test
    void getGameByNameOrUuid_withName_callsServiceByName() {
        String name = "Tekken";
        Game game = new Game(name);
        when(gamesService.getGameByIdentifier(name)).thenReturn(game);

        ResponseEntity<ApiResponse<GameDto>> response = gamesController.getGameByNameOrUuid(name);

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertNotNull(response.getBody());
        GameDto dto = response.getBody().getData();
        assertEquals(name, dto.name());
        verify(gamesService).getGameByIdentifier(name);
        verify(gamesService, never()).getGameByIdentifier(any(UUID.class));
    }

    @Test
    void createGame_createsGameAndReturnsCreatedResponse() {
        String gameName = "Tekken";
        CreateGameDto dto = new CreateGameDto(gameName);

        Game savedGame = new Game(gameName);
        UUID id = UUID.randomUUID();
        setId(savedGame, id);

        doAnswer(invocation -> {
            Game g = invocation.getArgument(0);
            setId(g, id);
            return null;
        }).when(gamesService).createGame(any(Game.class));

        ResponseEntity<ApiResponse<Game>> response = gamesController.createGame(dto);

        assertEquals(HttpStatusCode.valueOf(201), response.getStatusCode());
        assertNotNull(response.getHeaders().getLocation());
        assertTrue(response.getHeaders().getLocation().toString().contains(id.toString()));

        ArgumentCaptor<Game> captor = ArgumentCaptor.forClass(Game.class);
        verify(gamesService).createGame(captor.capture());
        assertEquals(gameName, captor.getValue().getName());
    }

    private void setId(Game game, UUID id) {
        try {
            var field = Game.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(game, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
