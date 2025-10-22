package com.victorxavier.course_platform.authuser.clients;

import com.victorxavier.course_platform.authuser.dtos.CourseDTO;
import com.victorxavier.course_platform.authuser.dtos.ResponsePageDTO;
import com.victorxavier.course_platform.authuser.services.UtilsService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class CourseClient {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    UtilsService utilsService;

    @Value("${course_platform.api.url.course}")
    String REQUEST_URl_COURSE;

    // @Retry(name = "retryInstance", fallbackMethod = "retryFallback")
    @CircuitBreaker(name = "circuitBreakerInstance", fallbackMethod = "circuitbreakerfallback")
    public Page<CourseDTO> getAllCoursesByUser(UUID userId, Pageable pageable) {
        List<CourseDTO> searchResult = null;
        Page<CourseDTO> page = Page.empty(pageable);
        String url = REQUEST_URl_COURSE + utilsService.createUrl(userId, pageable);
        log.debug("Request URL: {} ", url);
        log.info("Request URL: {} ", url);

        try {
            ParameterizedTypeReference<ResponsePageDTO<CourseDTO>> responseType =
                    new ParameterizedTypeReference<ResponsePageDTO<CourseDTO>>() {};
            ResponseEntity<ResponsePageDTO<CourseDTO>> result = restTemplate.exchange(url, HttpMethod.GET, null, responseType);

            if (result.getBody() != null) {
                ResponsePageDTO<CourseDTO> responseBody = result.getBody();
                searchResult = responseBody.getContent();
                log.debug("Response Number of Elements: {} ", searchResult != null ? searchResult.size() : 0);

                if (searchResult != null) {
                    page = new PageImpl<>(searchResult, pageable, responseBody.getTotalElements());
                } else {
                    page = new PageImpl<>(Collections.emptyList(), pageable, responseBody.getTotalElements());
                }
            } else {
                log.warn("Response body is null for URL: {}", url);
                page = createEmptyPage(pageable);
            }
        } catch (HttpStatusCodeException e){
            log.error("Error Request /courses userId {}: Status Code: {}, Body: {}", userId, e.getStatusCode(), e.getResponseBodyAsString(), e);
            page = createEmptyPage(pageable);
        } catch (Exception e) {
            log.error("Unexpected error during request /courses userId {}: {}", userId, e.getMessage(), e);
            page = createEmptyPage(pageable);
        }
        log.info("Ending request /courses userId {} ", userId);
        return page;
    }

    public Page<CourseDTO> circuitbreakerfallback(UUID userId, Pageable pageable, Exception ex) {
        log.error("Circuit Breaker fallback triggered for getAllCoursesByUser. UserId: {}, Cause: {}",
                userId, ex.getMessage(), ex);
        return createEmptyPage(pageable);
    }

    public Page<CourseDTO> retryFallback(UUID userId, Pageable pageable, Exception ex) {
        log.error("Fallback triggered for getAllCoursesByUser. UserId: {}, Cause: {}",
                userId, ex.getMessage(), ex);
        return createEmptyPage(pageable);
    }

    private Page<CourseDTO> createEmptyPage(Pageable pageable) {
        return new PageImpl<>(Collections.emptyList(), pageable, 0);
    }
}