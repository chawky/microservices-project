package com.nailic.JwtAuth;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
//@EnableConfigurationProperties(value = {classthat has the properties . class})
@OpenAPIDefinition(
    info = @Info(
        title = "AI Application API",
        description = "Comprehensive REST API for AI-powered application services including user authentication, data processing, and intelligent features",
        version = "1.0.0",
        contact = @Contact(
            name = "Development Team",
            email = "api-support@yourcompany.com",
            url = "https://yourcompany.com/support"
        ),
        license = @License(
            name = "MIT License",
            url = "https://opensource.org/licenses/MIT"
        ),
        termsOfService = "https://yourcompany.com/terms"
    ),
    servers = {
        @Server(
            description = "Production Environment",
            url = "https://api.yourcompany.com/v1"
        ),
        @Server(
            description = "Staging Environment",
            url = "https://staging-api.yourcompany.com/v1"
        ),
        @Server(
            description = "Development Environment",
            url = "http://localhost:8080/api/v1"
        )
    },
    security = {
        @SecurityRequirement(name = "bearerAuth"),
        @SecurityRequirement(name = "apiKey")
    },
    externalDocs = @ExternalDocumentation(
        description = "Find more info here",
        url = "https://yourcompany.com/docs"
    )
)
public class JwtAuthApplication {

  public static void main(String[] args) {
    SpringApplication.run(JwtAuthApplication.class, args);
  }
}
