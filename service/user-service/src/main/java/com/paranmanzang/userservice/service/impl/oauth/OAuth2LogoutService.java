package com.paranmanzang.userservice.service.impl.oauth;/*
package com.paranmanzang.userservice.service.user.impl.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OAuth2LogoutService {
    @Value("${oauth2.logout.url}") // OAuth2 제공자의 로그아웃 URL을 application.properties 또는 application.yml에서 주입받습니다.
    private String logoutUrl;

    private RestTemplate restTemplate;

    public void logout(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(logoutUrl, HttpMethod.POST, entity, String.class);
    }
}
*/
