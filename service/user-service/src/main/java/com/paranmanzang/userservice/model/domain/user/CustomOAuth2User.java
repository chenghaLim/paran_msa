package com.paranmanzang.userservice.model.domain.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private final UserModel userModel;

    public CustomOAuth2User(UserModel userModel) {

        this.userModel = userModel;
    }

    @Override
    public Map<String, Object> getAttributes() {

        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {
                return userModel.getRole();
            }
        });

        return collection;
    }

    @Override
    public String getName() {

        return userModel.getName();
    }

    public String getUsername() {

        return userModel.getUsername();
    }

    public String getNickname() {

        return userModel.getNickname();
    }

}
