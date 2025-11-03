package com.victorxavier.course_platform.course.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DataConflictException extends RuntimeException {

    public DataConflictException(String msg) {
        super(msg);
    }
}