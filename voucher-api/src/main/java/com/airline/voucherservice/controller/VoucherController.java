package com.airline.voucherservice.controller;

import com.airline.voucherservice.model.Voucher;
import com.airline.voucherservice.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/voucher")
public class VoucherController {

    private final VoucherService voucherService;

    @Autowired
    public VoucherController(VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    @PostMapping()
    public ResponseEntity<Voucher> createVoucher(@RequestBody Voucher dto) {
        return ResponseEntity.ok(voucherService.createVoucher(dto));
    }
    @GetMapping("/{id}")
    public ResponseEntity<Voucher> getVoucher(@PathVariable String id) {
        return ResponseEntity.ok(voucherService.getVoucher(id));
    }


    @PostMapping("/assign")
    public ResponseEntity<Voucher> assignVoucher(@RequestParam String code, @RequestParam Long userId) {
        return ResponseEntity.ok(voucherService.assignVoucher(code, userId));
    }

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateVoucher(@RequestParam String code, @RequestParam Long userId) {
        return ResponseEntity.ok(voucherService.validateVoucher(code, userId));
    }

    @PostMapping("/redeem")
    public ResponseEntity<Voucher> redeemVoucher(@RequestParam String code) {
        return ResponseEntity.ok(voucherService.redeemVoucher(code));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Voucher>> getUserVouchers(@PathVariable Long userId) {
        return ResponseEntity.ok(voucherService.getUserVouchers(userId));
    }
}
