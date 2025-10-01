package com.garfield.framedataapi.gameCharacters;

import com.garfield.framedataapi.config.structure.BaseApiController;
import com.garfield.framedataapi.config.authorization.Admin;
import com.garfield.framedataapi.config.authorization.Public;
import com.garfield.framedataapi.config.structure.ApiResponse;
import com.garfield.framedataapi.config.structure.ApiResponseEntity;
import com.garfield.framedataapi.gameCharacters.dtos.CreateGameCharacterDto;
import com.garfield.framedataapi.gameCharacters.dtos.GameCharacterDto;
import com.garfield.framedataapi.games.Game;
import com.garfield.framedataapi.games.GamesService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping(GameCharactersController.REQUEST_MAPPING)
public class GameCharactersController extends BaseApiController {

    public static final String REQUEST_MAPPING = "characters";

    private final GameCharactersService gameCharactersService;
    private final GamesService gamesService;

    public GameCharactersController(
            GameCharactersService gameCharactersService,
            GamesService gamesService) {
        this.gameCharactersService = gameCharactersService;
        this.gamesService = gamesService;
    }

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

    @Admin
    @PostMapping
    public ResponseEntity<ApiResponse<GameCharacterDto>> createGameCharacter(
            @RequestBody CreateGameCharacterDto dto) {
        Game game = this.gamesService.getGameByIdentifier(dto.gameId());
        GameCharacter newGameCharacter = new GameCharacter(dto.name(), game);

        this.gameCharactersService.createGameCharacter(newGameCharacter);

        return ApiResponseEntity.ok(GameCharacterDto.fromEntity(newGameCharacter));
    }

}
