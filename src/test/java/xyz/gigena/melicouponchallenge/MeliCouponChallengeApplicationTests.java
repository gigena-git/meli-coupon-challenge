package xyz.gigena.melicouponchallenge;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import xyz.gigena.melicouponchallenge.controller.CouponController;
import xyz.gigena.melicouponchallenge.dto.CouponDTO;
import xyz.gigena.melicouponchallenge.service.CouponService;
import xyz.gigena.melicouponchallenge.service.ItemService;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class MeliCouponChallengeApplicationTests {

  @InjectMocks ItemService itemService;
  @Mock HttpClient httpClient;
  @Mock HttpResponse<String> httpResponse;
  String happyPathJsonString =
      "[{\"code\": 404, \"body\": {\"id\": \"MLA5\"}},"
          + "{\"code\": 404, \"body\": {\"id\":\"MLA4\"}},"
          + "{\"code\": 404, \"body\": {\"id\": \"MLA3\"}},"
          + "{\"code\": 200, \"body\": {\"id\": \"MLA1120809452\",\"price\":50000}},"
          + "{\"code\": 200, \"body\": {\"id\": \"MLA816019440\",\"price\":116352.03}}]";

  @Test
  void testHappyPath() {
    CouponDTO couponRequestDTO = createMockRequest();
    CouponController controller = buildController();
    ResponseEntity<CouponDTO> couponResponseDTO = controller.createCoupon(couponRequestDTO);
    Assertions.assertEquals(couponResponseDTO.getBody().getAmount(), 166352.03);
  }

  private CouponDTO createMockRequest() {
    CouponDTO couponRequestDTO = mockCouponRequest();
    String uriString = "https://api.mercadolibre.com/items?ids=";
    for (String item : couponRequestDTO.getItemIds()) {
      uriString += item + ",";
    }
    uriString = uriString.substring(0, uriString.length() - 1);
    HttpRequest request = HttpRequest.newBuilder().uri(URI.create(uriString)).GET().build();
    Mockito.when(httpResponse.statusCode()).thenReturn(200);
    Mockito.when(httpResponse.body()).thenReturn(happyPathJsonString);
    try {
      Mockito.when(httpClient.send(request, BodyHandlers.ofString())).thenReturn(httpResponse);
    } catch (IOException e) {
      System.out.println("IO Exception occurred");
    } catch (InterruptedException e) {
      System.out.println("Interrupted Exception occurred");
    }
    return couponRequestDTO;
  }

  private CouponController buildController() {
    itemService = new ItemService(httpClient);
    CouponService couponService = new CouponService();
    CouponController controller = new CouponController();
    controller.setItemService(itemService);
    controller.setCouponService(couponService);
    return controller;
  }

  private CouponDTO mockCouponRequest() {
    CouponDTO couponRequestDTO = new CouponDTO();
    couponRequestDTO.setAmount(166352.03);
    couponRequestDTO.setItemIds(new ArrayList<String>());
    couponRequestDTO.getItemIds().add("MLA1120809452");
    couponRequestDTO.getItemIds().add("MLA816019440");
    couponRequestDTO.getItemIds().add("MLA3");
    couponRequestDTO.getItemIds().add("MLA4");
    couponRequestDTO.getItemIds().add("MLA5");
    return couponRequestDTO;
  }
}
