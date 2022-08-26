package xyz.gigena.melicouponchallenge.service;

import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import xyz.gigena.melicouponchallenge.dto.CouponDTO;
import xyz.gigena.melicouponchallenge.dto.ItemDTO;
import xyz.gigena.melicouponchallenge.dto.ItemSetDTO;

/**
 * @author Maximiliano Gigena
 */
@Service
public class CouponService {

  private final Logger logger = LoggerFactory.getLogger(CouponService.class);
  /**
   * @param itemSetDTO This is a set of valid items.
   * @param amount This is the amount of money the user has to spend.
   * @return The best coupon to use.
   */
  public CouponDTO getBestCoupon(ItemSetDTO itemSetDTO, Double amount) {
    long amountLimit = (long) (amount * 100);
    ItemDTO[] itemsArray = indexItemSet(itemSetDTO);
    long[] prices = getIndexedPrices(itemsArray);
    long[][] dp = knapsackZeroToOne(prices, amountLimit);

    return getValidItems(itemsArray, prices, amountLimit, dp);
  }

  /**
   * @param itemSetDTO This is the set of items to index.
   * @return The indexed item set. Necessary to know which items to save later.
   */
  private ItemDTO[] indexItemSet(ItemSetDTO itemSetDTO) {
    ItemDTO[] itemsArray = new ItemDTO[itemSetDTO.getItems().size()];
    int k = 0;
    for (ItemDTO iDto : itemSetDTO.getItems()) {
      itemsArray[k++] = iDto;
    }
    return itemsArray;
  }

  /**
   * @param itemsArray This is the indexed item set.
   * @return The prices of the array of items, with the two decimal positions shifted, so that it
   *     can be represented as a long integer.
   */
  private long[] getIndexedPrices(ItemDTO[] itemsArray) {
    long[] prices = new long[itemsArray.length];
    for (int i = 0; i < itemsArray.length; i++) {
      prices[i] = (long) (itemsArray[i].getPrice() * 100);
    }
    return prices;
  }

  /**
   * @param prices These are the prices of the items.
   * @param amountLimit This is the amount of money the user has to spend.
   * @return The dynamic programming solution dataframe to the 0-1 Knapsack Problem that determines
   *     the maximum amount possible from the list of items.
   */
  private long[][] knapsackZeroToOne(long[] prices, long amountLimit) {
    long[][] dp = new long[prices.length + 1][(int) amountLimit + 1];
    for (int p = 0; p <= prices.length; p++) {
      for (int cent = 0; cent <= amountLimit; cent++) {
        if (p == 0 || cent == 0) {
          dp[p][cent] = 0;
          logger.debug("p: {}, cent: {}, dp[p][cent]: {}", p, cent, dp[p][cent]);
        } else if (prices[p - 1] <= cent) {
          logger.debug(
              "p: {}, cent: {}, dp[p - 1][cent]: {}, dp[p - 1][cent - prices[p - 1]]: {}",
              p,
              cent,
              dp[p - 1][cent],
              dp[p - 1][(int) (cent - prices[p - 1])]);
          dp[p][cent] =
              Math.max(dp[p - 1][cent], dp[p - 1][(int) (cent - prices[p - 1])] + prices[p - 1]);
          logger.debug("dp[p][cent]: {}", dp[p][cent]);
        } else {
          dp[p][cent] = dp[p - 1][cent];
          logger.debug("p: {}, cent: {}, dp[p - 1][cent]: {}", p, cent, dp[p - 1][cent]);
        }
      }
    }
    return dp;
  }

  /**
   * @param itemsArray This is the indexed item set.
   * @param prices This is the prices of the items. Necessary to compute substractions to the
   *     amountIncrease.
   * @param amountLimit This is the amount of money the user has to spend. @ @param dp This is the
   *     dynamic programming solution dataframe to the 0-1 Knapsack problem.
   * @return The coupon containing the set of items that can be purchased with the given amount.
   */
  private CouponDTO getValidItems(
      ItemDTO[] itemsArray, long[] prices, long amountLimit, long[][] dp) {
    Double maxAmount = (double) (dp[itemsArray.length][(int) amountLimit]) / 100.0;
    int item = itemsArray.length;
    long amountIncrease = amountLimit;
    Set<ItemDTO> items = new HashSet<>();
    while (item > 0 && amountIncrease > 0) {
      if (dp[item][(int) amountIncrease] != dp[item - 1][(int) amountIncrease]) {
        logger.info(
            "Found item part of optimum solution: "
                + itemsArray[item - 1].getName()
                + ", price:"
                + itemsArray[item - 1].getPrice());
        items.add(itemsArray[item - 1]);
        amountIncrease -= prices[item - 1];
      }
      item--;
    }
    return new CouponDTO(items, maxAmount);
  }
}
