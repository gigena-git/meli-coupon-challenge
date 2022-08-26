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
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import xyz.gigena.melicouponchallenge.controller.CouponController;
import xyz.gigena.melicouponchallenge.dto.CouponDTO;
import xyz.gigena.melicouponchallenge.service.CouponService;
import xyz.gigena.melicouponchallenge.service.ItemService;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class MeliCouponChallengeApplicationTests {

  @InjectMocks ItemService itemService;
  @Mock HttpClient httpClient;
  @Mock HttpResponse<String> httpResponse;
  String[] happyPathRequestIds = {"MLA1120809452", "MLA816019440", "MLA3", "MLA4", "MLA5"};
  String happyPathJsonString =
      "[{\"code\": 404, \"body\": {\"id\": \"MLA5\"}},"
          + "{\"code\": 404, \"body\": {\"id\":\"MLA4\"}},"
          + "{\"code\": 404, \"body\": {\"id\": \"MLA3\"}},"
          + "{\"code\": 200, \"body\": {\"id\": \"MLA1120809452\",\"price\":50000}},"
          + "{\"code\": 200, \"body\": {\"id\": \"MLA816019440\",\"price\":116352.03}}]";
  String emptyJsonString = "[]";
  String faultyJsonString =
      "[{\"code\": 404, \"body\": {id\": \"MLA5\"}},"
          + "{\"code\": 404, \"body\": {\"id\":\"MLA4\"}},"
          + "{\"code\": 404, \"body\": {\"id\": \"MLA3\"}},"
          + "{\"code\": 200, \"body\": {\"id\": \"MLA1120809452\",\"price\":50000}},"
          + "{\"code\": 200, \"body\": {\"id\": \"MLA816019440\",\"price\":116352.03}}]";

  @Test
  public void testHappyPath() {
    CouponDTO couponRequestDTO =
        createMockRequest(happyPathRequestIds, 166352.03, 200, happyPathJsonString);
    CouponController controller = buildController();
    ResponseEntity<CouponDTO> couponResponseDTO = controller.createCoupon(couponRequestDTO);
    Assertions.assertEquals(couponResponseDTO.getBody().getAmount(), 166352.03);
  }

  @Test
  public void test404ResponseFromItemService() {
    CouponDTO couponRequestDTO =
        createMockRequest(happyPathRequestIds, 166352.03, 404, emptyJsonString);
    CouponController controller = buildController();
    ResponseEntity<CouponDTO> couponResponseDTO = controller.createCoupon(couponRequestDTO);
    Assertions.assertEquals(couponResponseDTO.getStatusCodeValue(), 200);
    Assertions.assertEquals(couponResponseDTO.getBody().getAmount(), 0.0);
  }

  @Test
  public void test500ResponseFromItemService() {
    CouponDTO couponRequestDTO =
        createMockRequest(happyPathRequestIds, 166352.03, 500, emptyJsonString);
    CouponController controller = buildController();
    ResponseEntity<CouponDTO> couponResponseDTO = controller.createCoupon(couponRequestDTO);
    Assertions.assertEquals(couponResponseDTO.getStatusCodeValue(), 200);
    Assertions.assertEquals(couponResponseDTO.getBody().getAmount(), 0.0);
  }

  @Test
  public void testJsonParsingError() {
    CouponDTO couponRequestDTO =
        createMockRequest(happyPathRequestIds, 166352.03, 200, faultyJsonString);
    CouponController controller = buildController();
    ResponseEntity<CouponDTO> couponResponseDTO = controller.createCoupon(couponRequestDTO);
    Assertions.assertEquals(couponResponseDTO.getStatusCodeValue(), 200);
    Assertions.assertEquals(couponResponseDTO.getBody().getAmount(), 0.0);
  }

  private CouponDTO createMockRequest(
      String[] requestJsonIds, Double maxAmount, Integer responseCode, String requestResponse) {
    CouponDTO couponRequestDTO = mockCouponRequest(requestJsonIds, maxAmount);
    String uriString = "https://api.mercadolibre.com/items?ids=";
    for (String item : couponRequestDTO.getItemIds()) {
      uriString += item + ",";
    }
    uriString = uriString.substring(0, uriString.length() - 1);
    HttpRequest request = HttpRequest.newBuilder().uri(URI.create(uriString)).GET().build();
    Mockito.when(httpResponse.statusCode()).thenReturn(responseCode);
    Mockito.when(httpResponse.body()).thenReturn(requestResponse);
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

  private CouponDTO mockCouponRequest(String[] requestJsonIds, Double maxAmount) {
    CouponDTO couponRequestDTO = new CouponDTO();
    couponRequestDTO.setAmount(maxAmount);
    couponRequestDTO.setItemIds(new ArrayList<String>());
    for (String id : requestJsonIds) {
      couponRequestDTO.getItemIds().add(id);
    }
    return couponRequestDTO;
  }
}
