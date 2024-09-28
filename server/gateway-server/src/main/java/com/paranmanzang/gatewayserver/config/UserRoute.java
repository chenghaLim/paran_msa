package com.paranmanzang.gatewayserver.config;

import com.paranmanzang.gatewayserver.controller.UserController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Slf4j
@Configuration
public class UserRoute {
    @Bean
    public RouterFunction<ServerResponse> setUserRoute(UserController userController) {
        log.info("여기 도착");
        return  //회원가입
                route(POST("/api/users/create").and(accept(MediaType.APPLICATION_JSON)), userController::createUser)
                        .andRoute(DELETE("/api/users/delete").and(accept(MediaType.APPLICATION_JSON)), userController::delete)
                        .andRoute(PUT("/api/users/updatePassword").and(accept(MediaType.APPLICATION_JSON)), userController::updatePassword)
                        .andRoute(GET("/api/users/getAllUsers").and(accept(MediaType.APPLICATION_JSON)), userController::getAllUsers)
                        .andRoute(GET("/api/users/getUserDetail").and(accept(MediaType.APPLICATION_JSON)), userController::getUserDetail)
                        .andRoute(POST("/api/users/checkNickname").and(accept(MediaType.APPLICATION_JSON)), userController::checkNickname)
                        .andRoute(POST("/api/users/checkPassword").and(accept(MediaType.APPLICATION_JSON)), userController::checkPassword)
                        .andRoute(PUT("/api/users/logoutUserTime").and(accept(MediaType.APPLICATION_JSON)), userController::logoutUserTime);
    }
}

//GET getAllUsers 82번 포트 탐 근데 갑자기 됨???

//안되는거 목록

//PUT logoutUserTime 유저를 못찾음
//PUT updatePassword 82번 포트 탐


