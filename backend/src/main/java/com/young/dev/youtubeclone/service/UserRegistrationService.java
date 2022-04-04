package com.young.dev.youtubeclone.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.young.dev.youtubeclone.dto.UserInfoDto;
import com.young.dev.youtubeclone.model.User;
import com.young.dev.youtubeclone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {

    @Value("${auth0.userInfoEndpoint}")
    private String userInfoEndpoint;

    private final UserRepository userRepository;

    public String registerUser(String tokenValue) {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(userInfoEndpoint))
                .setHeader("Authorization", String.format("Bearer %s", tokenValue))
                .build();

        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();

        try {
            HttpResponse<String> responseStr =
                    httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String body = responseStr.body();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            UserInfoDto userInfoDto = objectMapper.readValue(body, UserInfoDto.class);

            Optional<User> userBySub = userRepository.findBySub(userInfoDto.getSub());
            if (userBySub.isPresent()) {
                return userBySub.get().getId();
            } else {
                User user = new User();
                user.setFirstName(userInfoDto.getGivenName());
                user.setLastName(userInfoDto.getFamilyName());
                user.setFullName(userInfoDto.getName());
                user.setEmailAddress(userInfoDto.getEmail());
                user.setSub(userInfoDto.getSub());

                return userRepository.save(user).getId();
            }

        } catch (Exception e) {
            throw new RuntimeException("Exception occurred while registering a user", e);
        }
    }
}
