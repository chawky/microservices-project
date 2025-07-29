package com.nailic.JwtAuth.Controller;

import com.nailic.JwtAuth.DTOs.AuthResponse;
import com.nailic.JwtAuth.DTOs.CurrentUserDto;
import com.nailic.JwtAuth.DTOs.ResetPasswordRequest;
import com.nailic.JwtAuth.DTOs.loginRequestDto;
import com.nailic.JwtAuth.entities.CurrentUser;
import com.nailic.JwtAuth.exceptions.NotFoundException;
import com.nailic.JwtAuth.exceptions.OTPExpiredException;
import com.nailic.JwtAuth.services.CurrentUserService;
import com.nailic.JwtAuth.services.EmailService;
import com.nailic.JwtAuth.services.OtpService;
import com.nailic.JwtAuth.services.RefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import java.util.Collections;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "authentication controller")
public class AuthController {

  @Autowired
  private CurrentUserService currentUserService;
  @Autowired
  private RefreshTokenService refreshTokenService;
  @Autowired
  private AuthenticationManager authenticationManager;
  @Autowired
  private EmailService emailService;

  @Autowired
  private OtpService otpService;

  @PostMapping("/signup")
  @Operation(summary = "Create a new user account", description = "Register a new user with email and password")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "User account created successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid request data or user already exists"),
      @ApiResponse(responseCode = "404", description = "Resource not found"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  public ResponseEntity<?> signup(@RequestBody CurrentUserDto userDto) {
    ModelMapper userMapper = new ModelMapper();
    try {
      CurrentUser currentUser = currentUserService.registerUser(userDto);

      // map the saved user back to DTO for response
      CurrentUserDto responseDto = userMapper.map(currentUser, CurrentUserDto.class);
      return ResponseEntity.ok(responseDto);

    } catch (RuntimeException ex) {
      // This will catch "User already registered" and "Error saving user" exceptions thrown in registerUser()
      return ResponseEntity
          .status(
              HttpStatus.BAD_REQUEST)  // 400 is appropriate for client errors like username conflict
          .body(ex.getMessage());
    } catch (NotFoundException ex) {
      // If you want to catch NotFoundException separately
      return ResponseEntity
          .status(HttpStatus.NOT_FOUND)
          .body(ex.getMessage());
    } catch (Exception ex) {
      // catch any other unexpected exceptions
      return ResponseEntity
          .status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("An unexpected error occurred: " + ex.getMessage());
    }
  }

  @PostMapping("/login")
  @Operation(summary = "User login", description = "Authenticate user and return JWT token")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Login successful, returns JWT token"),
      @ApiResponse(responseCode = "404", description = "User not found")
  })
  public ResponseEntity<?> login(@RequestBody loginRequestDto userDto) throws NotFoundException {
    try {

      ModelMapper userMapper = new ModelMapper();
      CurrentUser user = userMapper.map(userDto, CurrentUser.class);
      AuthResponse jwt = currentUserService.login(user, authenticationManager);
      if (jwt == null) {
        throw new NotFoundException("User not found");
      }

      return ResponseEntity.ok(jwt);
    } catch (NotFoundException ex) {
      // If you want to catch NotFoundException separately
      return ResponseEntity
          .status(HttpStatus.NOT_FOUND)
          .body(ex.getMessage());
    }
  }

  @PostMapping("/refreshToken")
  @Operation(summary = "Refresh JWT token", description = "Generate new access token using refresh token")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Token refreshed successfully"),
      @ApiResponse(responseCode = "401", description = "Invalid or expired refresh token")
  })
  public ResponseEntity<?> refreshToken(@RequestBody String refreshToken) {
    return ResponseEntity.ok(refreshTokenService.refreshToken(refreshToken));
  }

  @PostMapping("/forgot-password")
  @Operation(summary = "Request password reset", description = "Send OTP to user's email for password reset")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OTP sent successfully"),
      @ApiResponse(responseCode = "404", description = "Email not found or OTP expired"),
      @ApiResponse(responseCode = "500", description = "Failed to send email")
  })
  public ResponseEntity<?> forgotPassword(@RequestBody String email) {
    String otp = "";
    try {

      otp = otpService.sendForgotPasswordEmail(email);
      return ResponseEntity.ok(Collections.singletonMap("message", otp));
    } catch (OTPExpiredException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(Collections.singletonMap("message", "OTP expired " + otp));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(Collections.singletonMap("message", e.getMessage()));
    }
  }

  @PostMapping("/verify-otp")
  @Operation(summary = "Verify OTP", description = "Verify the OTP sent to user's email")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OTP verified successfully"),
      @ApiResponse(responseCode = "404", description = "OTP expired or verification failed"),
      @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  public ResponseEntity<?> verifyOtp(@RequestParam String email, @RequestParam String otp) {
    try {
      boolean verification = otpService.verifyOtp(email, otp);
      return ResponseEntity.ok(Collections.singletonMap("message", "otp verified " + verification));
    } catch (OTPExpiredException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(Collections.singletonMap("message", "OTP verification failed"));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(Collections.singletonMap("message", e.getMessage()));
    }
  }

  @PostMapping("/reset-pw")
  @Operation(summary = "Reset password", description = "Reset user password after OTP verification")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Password reset successful"),
      @ApiResponse(responseCode = "500", description = "Password reset failed")
  })
  public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
    try {
      otpService.resetPassword(request);
      return ResponseEntity.ok(Collections.singletonMap("message", "password reset successful"));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }

  @GetMapping("/verify")
  @Operation(summary = "Verify user account", description = "Verify user account using email token")
  @ApiResponses({
      @ApiResponse(responseCode = "302", description = "Redirect to frontend with verification status"),
      @ApiResponse(responseCode = "400", description = "Invalid verification token")
  })
  public ResponseEntity<Void> verifyAccount(@RequestParam String token) {
    String redirectUrl;

    if (emailService.verifyUser(token)) {
      // Add param to indicate success
      redirectUrl = "http://localhost:4200?verified=true";
    } else {
      // Add param to indicate failure
      redirectUrl = "http://localhost:4200?verified=false";
    }
    return ResponseEntity.status(HttpStatus.FOUND)
        .location(URI.create(redirectUrl))
        .build();
  }


  @PostMapping("/resend-verification")
  @Operation(summary = "Resend verification email", description = "Resend account verification email to user")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Verification email resent successfully"),
      @ApiResponse(responseCode = "404", description = "User not found")
  })
  public ResponseEntity<?> resendVerification(@RequestBody String email) throws NotFoundException {
    emailService.resendVerificationEmail(email);
    return ResponseEntity.ok(Collections.singletonMap("message", "Account verified"));
  }

  @PostMapping("/send-verification")
  @Operation(summary = "Send verification email", description = "Send initial account verification email")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Verification email sent successfully"),
      @ApiResponse(responseCode = "404", description = "User not found")
  })
  public ResponseEntity<?> sendVerification(@RequestBody String email) throws NotFoundException {
    emailService.sendVerificationEmail(email);
    return ResponseEntity.ok(Collections.singletonMap("message", "Account verified"));
  }

}
