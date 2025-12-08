package com.garfield.framedataapi.aliases;

import com.garfield.framedataapi.advice.authorization.Admin;
import com.garfield.framedataapi.advice.authorization.Public;
import com.garfield.framedataapi.advice.responses.ApiResponse;
import com.garfield.framedataapi.advice.responses.ApiResponseEntity;
import com.garfield.framedataapi.aliases.dtos.AliasDto;
import com.garfield.framedataapi.aliases.dtos.CreateAliasDto;
import com.garfield.framedataapi.core.BaseApiController;
import com.garfield.framedataapi.gameCharacters.GameCharacter;
import com.garfield.framedataapi.gameCharacters.GameCharacterService;
import com.garfield.framedataapi.games.Game;
import com.garfield.framedataapi.games.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping(AliasController.REQUEST_MAPPING)
@RequiredArgsConstructor
public class AliasController extends BaseApiController {

    public static final String REQUEST_MAPPING = "aliases";

    private final AliasService aliasService;
    private final GameCharacterService gameCharacterService;
    private final GameService gameService;

    @Override
    public String getRequestMapping() {
        return REQUEST_MAPPING;
    }

    @Public
    @GetMapping("game/{gameNameOrUuid}/alias/{aliasNameOrUuid}")
    public ResponseEntity<ApiResponse<AliasDto>> getAliasByIdentifier(
            @PathVariable String gameNameOrUuid,
            @PathVariable String aliasNameOrUuid) {
        Game game = this.gameService.getGameByIdentifier(gameNameOrUuid);
        Alias alias = this.aliasService.getAliasByGameAndIdentifier(game, aliasNameOrUuid);

        return ApiResponseEntity.ok(AliasDto.fromEntity(alias));
    }

    @Public
    @GetMapping("character/{characterNameOrUuid}")
    public ResponseEntity<ApiResponse<Set<AliasDto>>> getAliasesForCharacter(
            @PathVariable String characterNameOrUuid) {
        GameCharacter gameCharacter = this.gameCharacterService.getGameCharacterByIdentifier(characterNameOrUuid);

        return ApiResponseEntity.ok(gameCharacter.getAliases().stream()
                .map(AliasDto::fromEntity).collect(Collectors.toSet())
        );
    }

    @Admin
    @PostMapping
    public ResponseEntity<ApiResponse<AliasDto>> createAlias(
            @RequestBody CreateAliasDto createAliasDto) {
        GameCharacter gameCharacter = this.gameCharacterService.getGameCharacterById(createAliasDto.characterId());
        Alias alias = new Alias(createAliasDto.aliasName(), gameCharacter);

        this.aliasService.createAlias(alias);

        return ApiResponseEntity.created(createControllerUri(alias.getId()));
    }

}
