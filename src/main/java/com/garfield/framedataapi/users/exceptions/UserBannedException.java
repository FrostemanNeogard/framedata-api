package com.garfield.framedataapi.users.exceptions;

public class UserBannedException extends RuntimeException {

    public UserBannedException() {
        super("You are currently banned.");
    }

}
