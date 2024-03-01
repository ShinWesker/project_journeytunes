package dhbw.mosbach.user;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "config")
@Data
public class AppConfigProperties {

    private String HOST_SYSTEM_IP_ADDRESS;
    private int EMAIL_SERVICE_PORT;
    private int USER_SERVICE_PORT;

}
