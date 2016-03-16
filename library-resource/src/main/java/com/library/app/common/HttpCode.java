package com.library.app.common;

public enum HttpCode {

    CREATED(201),
    VALIDATION_ERROR(422);

    private int code;

    private HttpCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
