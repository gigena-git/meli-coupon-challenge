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
import xyz.gigena.melicouponchallenge.service.CouponService;
import xyz.gigena.melicouponchallenge.service.ItemService;

/**
 * @author Maximiliano Gigena
 */
@RestController
@RequestMapping("/coupon")
public class CouponController {

  @Autowired private CouponRepository couponRepository;
  @Autowired private ItemService itemService;
  @Autowired private CouponService couponService;

  /**
   * @param couponRequestDTO This is a json object that specifies a request containing a list of
   *     items and a maximum amount to spend. Items might be valid or invalid.
   * @return The coupon containing the most expensive set of items that can be bought with the
   *     provided amount.
   */
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CouponDTO> createCoupon(@RequestBody CouponDTO couponRequestDTO) {
    long amountLimit = (long) (couponRequestDTO.getAmount() * 100);
    ItemSetDTO itemSetDTO = itemService.getNewItems(couponRequestDTO);
    Coupon coupon = couponService.getBestCoupon(itemSetDTO, amountLimit);
    couponRepository.save(coupon);
    // return ResponseEntity.ok(couponResponseDTO);
    return null;
  }
}
