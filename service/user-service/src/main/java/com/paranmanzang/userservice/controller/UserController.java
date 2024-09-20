package com.paranmanzang.userservice.controller;


import com.paranmanzang.userservice.model.domain.user.RegisterModel;
import com.paranmanzang.userservice.model.repository.UserRepository;
import com.paranmanzang.userservice.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/users/users")
public class UserController {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private UserRepository userRepository;

    //일반 회원 가입
    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody RegisterModel registerModel) {
        try {
            userService.create(registerModel);
            return ResponseEntity.ok("회원가입 성공");
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
    //비밀번호 변경 (소셜이면 소셜로그인입니다 뜨게 막아야됨.)
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestParam String newPassword) {
        if(userRepository.findById(id).get().getUsername().startsWith("naver")){
            throw new IllegalArgumentException("올바르지 않은 접근입니다.");
        }
        return ResponseEntity.ok(userService.updatePassword(id, newPassword));
    }

    //본인 정보 조회
    @GetMapping("/getinfo/{id}")
    public ResponseEntity<?> getInfo(@PathVariable Long id) {
        if(!userRepository.existsById(id)){
            throw new IllegalArgumentException("올바르지 않은 접근입니다.");
        }
        return ResponseEntity.ok( userService.getUserDetail(id));
    }

    //회원 탈퇴
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if(!userRepository.existsById(id)){
            throw new IllegalArgumentException("올바르지 않은 접근입니다.");
        }
        return ResponseEntity.ok( userService.deleteUser(id));
    }

    //닉네임 검증
    @GetMapping("/check-nickname")
    public ResponseEntity<?> checkNickname(@RequestBody RegisterModel registerModel) {
        return ResponseEntity.ok(userService.check(registerModel));
    }

    //비밀번호 확인
    @GetMapping("/check-password")
    public ResponseEntity<?> checkPassword(@RequestBody RegisterModel registerModel) {
        return ResponseEntity.ok(userService.checkPassword(registerModel.getPassword(), registerModel.getPasswordcheck()));
    }

    //회원 정보 조회(관리자용)
    @GetMapping("/getAllUsers")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    //회원 정보 조회(관리자용 백에서 막아야되면 아이디 투척)
    @GetMapping("/getAllUser/{userId}")
    public ResponseEntity<?> getAllUser(@RequestParam Long userId) {
        return ResponseEntity.ok(userService.getAllUsers(userId));
    }
}
