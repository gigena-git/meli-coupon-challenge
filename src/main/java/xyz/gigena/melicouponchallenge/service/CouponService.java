package xyz.gigena.melicouponchallenge.service;

import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Service;
import xyz.gigena.melicouponchallenge.dto.ItemDTO;
import xyz.gigena.melicouponchallenge.dto.ItemSetDTO;
import xyz.gigena.melicouponchallenge.entity.Coupon;

@Service
public class CouponService {

  public Coupon getBestCoupon(ItemSetDTO itemSetDTO, Float amount) {
    // Convert the set into array to index them.
    int itemCount = itemSetDTO.getItems().size();
    long amountLimit = (long) (amount * 100);
    ItemDTO[] itemsArray = indexItemSet(itemSetDTO, itemCount);
    long[] prices = getIndexedPrices(itemCount, itemsArray);
    Double maxAmount = knapsackZeroToOne(itemCount, amountLimit, prices) / 100.0;
    Set<ItemDTO> items = new HashSet<>();
    // return new Coupon((float)(dp[itemCount - 1][amountLimit]));
    return null;
  }

  private ItemDTO[] indexItemSet(ItemSetDTO itemSetDTO, int itemCount) {
    ItemDTO[] itemsArray = new ItemDTO[itemCount];
    int k = 0;
    for (ItemDTO iDto : itemSetDTO.getItems()) {
      itemsArray[k++] = iDto;
    }
    return itemsArray;
  }

  private long[] getIndexedPrices(int itemCount, ItemDTO[] itemsArray) {
    long[] prices = new long[itemCount];
    for (int i = 0; i < itemCount; i++) {
      prices[i] =
          (long)
              (itemsArray[i].getPrice()
                  * 100); // We multiply by one hundred to round the last two decimals.
    }
    return prices;
  }

  /**
   * This method will apply the dynamic programming solution to the 0-1 Knapsack Problem to
   * determine the maximum amount possible from the list of items.
   */
  private long knapsackZeroToOne(int itemCount, long amountLimit, long[] prices) {
    long[][] dp = new long[itemCount + 1][(int) amountLimit + 1];
    for (int i = 0; i <= itemCount; i++) {
      for (int cent = 0; cent <= amountLimit; cent++) {
        if (i == 0 || cent == 0) {
          dp[i][cent] = 0;
        } else if (prices[i - 1] <= cent) {
          dp[i][cent] =
              Math.max(dp[i - 1][cent], dp[i - 1][(int) (cent - prices[i - 1])] + prices[i - 1]);
        } else {
          dp[i][cent] = dp[i - 1][cent];
        }
      }
    }
    return dp[itemCount - 1][(int) amountLimit];
  }
}
