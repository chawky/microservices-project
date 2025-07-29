package com.nailic.JwtAuth.DTOs;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(
    name = "AuthResponse",
    description = "Authentication response containing user details and tokens",
    example = """
        {
          "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2huLmRvZUBleGFtcGxlLmNvbSIsImlhdCI6MTY5ODc2ODAwMCwiZXhwIjoxNjk4ODU0NDAwfQ.abc123xyz",
          "username": "john_doe",
          "email": "john.doe@example.com",
          "refreshToken": "rt_1234567890abcdef",
          "userId": 12345,
          "roles": ["ADMIN", "DEVELOPER", "TECH-LEAD"]
        }
        """
)
public class AuthResponse {

  @Schema(
      description = "JWT access token for authentication",
      example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9d.eyJzdWIiOigJqb2huLmRvZUBleGFtcGxlLmNvbSIsImlhdCI6MTY5ODc2ODAwMCwiZXhwIjoxNjk4ODU0NDAwfQ.abc123xyz",
      required = true
  )
  private String token;

  @Schema(
      description = "User's unique username",
      example = "john_doe",
      required = true,
      minLength = 3,
      maxLength = 50
  )
  private String username;

  @Schema(
      description = "User's email address",
      example = "john.doe@example.com",
      required = true,
      format = "email"
  )
  private String email;

  @Schema(
      description = "Refresh token for obtaining new access tokens",
      example = "rt_1234567890abcdef",
      required = true
  )
  private String refreshToken;

  @Schema(
      description = "Unique identifier for the user",
      example = "12345",
      required = true,
      minimum = "1"
  )
  private long userId;

  @Schema(
      description = "Set of user roles and permissions",
      example = "[\"DEVELOPER\", \"ADMIN\", \"TECH-LEAD\"]",
      required = true)
  private Set<String> roles;
}
