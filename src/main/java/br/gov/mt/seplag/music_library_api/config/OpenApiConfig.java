package br.gov.mt.seplag.music_library_api.config;

import br.gov.mt.seplag.music_library_api.controller.AlbumController;
import br.gov.mt.seplag.music_library_api.controller.ArtistaController;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OperationCustomizer sortParameterCustomizer() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            if (handlerMethod == null) return operation;
            Class<?> controllerClass = handlerMethod.getBeanType();
            List<Parameter> params = operation.getParameters();
            if (params == null) return operation;

            final String sortDescription;
            if (ArtistaController.class.equals(controllerClass) && "listar".equals(handlerMethod.getMethod().getName())) {
                sortDescription = "Ordenação: propriedade,direção. Ex: nome,asc ou nome,desc. Campos: id, nome, tipoArtista";
            } else if (AlbumController.class.equals(controllerClass) && "listar".equals(handlerMethod.getMethod().getName())) {
                sortDescription = "Ordenação: propriedade,direção. Ex: titulo,asc ou id,desc. Campos: id, titulo";
            } else {
                sortDescription = null;
            }

            if (sortDescription != null) {
                params.stream()
                        .filter(p -> "sort".equals(p.getName()))
                        .findFirst()
                        .ifPresent(p -> p.setDescription(sortDescription));
            }
            return operation;
        };
    }

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .info(new Info()
                        .title("Music Library API")
                        .description("API para gerenciamento de álbuns e artistas (versão v1)")
                        .version("v1"))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}
