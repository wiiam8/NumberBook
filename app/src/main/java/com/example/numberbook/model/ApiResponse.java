package com.example.numberbook.model;

public class ApiResponse {

    private boolean success;
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message == null ? "" : message;
    }
}