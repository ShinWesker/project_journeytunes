package dhbw.mosbach;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.regex.Pattern;

@Component
public class AuthTokenVerificationFilter implements GlobalFilter {

    private final WebClient webClient;
    private static final Pattern IMAGE_URL_PATTERN = Pattern.compile(".*/hotels/api/v1/hotels/\\d+/image");


    public AuthTokenVerificationFilter(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8222").build();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        boolean isVerifyPath = path.startsWith("/users/api/v1/verify");

        if (path.endsWith("/users/api/v1/login")
                || path.endsWith("/users/api/v1/create")
                || path.endsWith("/users/api/v1/token/verify")
                || IMAGE_URL_PATTERN.matcher(path).matches()
                || isVerifyPath) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            return webClient.post()
                    .uri("/users/api/v1/token/verify")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue("{\"token\":\"" + token + "\"}")
                    .retrieve()
                    .onStatus(status -> status.value() >= 400,
                            clientResponse -> {
                                System.out.println("Token verification failed with status: " + clientResponse.statusCode());
                                return Mono.error(new RuntimeException("Token verification failed"));
                            })
                    .bodyToMono(Void.class)
                    .then(chain.filter(exchange))
                    .onErrorResume(e -> {
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        return exchange.getResponse().setComplete();
                    });
        } else {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }
}
