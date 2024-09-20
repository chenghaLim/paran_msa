package com.paranmanzang.roomservice.controller;


import com.paranmanzang.roomservice.model.domain.AccountCancelModel;
import com.paranmanzang.roomservice.model.domain.AccountResultModel;
import com.paranmanzang.roomservice.service.impl.AccountServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rooms/accounts")
public class AccountController {

    private final AccountServiceImpl accountService;

    @PostMapping("/success")
    public ResponseEntity<?> savePayment(@RequestBody AccountResultModel model) {
        return ResponseEntity.ok(accountService.requestPayment(model));
    }

    @GetMapping("/findPayment")
    public ResponseEntity<?> findByOrderId(@RequestParam String orderId){
        return ResponseEntity.ok(accountService.findByOrderId(orderId));
    }


    @PutMapping("/cancel")
    public ResponseEntity<?> cancelPayment(@RequestBody AccountCancelModel model){
        return ResponseEntity.ok(accountService.cancel(model));
    }

}
