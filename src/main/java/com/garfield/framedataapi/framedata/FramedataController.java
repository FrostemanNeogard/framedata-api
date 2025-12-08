package com.garfield.framedataapi.framedata;

import com.garfield.framedataapi.advice.authorization.Admin;
import com.garfield.framedataapi.advice.authorization.Public;
import com.garfield.framedataapi.advice.responses.ApiResponse;
import com.garfield.framedataapi.advice.responses.ApiResponseEntity;
import com.garfield.framedataapi.core.BaseApiController;
import com.garfield.framedataapi.framedata.dtos.CreateFramedataDto;
import com.garfield.framedataapi.framedata.dtos.FramedataResponseDto;
import com.garfield.framedataapi.framedata.exceptions.FramedataNotFoundException;
import com.garfield.framedataapi.gameCharacters.GameCharacter;
import com.garfield.framedataapi.gameCharacters.GameCharactersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @Admin
    @PostMapping("character/{characterNameOrUuid}")
    public ResponseEntity<ApiResponse<FramedataResponseDto>> createFramedata(
            @PathVariable("characterNameOrUuid") String characterId,
            @Valid @RequestBody CreateFramedataDto dto) {
        GameCharacter gameCharacter = this.gameCharactersService.getGameCharacterByIdentifier(characterId);

        Framedata framedata = new Framedata(
                gameCharacter,
                dto.identity(),
                dto.data());

        this.framedataService.createFramedata(framedata);

        return ApiResponseEntity.created(createControllerUri(framedata.getId()));
    }

    @Admin
    @DeleteMapping("identifier/{framedataId}")
    public ResponseEntity<ApiResponse<Void>> deleteFramedata(@PathVariable("framedataId") UUID framedataId) {
        Framedata framedata = this.framedataService.getFramedataById(framedataId);

        this.framedataService.deleteFramedata(framedata);

        return ApiResponseEntity.deleted();
    }

    @Public
    @GetMapping("identifier/{framedataId}")
    public ResponseEntity<ApiResponse<FramedataResponseDto>> getFramedataById(
            @PathVariable("framedataId")
            UUID framedataId) {
        Framedata framedata = this.framedataService.getFramedataById(framedataId);

        return ApiResponseEntity.ok(new FramedataResponseDto(framedata));
    }

    @Public
    @GetMapping("character/{characterNameOrUuid}")
    public ResponseEntity<ApiResponse<FramedataResponseDto>> getAllFramedataForCharacter(
            @PathVariable("characterNameOrUuid") String characterNameOrUuid) {
        GameCharacter gameCharacter = this.gameCharactersService.getGameCharacterByIdentifier(characterNameOrUuid);
        Set<Framedata> framedata = gameCharacter.getFramedata();

        return ApiResponseEntity.ok(new FramedataResponseDto(framedata));
    }

    @Public
    @GetMapping("character/{characterNameOrUuid}/identifier/{input}")
    public ResponseEntity<ApiResponse<List<FramedataResponseDto>>> getFramedataByInput(
            @PathVariable("characterNameOrUuid") String characterNameOrUuid,
            @PathVariable("input") String input) {
        GameCharacter gameCharacter = this.gameCharactersService.getGameCharacterByIdentifier(characterNameOrUuid);
        Set<Framedata> characterFramedata = gameCharacter.getFramedata().stream()
                .filter(fd -> fd
                        .getIdentity()
                        .getIdentifiers()
                        .contains(input)
                ).collect(Collectors.toSet());

        if (characterFramedata.isEmpty()) {
            throw new FramedataNotFoundException(gameCharacter, input);
        }

        List<FramedataResponseDto> framedataResponseDtos = characterFramedata.stream().map(FramedataResponseDto::new).toList();

        return ApiResponseEntity.ok(framedataResponseDtos);
    }

}
