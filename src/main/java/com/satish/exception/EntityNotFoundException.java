package com.satish.exception;

import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends RuntimeException{

    private HttpStatus status;

    public EntityNotFoundException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

}
