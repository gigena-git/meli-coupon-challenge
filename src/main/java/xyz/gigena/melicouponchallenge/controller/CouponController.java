package xyz.gigena.melicouponchallenge.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import xyz.gigena.melicouponchallenge.dto.CouponDTO;
import xyz.gigena.melicouponchallenge.dto.ItemSetDTO;
import xyz.gigena.melicouponchallenge.entity.Coupon;
import xyz.gigena.melicouponchallenge.repository.CouponRepository;
import xyz.gigena.melicouponchallenge.service.ItemService;

@RestController
@RequestMapping("/coupon")
public class CouponController {

  @Autowired private CouponRepository couponRepository;

  @Autowired private ItemService itemService;

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CouponDTO> createCoupon(@RequestBody CouponDTO couponDTO) {
    ItemSetDTO itemSetDTO =  itemService.getNewItems(couponDTO);
    // determine the most optimal list to return to the user
    // Coupon coupon = couponService.getBestCoupon(itemSetDTO)
    // couponRepository.save(coupon);
    return ResponseEntity.ok(couponDTO);
    
  }
}
