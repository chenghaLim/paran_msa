package com.paranmanzang.chatservice.config;


import com.paranmanzang.chatservice.controller.ChatRoomHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ChatRoomRoute {

    @Bean
    public RouterFunction<ServerResponse> roomRoute(ChatRoomHandler chatRoomHandler) {
        return // # 3
                route(POST("/api/chats/room").and(accept(MediaType.APPLICATION_JSON)), chatRoomHandler::createRoom)
                        // # 7
                        .andRoute(GET("/api/chats/room/getchatlist").and(accept(MediaType.APPLICATION_JSON)), chatRoomHandler::getChatList)
                        // # 100
                        .andRoute(PUT("/api/chats/room/updatename").and(accept(MediaType.APPLICATION_JSON)), chatRoomHandler::updateName)
                        // # 101
                        .andRoute(PUT("/api/chats/room/updatepassword").and(accept(MediaType.APPLICATION_JSON)), chatRoomHandler::updatePassword)
                        // # 99
                        .andRoute(DELETE("/api/chats/room/{roomId}").and(accept(MediaType.APPLICATION_JSON)), chatRoomHandler::deleteRoom)
                        // # 109
                        .andRoute(POST("/api/chats/message/lastreadtime/{roomId}").and(accept(MediaType.APPLICATION_JSON)), chatRoomHandler::saveLastReadMessageTime);
    }
}
