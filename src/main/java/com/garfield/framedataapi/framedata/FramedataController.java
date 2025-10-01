package com.garfield.framedataapi.framedata;

import com.garfield.framedataapi.config.structure.BaseApiController;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(FramedataController.REQUEST_MAPPING)
@RequiredArgsConstructor
public class FramedataController extends BaseApiController {

    public static final String REQUEST_MAPPING = "framedata";

    private final FramedataService framedataService;

    @Override
    public String getRequestMapping() {
        return REQUEST_MAPPING;
    }

}
