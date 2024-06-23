package hieutran.crud.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Configuration
@Profile("!product")
//!Không nên build trên môi trường product vì lỗ hỏng bảo mật, nên chỉ build trên môi trường dev, test
public class OpenAPIConfig {
    @Bean
    public OpenAPI openAPI(
            @Value("${open.api.title}") String title,
            @Value("${open.api.version}") String version,
            @Value("${open.api.description}") String description,
            @Value("${open.api.serverUrl}") String serverUrl,
            @Value("${open.api.serverName}") String serverName
    ) {
        return new OpenAPI()
                .info(new Info()
                        .title(title)
                        .version(version)
                        .description(description)
                        .license(new License() //? Cái này để đánh dấu bản quyền
                                .name("API License")
                                .url("http://domain.vn/license")
                        ))
                .servers(List.of(new Server()//? Cái này để đánh dấu server
                        .url(serverUrl)
                        .description(serverName)));
        //! từ hàng 39 - 47 là cấu hình security (authenticator) cho swagger
//                .components(new Components()
//                        .addSecuritySchemes(
//                                "bearerAuth",
//                                new SecurityScheme()
//                                        .type(SecurityScheme.Type.HTTP)
//                                        .scheme("bearer")
//                                        .bearerFormat("JWT")))
//                .security(List.of(new SecurityRequirement().addList("bearerAuth")
//                ));
    }

    //? GroupedOpenApi là một cấu hình để chia nhóm các API theo các gói cụ thể
    @Bean
    public GroupedOpenApi groupedOpenApi() {
        return GroupedOpenApi.builder()
                .group("api-service")
                .packagesToScan("hieutran.crud.controller")
                .pathsToMatch("/api/**")
                .build();
    }
}
