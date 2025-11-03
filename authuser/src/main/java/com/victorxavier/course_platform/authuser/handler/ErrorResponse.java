package com.victorxavier.course_platform.authuser.handler;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;


@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ErrorResponse(

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        Instant timestamp,
        Integer status,
        String error,
        String message,
        String path,
        List<FieldMessage> fieldErrors

) implements Serializable {
    public ErrorResponse(Instant timestamp, Integer status, String error, String message, String path) {
        this(timestamp, status, error, message, path, List.of());
    }

    public record FieldMessage(
            String fieldName,
            String message
    ) implements Serializable { }

}