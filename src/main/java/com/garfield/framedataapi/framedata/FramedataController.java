package com.garfield.framedataapi.framedata;

import com.garfield.framedataapi.config.authorization.Admin;
import com.garfield.framedataapi.config.authorization.Public;
import com.garfield.framedataapi.config.structure.ApiResponse;
import com.garfield.framedataapi.config.structure.ApiResponseEntity;
import com.garfield.framedataapi.config.structure.BaseApiController;
import com.garfield.framedataapi.framedata.dtos.CreateFramedataDto;
import com.garfield.framedataapi.framedata.dtos.FramedataDto;
import com.garfield.framedataapi.gameCharacters.GameCharacter;
import com.garfield.framedataapi.gameCharacters.GameCharactersService;
import com.garfield.framedataapi.gameCharacters.exceptions.AmbiguousGameCharacterNameException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping(FramedataController.REQUEST_MAPPING)
@RequiredArgsConstructor
public class FramedataController extends BaseApiController {

    public static final String REQUEST_MAPPING = "framedata";

    private final FramedataService framedataService;
    private final GameCharactersService gameCharactersService;

    @Override
    public String getRequestMapping() {
        return REQUEST_MAPPING;
    }

    @Public
    @GetMapping("character/{characterNameOrUuid}")
    public ResponseEntity<ApiResponse<Set<FramedataDto>>> getAllFramedataForCharacter(
            @PathVariable("characterNameOrUuid") String characterNameOrUuid) {
        Set<GameCharacter> gameCharacter;

        try {
            gameCharacter = Set.of(this.gameCharactersService.getGameCharacterById(UUID.fromString(characterNameOrUuid)));
        } catch (IllegalArgumentException e) {
            gameCharacter = this.gameCharactersService.getGameCharactersByName(characterNameOrUuid);
        }

        if (gameCharacter.size() > 1) {
            throw new AmbiguousGameCharacterNameException(characterNameOrUuid);
        }

        Set<Framedata> framedata = this.framedataService.getFramedataForCharacter(gameCharacter.iterator().next());

        return ApiResponseEntity.ok(framedata
                .stream()
                .map(fd ->
                        FramedataDto.fromEntityAndAttributesMap(
                                fd,
                                this.framedataService.convertFramedataAttributeStringToMap(fd.getAttributes())
                        )
                ).collect(Collectors.toSet())
        );
    }

    @Admin
    @PostMapping
    public ResponseEntity<ApiResponse<FramedataDto>> createFramedata(@Valid @RequestBody CreateFramedataDto dto) {
        GameCharacter gameCharacter = this.gameCharactersService.getGameCharacterById(dto.characterId());

        Framedata framedata = new Framedata(
                gameCharacter,
                this.framedataService.convertFramedataAttributeMapToString(dto.attributes())
        );

        this.framedataService.createFramedata(framedata);

        return ApiResponseEntity.created(createControllerUri(framedata.getId()));
    }

}
