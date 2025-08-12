package com.garfield.framedataapi.exceptionhandler;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class ApiResponse<T> {

    private final int status;
    private final String error;
    private final T data;

    private ApiResponse(HttpStatus status, String error, T data) {
        this.status = status.value();
        this.error = error;
        this.data = data;
    }

    private ApiResponse(int status, String error, T data) {
        this.status = status;
        this.error = error;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(HttpStatus status, T data) {
        return new ApiResponse<>(status.value(), null, data);
    }

    public static <T> ApiResponse<T> error(HttpStatus status, String errorMessage) {
        return new ApiResponse<>(status.value(), errorMessage, null);
    }

    public static <T> ApiResponse<T> created() {
        return new ApiResponse<>(HttpStatus.CREATED, null, null);
    }

}
