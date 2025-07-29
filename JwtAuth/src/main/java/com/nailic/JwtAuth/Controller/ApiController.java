package com.nailic.JwtAuth.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Collections;
import org.modelmapper.Converters.Collection;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Main controller")
public class ApiController {

  @GetMapping("/api")
  @Operation(summary = "Main, API", description = "main business work")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "logged in to main app"),
      @ApiResponse(responseCode = "404", description = "User not found")
  })
  private ResponseEntity<?> api() {
    return ResponseEntity.ok(Collections.singletonMap("message", "api"));
  }
}
