
package com.mmenshikov.PartyTreasurer.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created: 10.08.2022 10:34
 *
 * @author MMenshikov
 */
@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI customOpenApi() {
    return new OpenAPI().info(new Info().title("Application API")
            .version("1.0")
            .description("Party Treasurer")
            .license(new License().name("Apache 2.0")
                .url("http://springdoc.org"))
            .contact(new Contact().name("mmenshikov")
                .email("maksim-menshikov@mail.ru")))
        .servers(List.of(new Server().url("/")
            .description("")));
  }
}
