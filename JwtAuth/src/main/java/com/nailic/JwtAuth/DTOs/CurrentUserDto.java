package com.nailic.JwtAuth.DTOs;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(
    name = "CurrentUserDto",
    description = "User data transfer object for registration and user management",
    example = """
        {
          "username": "john_doe",
          "email": "john.doe@example.com",
          "roles": ["USER"],
          "password": "SecurePassword123!"
        }
        """
)
public class CurrentUserDto {

  @Schema(
      description = "User's unique username",
      example = "john_doe",
      required = true,
      minLength = 3,
      maxLength = 50,
      pattern = "^[a-zA-Z0-9_]+$"
  )
  private String username;

  @Schema(
      description = "User's email address",
      example = "john.doe@example.com",
      required = true,
      format = "email",
      maxLength = 100
  )
  private String email;

  @Schema(
      description = "Set of user roles and permissions",
      example = "[\"USER\", \"ADMIN\"]",
      allowableValues = {"USER", "ADMIN", "MODERATOR"}
  )
  private Set<String> roles;

  @Schema(
      description = "User's password (will be encrypted)",
      example = "SecurePassword123!",
      required = true,
      minLength = 8,
      maxLength = 100,
      format = "password"
  )
  private String password;
}
