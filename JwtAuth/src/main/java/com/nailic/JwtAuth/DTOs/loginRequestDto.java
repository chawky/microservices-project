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

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(
    name = "LoginRequest",
    description = "Login request containing user credentials",
    example = """
        {
          "email": "john.doe@example.com",
          "password": "SecurePassword123!"
        }
        """
)
public class loginRequestDto {

  @Schema(
      description = "User's email address for authentication",
      example = "john.doe@example.com",
      required = true,
      format = "email",
      maxLength = 100
  )
  private String email;

  @Schema(
      description = "User's password for authentication",
      example = "SecurePassword123!",
      required = true,
      minLength = 8,
      maxLength = 100,
      format = "password"
  )
  private String password;
}
