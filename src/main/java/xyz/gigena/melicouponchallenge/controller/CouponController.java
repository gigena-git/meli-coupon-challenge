package xyz.gigena.melicouponchallenge.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.gigena.melicouponchallenge.dto.CouponDTO;
import xyz.gigena.melicouponchallenge.dto.ItemSetDTO;
import xyz.gigena.melicouponchallenge.service.CouponService;
import xyz.gigena.melicouponchallenge.service.ItemService;

/**
 * @author Maximiliano Gigena
 */
@RestController
@RequestMapping("/coupon")
public class CouponController {

  private final Logger logger = LoggerFactory.getLogger(CouponController.class);

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
    if (couponRequestDTO == null) {
      logger.error("Null coupon request received");
      return ResponseEntity.badRequest().build();
    }
    if (couponRequestDTO.getItemIds() == null || couponRequestDTO.getItemIds().size() == 0) {
      logger.error("Empty item list received");
      return ResponseEntity.badRequest().build();
    }
    if (couponRequestDTO.getAmount() == null || couponRequestDTO.getAmount() <= 0) {
      logger.error("Invalid amount received");
      return ResponseEntity.badRequest().build();
    }
    ItemSetDTO itemSetDTO = itemService.getNewItems(couponRequestDTO);
    CouponDTO couponResponseDTO =
        couponService.getBestCoupon(itemSetDTO, couponRequestDTO.getAmount());
    return ResponseEntity.ok(couponResponseDTO);
  }

  public void setItemService(ItemService itemService) {
    this.itemService = itemService;
  }

  public void setCouponService(CouponService couponService) {
    this.couponService = couponService;
  }
}
