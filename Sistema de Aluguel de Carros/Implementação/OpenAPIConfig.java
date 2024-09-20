package br.com.demo.regescweb;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springdoc.core.GroupedOpenApi;

@Configuration
public class OpenAPIConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("clientes-public")
                .pathsToMatch("/clientes/**")  // Caminho dos endpoints API
                .build();
    }
}
