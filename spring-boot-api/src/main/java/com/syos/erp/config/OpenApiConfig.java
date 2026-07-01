package com.syos.erp.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI syosOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("SYOS ERP Spring Boot API")
                        .version("0.1.0")
                        .description("Portfolio backend migration of the legacy Java Servlet/JSP SYOS ERP system.")
                        .license(new License().name("Portfolio project")));
    }

    @Bean
    public GroupedOpenApi suppliersApi() {
        return GroupedOpenApi.builder()
                .group("suppliers")
                .displayName("Suppliers")
                .pathsToMatch("/api/suppliers/**")
                .build();
    }

    @Bean
    public GroupedOpenApi productsApi() {
        return GroupedOpenApi.builder()
                .group("products")
                .displayName("Products")
                .pathsToMatch("/api/products/**")
                .build();
    }

    @Bean
    public GroupedOpenApi inventoryApi() {
        return GroupedOpenApi.builder()
                .group("inventory")
                .displayName("Inventory")
                .pathsToMatch("/api/inventory/**")
                .build();
    }

    @Bean
    public GroupedOpenApi reorderApi() {
        return GroupedOpenApi.builder()
                .group("reorder-levels")
                .displayName("Reorder Levels")
                .pathsToMatch("/api/reorder-levels/**")
                .build();
    }

    @Bean
    public GroupedOpenApi reportsApi() {
        return GroupedOpenApi.builder()
                .group("reports")
                .displayName("Reports")
                .pathsToMatch("/api/reports/**")
                .build();
    }

    @Bean
    public GroupedOpenApi salesApi() {
        return GroupedOpenApi.builder()
                .group("sales")
                .displayName("Sales")
                .pathsToMatch("/api/sales/**")
                .build();
    }

    @Bean
    public GroupedOpenApi billsApi() {
        return GroupedOpenApi.builder()
                .group("bills")
                .displayName("Bills")
                .pathsToMatch("/api/bills/**")
                .build();
    }
}
