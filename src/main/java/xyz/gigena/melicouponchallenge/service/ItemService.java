package xyz.gigena.melicouponchallenge.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.gigena.melicouponchallenge.dto.CouponDTO;
import xyz.gigena.melicouponchallenge.dto.ItemDTO;
import xyz.gigena.melicouponchallenge.dto.ItemFoundDTO;
import xyz.gigena.melicouponchallenge.dto.ItemNotFoundDTO;
import xyz.gigena.melicouponchallenge.dto.ItemSetDTO;

public class ItemService {
  private HttpClient client;

  private Logger logger = LoggerFactory.getLogger(ItemService.class);

  public ItemService(HttpClient cHttpClient) {
    client = cHttpClient;
  }

  public ItemSetDTO getNewItems(CouponDTO couponDTO) {
    ItemSetDTO itemSetDTO = new ItemSetDTO(couponDTO.getItemIds().size());
    Set<ItemDTO> itemResponseDTOs = new HashSet<ItemDTO>();
    try {
      HttpResponse<String> response = getItemsFromAPI(couponDTO);

      if (response.statusCode() >= 200 && response.statusCode() <= 299) {
        logger.info("Response from API: " + response.body());
        parseItems(itemSetDTO, itemResponseDTOs, response);
      } else {
        logger.warn("Bad status code: " + response.statusCode());
        itemSetDTO.setNotFound(couponDTO.getItemIds().size());
      }
    } catch (JsonProcessingException e) {
      logger.error("Jackson could not parse the response body!");
      itemSetDTO.setNotFound(couponDTO.getItemIds().size());
    } catch (IOException e) {
      logger.error("IO Exception occurred");
      itemSetDTO.setNotFound(couponDTO.getItemIds().size());
    } catch (InterruptedException e) {
      logger.error("Interrupted Exception occurred");
      itemSetDTO.setNotFound(couponDTO.getItemIds().size());
    }
    itemSetDTO.setItems(itemResponseDTOs);
    return itemSetDTO;
  }

  private HttpResponse<String> getItemsFromAPI(CouponDTO couponDTO)
      throws IOException, InterruptedException {
    String uriString = "https://api.mercadolibre.com/items?ids=";
    for (String item : couponDTO.getItemIds()) {
      uriString += item + ",";
    }
    uriString = uriString.substring(0, uriString.length() - 1);
    URI uri = URI.create(uriString);
    HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
    return client.send(request, BodyHandlers.ofString());
  }

  private void parseItems(
      ItemSetDTO itemSetDTO, Set<ItemDTO> itemResponseDTOs, HttpResponse<String> response)
      throws JsonProcessingException {
    JsonNode node = new ObjectMapper().readTree(response.body());
    for (int nodeIndex = 0; nodeIndex < node.size(); nodeIndex++) {
      ObjectNode itemNode = (ObjectNode) node.get(nodeIndex);
      int itemCode = itemNode.get("code").asInt();
      if (itemCode == 404) {
        itemSetDTO.setNotFound(itemSetDTO.getNotFound() + 1);
        itemResponseDTOs.add(new ItemNotFoundDTO(itemNode.get("body").get("id").asText()));
      } else {
        itemResponseDTOs.add(
            new ItemFoundDTO(
                itemNode.get("body").get("id").asText(),
                itemNode.get("body").get("price").asDouble()));
      }
    }
  }
}
