package com.garfield.framedataapi.gameCharacters;

import com.garfield.framedataapi.config.authorization.Admin;
import com.garfield.framedataapi.config.authorization.Public;
import com.garfield.framedataapi.config.structure.ApiResponse;
import com.garfield.framedataapi.config.structure.ApiResponseEntity;
import com.garfield.framedataapi.config.structure.BaseApiController;
import com.garfield.framedataapi.gameCharacters.dtos.CreateGameCharacterDto;
import com.garfield.framedataapi.gameCharacters.dtos.GameCharacterDto;
import com.garfield.framedataapi.games.Game;
import com.garfield.framedataapi.games.GamesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;
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
    public ResponseEntity<ApiResponse<Set<GameCharacterDto>>> getGameCharacterByNameOrUuid(
            @PathVariable("nameOrUuidd") String nameOrUuid) {
        Set<GameCharacter> gameCharacters;

        try {
            gameCharacters = Set.of(this.gameCharactersService.getGameCharactersById(UUID.fromString(nameOrUuid)));
        } catch (IllegalArgumentException e) {
            gameCharacters = this.gameCharactersService.getGameCharactersByName(nameOrUuid);
        }

        return ApiResponseEntity.ok(
                gameCharacters.stream().map(GameCharacterDto::fromEntity).collect(Collectors.toSet())
        );
    }

    @Public
    @GetMapping("game/{gameNameOrUuid}")
    public ResponseEntity<ApiResponse<Set<GameCharacterDto>>> getGameCharactersByGame(
            @PathVariable("gameNameOrUuid") String nameOrUuid) {
        Game game;

        try {
            game = this.gamesService.getGameByIdentifier(UUID.fromString(nameOrUuid));
        } catch (IllegalArgumentException e) {
            game = this.gamesService.getGameByIdentifier(nameOrUuid);
        }

        return ApiResponseEntity.ok(
                game.getGameCharacters().stream().map(GameCharacterDto::fromEntity).collect(Collectors.toSet())
        );
    }

    @Admin
    @PostMapping
    public ResponseEntity<ApiResponse<GameCharacterDto>> createGameCharacter(
            @RequestBody CreateGameCharacterDto dto) {
        Game game = this.gamesService.getGameByIdentifier(dto.gameId());
        GameCharacter newGameCharacter = new GameCharacter(dto.name(), game);

        this.gameCharactersService.createGameCharacter(newGameCharacter);

        return ApiResponseEntity.created(createControllerUri(String.format("name/%s", newGameCharacter.getId())));
    }

}
