package com.paranmanzang.chatservice.config;

import com.paranmanzang.chatservice.controller.ChatMessageHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ChatMessageRoute {
    @Bean
    public RouterFunction<ServerResponse> messageRoute(ChatMessageHandler chatMessageHandler) {
        return route()
                // # 108
                .GET("/api/chats/message/{roomId}", accept(MediaType.TEXT_EVENT_STREAM), chatMessageHandler::getMessageList)
                // # 6
                .POST("/api/chats/message", accept(MediaType.APPLICATION_JSON), chatMessageHandler::insertMessage)
                // # 110
                .GET("/api/chats/message/totalunread", accept(MediaType.APPLICATION_JSON), chatMessageHandler::unReadTotalMessageCount)
                .build();
    }

}
