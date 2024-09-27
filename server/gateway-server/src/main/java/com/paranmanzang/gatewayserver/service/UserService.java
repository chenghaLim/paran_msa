package com.paranmanzang.gatewayserver.service;

import com.paranmanzang.gatewayserver.model.RegisterModel;
import com.paranmanzang.gatewayserver.model.entity.User;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.util.List;


@Service
public interface UserService {
        Mono<Void> create(RegisterModel registerModel);

        Mono<Boolean> deleteUser(String nickname);


        Mono<Boolean> logoutTime(String nickname);

        Mono<Boolean> updatePassword(String nickname, String newPassword);

        Mono<Boolean> checkNickname(RegisterModel registerModel);

        Mono<Boolean> checkPassword(RegisterModel registerModel);

        Mono<List<User>> getAllUsers(String nickname);

        Mono<User> getUserDetail(String nickname);

}
