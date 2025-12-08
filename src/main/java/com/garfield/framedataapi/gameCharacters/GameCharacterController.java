package com.garfield.framedataapi.gameCharacters;

import com.garfield.framedataapi.advice.authorization.Admin;
import com.garfield.framedataapi.advice.authorization.Public;
import com.garfield.framedataapi.advice.responses.ApiResponse;
import com.garfield.framedataapi.advice.responses.ApiResponseEntity;
import com.garfield.framedataapi.core.BaseApiController;
import com.garfield.framedataapi.gameCharacters.dtos.CreateGameCharacterDto;
import com.garfield.framedataapi.gameCharacters.dtos.GameCharacterDto;
import com.garfield.framedataapi.games.Game;
import com.garfield.framedataapi.games.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping(GameCharacterController.REQUEST_MAPPING)
@RequiredArgsConstructor
public class GameCharacterController extends BaseApiController {

    public static final String REQUEST_MAPPING = "characters";

    private final GameCharacterService gameCharacterService;
    private final GameService gameService;

    @Override
    public String getRequestMapping() {
        return REQUEST_MAPPING;
    }

    @Public
    @GetMapping("identifier/{nameOrUuidd}")
    public ResponseEntity<ApiResponse<GameCharacterDto>> getGameCharacterByNameOrUuid(
            @PathVariable String nameOrUuid) {
        GameCharacter gameCharacter = this.gameCharacterService.getGameCharacterByIdentifier(nameOrUuid);

        return ApiResponseEntity.ok(GameCharacterDto.fromEntity(gameCharacter));
    }

    @Public
    @GetMapping("game/{gameNameOrUuid}")
    public ResponseEntity<ApiResponse<Set<GameCharacterDto>>> getGameCharactersByGame(
            @PathVariable String gameNameOrUuid) {
        Game game = this.gameService.getGameByIdentifier(gameNameOrUuid);

        return ApiResponseEntity.ok(
                game.getGameCharacters().stream().map(GameCharacterDto::fromEntity).collect(Collectors.toSet())
        );
    }

    @Admin
    @PostMapping
    public ResponseEntity<ApiResponse<GameCharacterDto>> createGameCharacter(
            @RequestBody CreateGameCharacterDto dto) {
        Game game = this.gameService.getGameById(dto.gameId());
        GameCharacter newGameCharacter = new GameCharacter(dto.name(), game);

        this.gameCharacterService.createGameCharacter(newGameCharacter);

        return ApiResponseEntity.created(createControllerUri(String.format("name/%s", newGameCharacter.getId())));
    }

    @Admin
    @DeleteMapping("identifier/{gameCharacterId}")
    public ResponseEntity<ApiResponse<Void>> deleteGameCharacterById(@PathVariable UUID gameCharacterId) {
        GameCharacter gameCharacter = this.gameCharacterService.getGameCharacterById(gameCharacterId);

        this.gameCharacterService.deleteGameCharacter(gameCharacter);

        return ApiResponseEntity.deleted();
    }

}
