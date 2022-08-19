package xyz.gigena.melicouponchallenge.dto;

public class ItemNotFoundDTO extends ItemDTO{

    public ItemNotFoundDTO(String name) {
        super(name);
    }

    @Override
    public Double getPrice() {
        return Double.MAX_VALUE / 100;
    }
  
}
