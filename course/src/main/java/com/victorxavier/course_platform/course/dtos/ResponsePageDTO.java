package com.victorxavier.course_platform.course.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Getter
public class ResponsePageDTO<T> extends PageImpl<T> {

    private final boolean last;
    private final int totalPages;
    private final boolean first;
    private final boolean empty;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public ResponsePageDTO(
            @JsonProperty("content") List<T> content,
            @JsonProperty("number") int number,
            @JsonProperty("size") int size,
            @JsonProperty("totalElements") Long totalElements,
            @JsonProperty("pageable") JsonNode pageable,
            @JsonProperty("last") boolean last,
            @JsonProperty("totalPages") int totalPages,
            @JsonProperty("sort") JsonNode sort,
            @JsonProperty("first") boolean first,
            @JsonProperty("empty") boolean empty) {
        super(content, PageRequest.of(number, size), totalElements);

        this.last = last;
        this.totalPages = totalPages;
        this.first = first;
        this.empty = empty;
    }

    public ResponsePageDTO(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
        this.totalPages = pageable.isPaged() ? (int) Math.ceil((double) total / pageable.getPageSize()) : 1;
        this.last = pageable.isPaged() ? (pageable.getPageNumber() + 1) * pageable.getPageSize() >= total : true;
        this.first = pageable.isPaged() ? pageable.getPageNumber() == 0 : true;
        this.empty = content.isEmpty();
    }

    public ResponsePageDTO(List<T> content) {
        super(content);

        this.totalPages = 1;
        this.last = true;
        this.first = true;
        this.empty = content.isEmpty();
    }

}