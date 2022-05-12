package com.utopiapp.demo.dto;

public class Message {
    private String httpStatus;
    private String Message;

    public Message(String Message) {
        this.Message = Message;
    }

    public String getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(String httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        this.Message = message;
    }
}
