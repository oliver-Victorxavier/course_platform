package com.victorxavier.course_platform.course.clients;

import com.victorxavier.course_platform.course.dto.CourseUserDTO;
import com.victorxavier.course_platform.course.dto.ResponsePageDTO;
import com.victorxavier.course_platform.course.dto.UserDTO;
import com.victorxavier.course_platform.course.services.UtilsService;
import lombok.extern.log4j.Log4j2;
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

import java.util.List;
import java.util.UUID;

@Log4j2
@Component
public class AuthUserClient {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    UtilsService utilsService;

    @Value("${course_platform.api.url.authuser}")
    String REQUEST_URI_AUTHUSER;

    public Page<UserDTO> getAllUsersByCourse(UUID courseId, Pageable pageable) {
        List<UserDTO> searchResult = null;
        String url = REQUEST_URI_AUTHUSER + utilsService.createUrlGetAllUserByCourse(courseId, pageable);
        log.debug("Request URL: {} ", url);
        log.info("Request URL: {} ", url);
        try {
            ParameterizedTypeReference<ResponsePageDTO<UserDTO>> responseType =
                    new ParameterizedTypeReference<ResponsePageDTO<UserDTO>>() {};

            ResponseEntity<ResponsePageDTO<UserDTO>> result = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
            searchResult = result.getBody().getContent();
            log.debug("Response Number of Elements: {} ", searchResult.size());
        } catch (HttpStatusCodeException e){
            log.error("Error Request /courses {} ", e);
        }
        log.info("Ending request /users courseId {} ", courseId);
        return new PageImpl<>(searchResult);
    }

    public ResponseEntity<UserDTO> getOneUserById(UUID UserId) {
        String url = REQUEST_URI_AUTHUSER + "/users/" + UserId;
        return restTemplate.exchange(url, HttpMethod.GET, null, UserDTO.class);

    }

    public void postSubscriptionUserInCourse(UUID courseId, UUID userId) {
        String url = REQUEST_URI_AUTHUSER + "/users/" + userId + "/courses/subscription";
        var courseUserDTO = new CourseUserDTO(courseId, userId);

        restTemplate.postForObject(url, courseUserDTO, String.class);
    }

}
