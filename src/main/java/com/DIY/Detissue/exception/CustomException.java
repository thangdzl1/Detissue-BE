package com.DIY.Detissue.exception;

public class CustomException extends RuntimeException{
    private String message;

    public CustomException(String message){
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
