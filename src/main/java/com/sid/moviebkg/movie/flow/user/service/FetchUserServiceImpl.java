package com.sid.moviebkg.movie.flow.user.service;

import com.sid.moviebkg.common.dto.user.UserDetailsDto;
import com.sid.moviebkg.common.logging.MBkgLogger;
import com.sid.moviebkg.common.logging.MBkgLoggerFactory;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static com.sid.moviebkg.movie.util.MovieCmnConstants.BASIC;
import static com.sid.moviebkg.movie.util.MovieCmnConstants.COLON;

@Service
@RequiredArgsConstructor
public class FetchUserServiceImpl implements FetchUserService {

    private MBkgLogger logger = MBkgLoggerFactory.getLogger(FetchUserServiceImpl.class);
    private final RestTemplate restTemplate;
    @Value("${user.service.fetch.user.endpoint}")
    private String endPoint;
    @Value("${user.auth.user.name:#{null}}")
    private String userName;
    @Value("${user.auth.user.password:#{null}}")
    private String password;

    @Override
    public UserDetailsDto fetchUser(String userId) {
        long start = System.currentTimeMillis();
        HttpEntity<String> entity = new HttpEntity<>(createHeaders());
        String url = endPoint + userId;
        ResponseEntity<UserDetailsDto> response = restTemplate.exchange(url, HttpMethod.GET, entity, UserDetailsDto.class);
        long end = System.currentTimeMillis();
        long timeTaken = end - start;
        logger.info("Time taken to fetch user details from MBkg-User-Svc is:{} ms", timeTaken);
        if (response.getStatusCode().value() == HttpStatus.OK.value() || response.getStatusCode().value() == HttpStatus.ACCEPTED.value()) {
            logger.info("Response from MBkg-User-Svc:{}", response);
        }
        return response.getBody();
    }

    private HttpHeaders createHeaders() {
        String auth = userName + COLON + password;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.add(HttpHeaders.AUTHORIZATION, BASIC + Base64.encodeBase64String(auth.getBytes()));
        return httpHeaders;
    }
}
