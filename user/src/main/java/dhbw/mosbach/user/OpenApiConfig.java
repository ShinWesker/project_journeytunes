package dhbw.mosbach.user;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().servers(
                        List.of(new Server().url("http://localhost:8222"), new Server().url("http://10.50.15.51:8222"))
                )
                .info(new Info()
                        .title("User API")
                        .description("This is a API to for the user endpoint of JourneyTunes.")
                        .version("1.0.0"));
    }
}
