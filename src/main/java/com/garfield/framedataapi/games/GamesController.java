package com.garfield.framedataapi.games;

import com.garfield.framedataapi.base.BaseApiController;
import com.garfield.framedataapi.config.Admin;
import com.garfield.framedataapi.exceptionhandler.ApiResponse;
import com.garfield.framedataapi.exceptionhandler.ApiResponseEntity;
import com.garfield.framedataapi.games.dto.CreateGameDto;
import com.garfield.framedataapi.games.dto.GameDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping(GamesController.REQUEST_MAPPING)
public class GamesController extends BaseApiController {

    public static final String REQUEST_MAPPING = "games";

    private final GamesService gamesService;

    public GamesController(GamesService gamesService) {
        this.gamesService = gamesService;
    }

    @Override
    public String getRequestMapping() {
        return REQUEST_MAPPING;
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<List<GameDto>>> getAllGames() {
        List<Game> games = this.gamesService.getAllGames();
        List<GameDto> gameDtos = games.stream().map(GameDto::fromEntity).toList();
        return ApiResponseEntity.ok(gameDtos);
    }

    @Admin
    @GetMapping("/admin")
    public ResponseEntity<ApiResponse<List<GameDto>>> getAllGamesAdmin() {
        List<Game> games = this.gamesService.getAllGames();
        List<GameDto> gameDtos = games.stream().map(GameDto::fromEntity).toList();
        return ApiResponseEntity.ok(gameDtos);
    }

    @GetMapping("name/{nameOrUuid}")
    public ResponseEntity<ApiResponse<GameDto>> getGameByNameOrUuid(@PathVariable String nameOrUuid) {
        Game game;
        try {
            game = this.gamesService.getGameByIdentifier(UUID.fromString(nameOrUuid));
        } catch (IllegalArgumentException e) {
            game = this.gamesService.getGameByIdentifier(nameOrUuid);
        }
        GameDto dto = GameDto.fromEntity(game);
        return ApiResponseEntity.ok(dto);
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<Game>> createGame(@Valid @RequestBody CreateGameDto dto) {
        Game newGame = new Game(dto.name());
        this.gamesService.createGame(newGame);
        return ApiResponseEntity.created(createControllerUri(newGame.getId().toString()));
    }

}
