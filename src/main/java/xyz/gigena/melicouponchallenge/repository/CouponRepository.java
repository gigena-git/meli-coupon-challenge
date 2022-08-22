package xyz.gigena.melicouponchallenge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xyz.gigena.melicouponchallenge.entity.Coupon;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {}
