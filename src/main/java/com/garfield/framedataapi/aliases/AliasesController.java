package com.garfield.framedataapi.aliases;

import com.garfield.framedataapi.aliases.dtos.AliasDto;
import com.garfield.framedataapi.aliases.dtos.CreateAliasDto;
import com.garfield.framedataapi.config.authorization.Admin;
import com.garfield.framedataapi.config.authorization.Public;
import com.garfield.framedataapi.config.structure.ApiResponse;
import com.garfield.framedataapi.config.structure.ApiResponseEntity;
import com.garfield.framedataapi.config.structure.BaseApiController;
import com.garfield.framedataapi.gameCharacters.GameCharacter;
import com.garfield.framedataapi.gameCharacters.GameCharactersService;
import com.garfield.framedataapi.gameCharacters.exceptions.GameCharacterNotFoundException;
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
@RequestMapping(AliasesController.REQUEST_MAPPING)
@RequiredArgsConstructor
public class AliasesController extends BaseApiController {

    public static final String REQUEST_MAPPING = "aliases";

    private final AliasesService aliasesService;
    private final GameCharactersService gameCharactersService;
    private final GamesService gamesService;

    @Override
    public String getRequestMapping() {
        return REQUEST_MAPPING;
    }

    @Public
    @GetMapping("game/{gameNameOrUuid}/alias/{aliasNameOrUuid}")
    public ResponseEntity<ApiResponse<AliasDto>> getAliasByIdentifier(
            @PathVariable("gameNameOrUuid") String gameNameOrUuid,
            @PathVariable("aliasNameOrUuid") String aliasNameOrUuid) {
        Game game;

        try {
            game = this.gamesService.getGameByIdentifier(UUID.fromString(gameNameOrUuid));
        } catch (IllegalArgumentException e) {
            game = this.gamesService.getGameByIdentifier(gameNameOrUuid);
        }

        Alias alias;

        try {
            alias = this.aliasesService.getAliasByIdentifier(UUID.fromString(aliasNameOrUuid));
        } catch (IllegalArgumentException e) {
            alias = this.aliasesService.getAliasByIdentifier(game, aliasNameOrUuid);
        }

        return ApiResponseEntity.ok(AliasDto.fromEntity(alias));
    }

    @Public
    @GetMapping("character/{characterNameOrUuid}")
    public ResponseEntity<ApiResponse<Set<Set<AliasDto>>>> getAliasesForCharacter(
            @PathVariable String characterNameOrUuid) {
        Set<GameCharacter> gameCharacters;

        try {
            gameCharacters = Set.of(this.gameCharactersService.getGameCharactersById(UUID.fromString(characterNameOrUuid)));
        } catch (IllegalArgumentException e) {
            gameCharacters = this.gameCharactersService.getGameCharactersByName(characterNameOrUuid);
        }

        if (gameCharacters.isEmpty()) {
            throw new GameCharacterNotFoundException(characterNameOrUuid);
        }

        Set<Set<Alias>> aliases = gameCharacters.stream().map(GameCharacter::getAliases).collect(Collectors.toSet());

        return ApiResponseEntity.ok(aliases.stream()
                .map(a -> a.stream()
                        .map(AliasDto::fromEntity).collect(Collectors.toSet()))
                .collect(Collectors.toSet())
        );
    }

    @Admin
    @PostMapping
    public ResponseEntity<ApiResponse<AliasDto>> createAlias(
            @RequestBody CreateAliasDto createAliasDto) {
        GameCharacter gameCharacter = this.gameCharactersService.getGameCharactersById(createAliasDto.characterId());
        Alias alias = new Alias(createAliasDto.aliasName(), gameCharacter);

        this.aliasesService.createAlias(alias);

        return ApiResponseEntity.created(createControllerUri(alias.getId()));
    }

}
