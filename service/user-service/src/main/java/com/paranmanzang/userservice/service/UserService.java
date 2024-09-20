package com.paranmanzang.userservice.service;



import com.paranmanzang.userservice.model.domain.user.RegisterModel;
import com.paranmanzang.userservice.model.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    //일반 회원가입
    void create(RegisterModel registerModel);


    //유저 닉네임 중복체크
    boolean check(RegisterModel registerModel);

    // 비밀번호 변경
    boolean updatePassword(Long userId, String newPassword);

    // 비밀번호 확인
    boolean checkPassword(String rawPassword, String encodedPassword);

    // 회원정보 조회(관리자용 리스트 띄우기)
    List<User> getAllUsers(Long userId);

    // 회원정보 조회(한명거)
    User getUserDetail(Long userId);

    // 탈퇴
    boolean deleteUser(Long userId);
}