package com.paranmanzang.userservice.model.repository;

import com.paranmanzang.userservice.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
    User findByNickname(String nickname);
    Boolean existsByUsername(String username);
    Boolean existsByNickname(String nickname);
}
