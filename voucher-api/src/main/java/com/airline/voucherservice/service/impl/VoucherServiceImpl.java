package com.airline.voucherservice.service.impl;

import com.airline.voucherservice.model.Voucher;
import com.airline.voucherservice.repository.VoucherRepository;
import com.airline.voucherservice.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class VoucherServiceImpl implements VoucherService {

    private final VoucherRepository voucherRepository;

    @Autowired
    public VoucherServiceImpl(VoucherRepository voucherRepository) {
        this.voucherRepository = voucherRepository;
    }

    @Override
    public Voucher getVoucher(String id) {
        Optional<Voucher> voucher=voucherRepository.findByCode(id);
        if(voucher.isPresent()){
            return voucher.get();
        }
        return null;
    }

    @Override
    public Voucher createVoucher(Voucher dto) {

        return voucherRepository.save(dto);
    }

    @Override
    public Voucher assignVoucher(String code, Long userId) {
        Voucher voucher = voucherRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Voucher not found"));
        voucher.setUserId(userId);
        return voucherRepository.save(voucher);
    }

    @Override
    public boolean validateVoucher(String code, Long userId) {
        Voucher voucher = voucherRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Voucher not found"));

        return !voucher.isUsed()
                && voucher.getUserId().equals(userId)
                && voucher.getValidUntil().isAfter(LocalDate.now());
    }

    @Override
    public Voucher redeemVoucher(String code) {
        Voucher voucher = voucherRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Voucher not found"));

        if (voucher.isUsed() || voucher.getValidUntil().isBefore(LocalDate.now())) {
            throw new RuntimeException("Voucher is not valid or already used");
        }

        voucher.setUsed(true);
        return voucherRepository.save(voucher);
    }

    @Override
    public List<Voucher> getUserVouchers(Long userId) {
        return voucherRepository.findAllByUserId(userId);
    }
}
