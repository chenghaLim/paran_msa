package com.paranmanzang.gatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@EnableDiscoveryClient
@SpringBootApplication
public class GatewayServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayServerApplication.class, args);
    }

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path("/get")
                        .filters(f -> f.addRequestHeader("Hello", "World"))
                        .uri("http://httpbin.org:80"))
                .route(r -> r.path("/api/chats/**").uri("lb://chat-service"))
                .route(r -> r.path("/api/comments/**").uri("lb://comment-service"))
                .route(r -> r.path("/api/files/**").uri("lb://file-service"))
                .route(r -> r.path("/api/groups/**").uri("lb://group-service"))
                .route(r -> r.path("/api/rooms/**").uri("lb://room-service"))
                .route(r -> r.path("/api/users/**").uri("lb://user-service"))
                .build();
    }

}
