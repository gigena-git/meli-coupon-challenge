package xyz.gigena.melicouponchallenge.configuration;

import java.net.http.HttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.gigena.melicouponchallenge.service.CouponService;

@Configuration
public class CouponConfiguration {
  @Bean
  public CouponService instantiateCouponService() {
    HttpClient client = HttpClient.newHttpClient();
    return new CouponService(client);
  }
}
