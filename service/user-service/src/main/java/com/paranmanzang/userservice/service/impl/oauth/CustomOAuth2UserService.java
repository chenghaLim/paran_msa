package com.paranmanzang.userservice.service.impl.oauth;


import com.paranmanzang.userservice.model.domain.user.CustomOAuth2User;
import com.paranmanzang.userservice.model.domain.user.NaverResponse;
import com.paranmanzang.userservice.model.domain.user.OAuth2Response;
import com.paranmanzang.userservice.model.domain.user.UserModel;
import com.paranmanzang.userservice.model.entity.User;
import com.paranmanzang.userservice.model.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println("o2auth서비스");
        System.out.println(oAuth2User);

        if (oAuth2User == null) {
            throw new OAuth2AuthenticationException("Failed to load OAuth2 user");
        }

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("naver")) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        } else {
            return null;
        }

        String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
        User existData = userRepository.findByUsername(username);

        if (existData == null) {
            User user = new User();
            user.setUsername(username);
            user.setName(oAuth2Response.getName());
            user.setNickname(createRandomNickname());
            user.setTel(oAuth2Response.getMobile());
            user.setState(true);
            user.setPassword(RandomStringUtils.randomAlphanumeric(10));
            user.setRole("ROLE_USER");

            userRepository.save(user);

            UserModel userModelDTO = new UserModel();
            userModelDTO.setUsername(username);
            userModelDTO.setName(oAuth2Response.getName());
            userModelDTO.setNickname(createRandomNickname());
            userModelDTO.setTel(oAuth2Response.getMobile());
            userModelDTO.setPassword(RandomStringUtils.randomAlphanumeric(10));
            userModelDTO.setRole("ROLE_USER");

            return new CustomOAuth2User(userModelDTO);
        } else {
            existData.setName(oAuth2Response.getName());
            existData.setTel(oAuth2Response.getMobile());

            userRepository.save(existData);

            UserModel userModel = new UserModel();
            userModel.setUsername(existData.getUsername());
            userModel.setName(oAuth2Response.getName());
            userModel.setNickname(existData.getNickname());
            userModel.setTel(existData.getTel());
            userModel.setRole(existData.getRole());
            userModel.setPassword(existData.getPassword());

            return new CustomOAuth2User(userModel);
        }
    }

    public String createRandomNickname(){
        return "paran"+RandomStringUtils.randomAlphanumeric(10);
    }
}