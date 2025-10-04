package com.garfield.framedataapi.gameMetadata;

import com.garfield.framedataapi.config.structure.BaseApiController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(GameMetadatasController.REQUEST_MAPPING)
public class GameMetadatasController extends BaseApiController {

    public static final String REQUEST_MAPPING = "/gamemetadatas";

    private final GameMetadatasService gameMetadatasService;

    public GameMetadatasController(GameMetadatasService gameMetadatasService) {
        this.gameMetadatasService = gameMetadatasService;
    }

    @Override
    public String getRequestMapping() {
        return REQUEST_MAPPING;
    }

}
