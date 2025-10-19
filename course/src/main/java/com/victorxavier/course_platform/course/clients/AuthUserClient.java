package com.victorxavier.course_platform.course.clients;

import com.victorxavier.course_platform.course.dto.CourseUserDTO;
import com.victorxavier.course_platform.course.dto.ResponsePageDTO;
import com.victorxavier.course_platform.course.dto.UserDTO;
import com.victorxavier.course_platform.course.services.UtilsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class AuthUserClient {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    UtilsService utilsService;

    @Value("${course_platform.api.url.authuser}")
    String REQUEST_URL_AUTHUSER;

    public Page<UserDTO> getAllUsersByCourse(UUID courseId, Pageable pageable) {
        List<UserDTO> searchResult = null;

        Page<UserDTO> page = Page.empty(pageable);
        String url = REQUEST_URL_AUTHUSER + utilsService.createUrlGetAllUserByCourse(courseId, pageable);
        log.debug("Request URL: {} ", url);
        log.info("Request URL: {} ", url);
        try {
            ParameterizedTypeReference<ResponsePageDTO<UserDTO>> responseType =
                    new ParameterizedTypeReference<ResponsePageDTO<UserDTO>>() {};

            ResponseEntity<ResponsePageDTO<UserDTO>> result = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
            if (result.getBody() != null) {
                searchResult = result.getBody().getContent();
                log.debug("Response Number of Elements: {} ", searchResult != null ? searchResult.size() : 0);
                ResponsePageDTO<UserDTO> responseBody = result.getBody();
                if (searchResult != null) {
                    page = new PageImpl<>(searchResult, pageable, responseBody.getTotalElements());
                } else {
                    page = new PageImpl<>(Collections.emptyList(), pageable, responseBody.getTotalElements());
                }
            } else {
                log.warn("Response body is null for URL: {}", url);
                page = new PageImpl<>(Collections.emptyList(), pageable, 0);
            }

        } catch (HttpStatusCodeException e){
            log.error("Error Request /users for courseId {}: Status Code: {}, Body: {}", courseId, e.getStatusCode(), e.getResponseBodyAsString(), e);
            page = new PageImpl<>(Collections.emptyList(), pageable, 0);
        } catch (Exception e) {
            log.error("Unexpected error during request /users for courseId {}: {}", courseId, e.getMessage(), e);
            page = new PageImpl<>(Collections.emptyList(), pageable, 0);
        }
        log.info("Ending request /users courseId {} ", courseId);
        return page;
    }

    public ResponseEntity<UserDTO> getOneUserById(UUID userId) {
        String url = REQUEST_URL_AUTHUSER + "/users/" + userId;
        try {
            return restTemplate.exchange(url, HttpMethod.GET, null, UserDTO.class);
        } catch (HttpStatusCodeException e) {
            log.error("Error requesting user by ID {}: Status Code: {}, Body: {}", userId, e.getStatusCode(), e.getResponseBodyAsString());
            return ResponseEntity.status(e.getStatusCode()).build();
        } catch (Exception e) {
            log.error("Unexpected error requesting user by ID {}: {}", userId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public void postSubscriptionUserInCourse(UUID courseId, UUID userId) {
        String url = REQUEST_URL_AUTHUSER + "/users/" + userId + "/courses/subscription";
        var courseUserDTO = new CourseUserDTO(courseId, userId);

        restTemplate.postForObject(url, courseUserDTO, String.class);
    }

    public void deleteCourseInAuthUser(UUID courseId) {
        String url = REQUEST_URL_AUTHUSER + "/users/courses/" + courseId;
        restTemplate.exchange(url, HttpMethod.DELETE, null, String.class);
    }
}
