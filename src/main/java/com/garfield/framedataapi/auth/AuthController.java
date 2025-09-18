package com.garfield.framedataapi.auth;

import com.garfield.framedataapi.base.BaseApiController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(AuthController.REQUEST_MAPPING)
public class AuthController extends BaseApiController {

    public static final String REQUEST_MAPPING = "auth";

    public AuthController() {
    }

    @Override
    public String getRequestMapping() {
        return REQUEST_MAPPING;
    }

    @GetMapping("public")
    private String getPublic() {
        return "success public";
    }

    @GetMapping("private")
    private String getPrivate() {
        return "success private";
    }

}
