package com.paranmanzang.userservice.model.domain.user;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginModel {
    private String username;
    private String password;
    private boolean state;
}
