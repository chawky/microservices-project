package com.nailic.JwtAuth.DTOs;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(
    name = "ResetPasswordRequest",
    description = "Request object for resetting user password after OTP verification",
    example = """
        {
          "email": "john.doe@example.com",
          "newPassword": "NewSecurePassword123!"
        }
        """
)
public class ResetPasswordRequest {

  @Schema(
      description = "User's email address for password reset",
      example = "john.doe@example.com",
      required = true,
      format = "email",
      maxLength = 100
  )
  private String email;

  @Schema(
      description = "New password to set for the user account",
      example = "NewSecurePassword123!",
      required = true,
      minLength = 8,
      maxLength = 100,
      format = "password"
  )
  private String newPassword;
}
