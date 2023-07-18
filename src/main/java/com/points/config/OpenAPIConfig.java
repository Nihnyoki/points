package com.points.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Code-First Approach (reflectoring.io)",
                description = "" +
                        "Spring Doc description from annotation",
                contact = @Contact(
                        name = "Reflectoring",
                        url = "https://reflectoring.io",
                        email = "petros.stergioulas94@gmail.com"
                ),
                license = @License(
                        name = "MIT Licence",
                        url = "https://github.com/thombergs/code-examples/blob/master/LICENSE"))/*,
        servers = @Server(url = "http://localhost:8080")*/
)
@Configuration
public class OpenAPIConfig {
    public OpenAPI baseOpenAPI(){
        return new OpenAPI().info(new io.swagger.v3.oas.models.info.Info()
                .title("Spring Doc")
                .version("1.0.0")
                .description("Spring doc."));
    }
}
