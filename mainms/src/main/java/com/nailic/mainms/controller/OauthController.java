package com.nailic.mainms.controller;

import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/test") // Fixed: should be value, not name
public class OauthController {

  private final RestTemplate restTemplate;

  public OauthController() {
    this.restTemplate = new RestTemplate();
  }

  @GetMapping
  public String oauth() {
    return "OAuth processed. API says: " + "message";
  }
}
