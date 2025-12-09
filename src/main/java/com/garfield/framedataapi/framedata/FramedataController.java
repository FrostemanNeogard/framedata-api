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
import com.garfield.framedataapi.gameCharacters.GameCharacterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@Controller
@RequestMapping(FramedataController.REQUEST_MAPPING)
@RequiredArgsConstructor
public class FramedataController extends BaseApiController {

    public static final String REQUEST_MAPPING = "framedata";

    private final FramedataService framedataService;
    private final GameCharacterService gameCharacterService;

    @Override
    public String getRequestMapping() {
        return REQUEST_MAPPING;
    }

    @Admin
    @PostMapping("character/{characterNameOrUuid}")
    public ResponseEntity<ApiResponse<FramedataResponseDto>> createFramedata(
            @PathVariable String characterNameOrUuid,
            @Valid @RequestBody CreateFramedataDto dto) {
        GameCharacter gameCharacter = this.gameCharacterService.getGameCharacterByIdentifier(characterNameOrUuid);

        Framedata framedata = new Framedata(
                gameCharacter,
                dto.identity(),
                dto.data());

        this.framedataService.createFramedata(framedata);

        return ApiResponseEntity.created(createControllerUri(framedata.getId()));
    }

    @Admin
    @DeleteMapping("identifier/{framedataId}")
    public ResponseEntity<ApiResponse<Void>> deleteFramedata(@PathVariable UUID framedataId) {
        Framedata framedata = this.framedataService.getFramedataById(framedataId);

        this.framedataService.deleteFramedata(framedata);

        return ApiResponseEntity.deleted();
    }

    @Public
    @GetMapping("identifier/{framedataId}")
    public ResponseEntity<ApiResponse<FramedataResponseDto>> getFramedataById(@PathVariable UUID framedataId) {
        Framedata framedata = this.framedataService.getFramedataById(framedataId);

        return ApiResponseEntity.ok(new FramedataResponseDto(framedata));
    }

    @Public
    @GetMapping("character/{characterNameOrUuid}")
    public ResponseEntity<ApiResponse<FramedataResponseDto>> getAllFramedataForCharacter(
            @PathVariable String characterNameOrUuid) {
        GameCharacter gameCharacter = this.gameCharacterService.getGameCharacterByIdentifier(characterNameOrUuid);
        Set<Framedata> framedata = gameCharacter.getFramedata();

        return ApiResponseEntity.ok(new FramedataResponseDto(framedata));
    }

    @Public
    @GetMapping("character/{characterNameOrUuid}/identifier/{input}")
    public ResponseEntity<ApiResponse<FramedataResponseDto>> getFramedataByInput(
            @PathVariable String characterNameOrUuid,
            @PathVariable String input) {
        GameCharacter gameCharacter = this.gameCharacterService.getGameCharacterByIdentifier(characterNameOrUuid);

        try {
            Framedata matchedEntry = this.framedataService.getFramedataByInput(gameCharacter, input);
            return ApiResponseEntity.ok(new FramedataResponseDto(matchedEntry));
        } catch(FramedataNotFoundException e) {
            Set<Framedata> matchedEntries = this.framedataService.getMostSimilarFramedataEntries(
                    gameCharacter,
                    input
            );

            return ApiResponseEntity.error(HttpStatus.NOT_FOUND, null, new FramedataResponseDto(matchedEntries));
        }

    }

}
