package com.paranmanzang.userservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class AdminController {
    //관리자 확인용 메서드
    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }
}
