package com.paranmanzang.userservice.service.impl;


import com.paranmanzang.userservice.model.domain.user.RegisterModel;
import com.paranmanzang.userservice.model.entity.User;
import com.paranmanzang.userservice.model.repository.UserRepository;
import com.paranmanzang.userservice.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //일반 회원가입
    public void create(RegisterModel registerModel) {
        // 사용자 이름 중복 확인
        Boolean isExist = userRepository.existsByUsername(registerModel.getUsername());
        if (isExist) {
            throw new RuntimeException("이미 존재하는 사용자입니다.");
        }
        // 닉네임 중복 확인
        if (userRepository.existsByNickname(registerModel.getNickname())) {
            throw new RuntimeException("이미 존재하는 닉네임입니다.");
        }

        // 비밀번호와 비밀번호 확인 일치 여부 확인
        if (!registerModel.getPassword().equals(registerModel.getPasswordcheck())) {
            throw new RuntimeException("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }
        //이메일 검증
        if(!Pattern.matches("^[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}$", registerModel.getUsername())){
            throw new RuntimeException("이메일 형식이 올바르지 않습니다.");
        }

        //비밀번호 검증
        if(!Pattern.matches("^(?=.*[a-zA-Z])((?=.*\\d)|(?=.*\\W)).{8,128}$", registerModel.getPassword())){
            throw new RuntimeException("비밀번호가 올바르지 않습니다.최소 8자 이상이며, 알파벳 대소문자, 숫자 또는 특수 문자가 포함시켜주세요.");
        }

        //휴대폰 번호 검증
        if(!Pattern.matches("^010-\\d{3,4}-\\d{4}$", registerModel.getTel())){
            throw new RuntimeException("휴대폰 번호가 올바르지 않습니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(registerModel.getPassword());

        // 새로운 UserModel 생성
        User user = new User();
        user.setUsername(registerModel.getUsername());
        user.setPassword(encodedPassword);
        user.setName(registerModel.getName());
        user.setTel(registerModel.getTel());
        user.setNickname(registerModel.getNickname());
        user.setState(true);
        user.setRole("ROLE_USER"); // 기본 역할 설정

        // 사용자 정보 저장
        userRepository.save(user);
    }

    //유저 닉네임 중복체크
    public boolean check(RegisterModel registerModel) {
        return !userRepository.existsByNickname(registerModel.getNickname());
    }

    // 비밀번호 변경
    public boolean updatePassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return true;
    }

    // 비밀번호 확인
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    // 회원정보 조회(관리자용 리스트 띄우기)
    public List<User> getAllUsers(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

        if (user.getRole().equals("ROLE_ADMIN")) {
            return userRepository.findAll();
        } else {
            throw new IllegalArgumentException("관리자만 접근 가능합니다.");
        }
    }

    // 회원정보 조회(한명거)
    public User getUserDetail(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
    }

    // 탈퇴
    public boolean deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
        user.setState(false);
        return true;
    }

}