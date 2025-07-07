package com.airline.voucherservice.service;

import com.airline.voucherservice.model.Voucher;

import java.util.List;

public interface VoucherService {

    Voucher createVoucher(Voucher dto);
    Voucher assignVoucher(String code, Long userId);
    boolean validateVoucher(String code, Long userId);
    Voucher redeemVoucher(String code);
    List<Voucher> getUserVouchers(Long userId);
}
