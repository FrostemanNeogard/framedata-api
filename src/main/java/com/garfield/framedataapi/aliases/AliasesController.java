package com.garfield.framedataapi.aliases;

import com.garfield.framedataapi.aliases.dtos.AliasDto;
import com.garfield.framedataapi.aliases.dtos.CreateAliasDto;
import com.garfield.framedataapi.aliases.exceptions.AmbiguousCharacterNameException;
import com.garfield.framedataapi.config.authorization.Admin;
import com.garfield.framedataapi.config.authorization.Public;
import com.garfield.framedataapi.config.structure.ApiResponse;
import com.garfield.framedataapi.config.structure.ApiResponseEntity;
import com.garfield.framedataapi.config.structure.BaseApiController;
import com.garfield.framedataapi.gameCharacters.GameCharacter;
import com.garfield.framedataapi.gameCharacters.GameCharactersService;
import com.garfield.framedataapi.gameCharacters.exceptions.GameCharacterNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping(AliasesController.REQUEST_MAPPING)
public class AliasesController extends BaseApiController {

    public static final String REQUEST_MAPPING = "aliases";

    private final AliasesService aliasesService;
    private final GameCharactersService gameCharactersService;

    public AliasesController(
            AliasesService aliasesService,
            GameCharactersService gameCharactersService) {
        this.aliasesService = aliasesService;
        this.gameCharactersService = gameCharactersService;
    }

    @Override
    public String getRequestMapping() {
        return REQUEST_MAPPING;
    }

    @Public
    @GetMapping("character/{characterNameOrUuid}")
    public ResponseEntity<ApiResponse<Set<AliasDto>>> getAliasesForCharacter(
            @PathVariable String characterNameOrUuid) {
        Set<GameCharacter> gameCharacters;

        try {
            gameCharacters = Set.of(this.gameCharactersService.getGameCharactersById(UUID.fromString(characterNameOrUuid)));
        } catch (IllegalArgumentException e) {
            gameCharacters = this.gameCharactersService.getGameCharactersByName(characterNameOrUuid);
        }

        if (gameCharacters.size() > 1) {
            throw new AmbiguousCharacterNameException(characterNameOrUuid);
        }

        if (gameCharacters.stream().findFirst().isEmpty()) {
            throw new GameCharacterNotFoundException(characterNameOrUuid);
        }

        Set<Alias> aliases = this.aliasesService.getAliasesForGameCharacter(gameCharacters.stream().findFirst().get());

        return ApiResponseEntity.ok(
                aliases.stream().map(AliasDto::fromEntity).collect(Collectors.toSet())
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
