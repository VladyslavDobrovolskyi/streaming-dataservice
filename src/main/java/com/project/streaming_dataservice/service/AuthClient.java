//package com.project.streaming_dataservice.service;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.http.*;
//
//@Service
//public class AuthClient {
//
//    private final RestTemplate restTemplate;
//
//    @Value("${auth.service.url}")
//    private String authServiceUrl; // URL сервиса аутентификации (можно задать в application.properties)
//
//    public AuthClient(RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//    }
//
//    public boolean validateToken(String token) {
//        String url = authServiceUrl + "/api/auth/validate";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", token);
//
//        HttpEntity<String> request = new HttpEntity<>(headers);
//
//        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
//
//        return response.getStatusCode() != HttpStatus.OK;
//    }
//}
