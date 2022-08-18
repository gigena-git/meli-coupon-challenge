package xyz.gigena.melicouponchallenge.configuration;

import java.net.http.HttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.gigena.melicouponchallenge.service.ItemService;

@Configuration
public class CouponConfiguration {
  @Bean
  public ItemService instantiateCouponService() {
    HttpClient client = HttpClient.newHttpClient();
    return new ItemService(client);
  }
}
