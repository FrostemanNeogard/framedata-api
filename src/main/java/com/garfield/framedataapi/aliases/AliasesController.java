package com.garfield.framedataapi.aliases;

import com.garfield.framedataapi.advice.authorization.Admin;
import com.garfield.framedataapi.advice.authorization.Public;
import com.garfield.framedataapi.advice.responses.ApiResponse;
import com.garfield.framedataapi.advice.responses.ApiResponseEntity;
import com.garfield.framedataapi.aliases.dtos.AliasDto;
import com.garfield.framedataapi.aliases.dtos.CreateAliasDto;
import com.garfield.framedataapi.core.BaseApiController;
import com.garfield.framedataapi.gameCharacters.GameCharacter;
import com.garfield.framedataapi.gameCharacters.GameCharactersService;
import com.garfield.framedataapi.games.Game;
import com.garfield.framedataapi.games.GamesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
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
        Game game = this.gamesService.getGameByIdentifier(gameNameOrUuid);
        Alias alias = this.aliasesService.getAliasByGameAndIdentifier(game, aliasNameOrUuid);

        return ApiResponseEntity.ok(AliasDto.fromEntity(alias));
    }

    @Public
    @GetMapping("character/{characterNameOrUuid}")
    public ResponseEntity<ApiResponse<Set<AliasDto>>> getAliasesForCharacter(
            @PathVariable String characterNameOrUuid) {
        GameCharacter gameCharacter = this.gameCharactersService.getGameCharacterByIdentifier(characterNameOrUuid);

        return ApiResponseEntity.ok(gameCharacter.getAliases().stream()
                .map(AliasDto::fromEntity).collect(Collectors.toSet())
        );
    }

    @Admin
    @PostMapping
    public ResponseEntity<ApiResponse<AliasDto>> createAlias(
            @RequestBody CreateAliasDto createAliasDto) {
        GameCharacter gameCharacter = this.gameCharactersService.getGameCharacterById(createAliasDto.characterId());
        Alias alias = new Alias(createAliasDto.aliasName(), gameCharacter);

        this.aliasesService.createAlias(alias);

        return ApiResponseEntity.created(createControllerUri(alias.getId()));
    }

}
