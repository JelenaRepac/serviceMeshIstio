package com.airline.voucherservice.repository;

import com.airline.voucherservice.model.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VoucherRepository extends JpaRepository<Voucher, Long> {

    Optional<Voucher> findByCode(String code);
    List<Voucher> findAllByUserId(Long userId);
}
