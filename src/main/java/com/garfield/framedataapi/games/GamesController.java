package com.garfield.framedataapi.games;

import com.garfield.framedataapi.advice.authorization.Admin;
import com.garfield.framedataapi.advice.authorization.Public;
import com.garfield.framedataapi.advice.responses.ApiResponse;
import com.garfield.framedataapi.advice.responses.ApiResponseEntity;
import com.garfield.framedataapi.core.BaseApiController;
import com.garfield.framedataapi.framedata.FramedataTemplate;
import com.garfield.framedataapi.games.dto.CreateGameDto;
import com.garfield.framedataapi.games.dto.GameDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping(GamesController.REQUEST_MAPPING)
@RequiredArgsConstructor
public class GamesController extends BaseApiController {

    public static final String REQUEST_MAPPING = "games";

    private final GamesService gamesService;

    @Override
    public String getRequestMapping() {
        return REQUEST_MAPPING;
    }

    @Public
    @GetMapping()
    public ResponseEntity<ApiResponse<List<GameDto>>> getAllGames() {
        return ApiResponseEntity.ok(
                this.gamesService.getAllGames().stream().map(GameDto::fromEntity).toList()
        );
    }

    @Public
    @GetMapping("name/{nameOrUuid}")
    public ResponseEntity<ApiResponse<GameDto>> getGameByNameOrUuid(@PathVariable String nameOrUuid) {
        Game game = this.gamesService.getGameByIdentifier(nameOrUuid);

        return ApiResponseEntity.ok(GameDto.fromEntity(game));
    }

    @Admin
    @PostMapping()
    public ResponseEntity<ApiResponse<Game>> createGame(@Valid @RequestBody CreateGameDto dto) {
        FramedataTemplate template = new FramedataTemplate(dto.attributesTemplate());

        Game newGame = new Game(
                dto.name(),
                template);

        this.gamesService.createGame(newGame);

        return ApiResponseEntity.created(createControllerUri(newGame.getId().toString()));
    }

    @Admin
    @DeleteMapping("{gameId}")
    public ResponseEntity<ApiResponse<Void>> deleteGame(@PathVariable UUID gameId) {
        this.gamesService.deleteGameById(gameId);

        return ApiResponseEntity.deleted();
    }

}
