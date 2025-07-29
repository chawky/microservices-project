package com.nailic.MsAouth.controller;

import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/auth") // Fixed: should be value, not name
public class OauthController {

  private final RestTemplate restTemplate;

  public OauthController() {
    this.restTemplate = new RestTemplate();
  }

  @GetMapping
  public String oauth() {
    HttpHeaders headers = new HttpHeaders();
    HttpEntity<String> entity = new HttpEntity<>(headers);

    // Use the correct URL - mainms runs on port 8070 inside container
    String apiServiceUrl = "http://mainms:8070/test";

    ResponseEntity<String> response = restTemplate.exchange(
        apiServiceUrl,
        HttpMethod.GET,
        entity,
        String.class  // Changed from Map.class to String.class since mainms returns a String
    );

    String message = response.getBody();
    return "OAuth processed. API says: " + message;
  }
}
