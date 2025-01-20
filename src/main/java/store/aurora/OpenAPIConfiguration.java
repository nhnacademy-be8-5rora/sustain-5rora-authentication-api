package store.aurora;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

/**
 * @package : store.aurora
 * @name : OpenAPIConfiguration.java
 * @date : 2025-01-20 오전 11:56
 * @author : Flature
 * @version : 1.0.0
 */
@Configuration
public class OpenAPIConfiguration {

    private static final String API_NAME = "jwt 발급 api 서버";
    private static final String API_VERSION = "1.0.0";
    private static final String API_DESCRIPTION = "jwt(액세스/리프레쉬) 토큰 발급 api 호출 가능";

    @Bean
    public OpenAPI openApiConfig() {
        return new OpenAPI()
                .info(new Info().title(API_NAME).description(API_DESCRIPTION).version(API_VERSION));
    }
}