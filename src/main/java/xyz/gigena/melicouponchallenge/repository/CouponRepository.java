package xyz.gigena.melicouponchallenge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.gigena.melicouponchallenge.entity.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long> {}
