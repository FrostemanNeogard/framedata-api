package com.garfield.framedataapi.gameCharacters;

import com.garfield.framedataapi.advice.authorization.Admin;
import com.garfield.framedataapi.advice.authorization.Public;
import com.garfield.framedataapi.advice.responses.ApiResponse;
import com.garfield.framedataapi.advice.responses.ApiResponseEntity;
import com.garfield.framedataapi.core.BaseApiController;
import com.garfield.framedataapi.gameCharacters.dtos.CreateGameCharacterDto;
import com.garfield.framedataapi.gameCharacters.dtos.GameCharacterDto;
import com.garfield.framedataapi.games.Game;
import com.garfield.framedataapi.games.GamesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping(GameCharactersController.REQUEST_MAPPING)
@RequiredArgsConstructor
public class GameCharactersController extends BaseApiController {

    public static final String REQUEST_MAPPING = "characters";

    private final GameCharactersService gameCharactersService;
    private final GamesService gamesService;

    @Override
    public String getRequestMapping() {
        return REQUEST_MAPPING;
    }

    @Public
    @GetMapping("name/{nameOrUuidd}")
    public ResponseEntity<ApiResponse<GameCharacterDto>> getGameCharacterByNameOrUuid(
            @PathVariable String nameOrUuid) {
        GameCharacter gameCharacter = this.gameCharactersService.getGameCharacterByIdentifier(nameOrUuid);

        return ApiResponseEntity.ok(GameCharacterDto.fromEntity(gameCharacter));
    }

    @Public
    @GetMapping("game/{gameNameOrUuid}")
    public ResponseEntity<ApiResponse<Set<GameCharacterDto>>> getGameCharactersByGame(
            @PathVariable String gameNameOrUuid) {
        Game game = this.gamesService.getGameByIdentifier(gameNameOrUuid);

        return ApiResponseEntity.ok(
                game.getGameCharacters().stream().map(GameCharacterDto::fromEntity).collect(Collectors.toSet())
        );
    }

    @Admin
    @PostMapping
    public ResponseEntity<ApiResponse<GameCharacterDto>> createGameCharacter(
            @RequestBody CreateGameCharacterDto dto) {
        Game game = this.gamesService.getGameById(dto.gameId());
        GameCharacter newGameCharacter = new GameCharacter(dto.name(), game);

        this.gameCharactersService.createGameCharacter(newGameCharacter);

        return ApiResponseEntity.created(createControllerUri(String.format("name/%s", newGameCharacter.getId())));
    }

}
