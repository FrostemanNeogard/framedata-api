package com.garfield.framedataapi.users;

import com.garfield.framedataapi.base.BaseApiController;
import com.garfield.framedataapi.exceptionhandler.ApiResponse;
import com.garfield.framedataapi.exceptionhandler.ApiResponseEntity;
import com.garfield.framedataapi.users.dtos.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(UsersController.REQUEST_MAPPING)
public class UsersController extends BaseApiController {

    public static final String REQUEST_MAPPING = "users";

    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @Override
    public String getRequestMapping() {
        return REQUEST_MAPPING;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDto>>> getUsers() {
        return ApiResponseEntity.ok(UserDto.fromEntityList(this.usersService.getAllUsers()));
    }
}
