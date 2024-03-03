package dhbw.mosbach.hotel;

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
                        List.of(new Server().url("http://gateway:8222"))
                )
                .info(new Info()
                        .title("Hotel API")
                        .description("This is a API to for the hotel endpoint of journeytunes.")
                        .version("1.0.0"));
    }
}
