package com.garfield.framedataapi.users;

import com.garfield.framedataapi.advice.responses.ApiResponse;
import com.garfield.framedataapi.advice.responses.ApiResponseEntity;
import com.garfield.framedataapi.core.BaseApiController;
import com.garfield.framedataapi.users.dtos.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(UserController.REQUEST_MAPPING)
@RequiredArgsConstructor
public class UserController extends BaseApiController {

    public static final String REQUEST_MAPPING = "users";

    private final UserService userService;

    @Override
    public String getRequestMapping() {
        return REQUEST_MAPPING;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDto>>> getUsers() {
        return ApiResponseEntity.ok(UserDto.fromEntityList(this.userService.getAllUsers()));
    }
}
