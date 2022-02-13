package tests;

import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import utils.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Execution(ExecutionMode.CONCURRENT)
public class TestStore extends BaseTest {
    JSONObject responseBody;

    @Test
    @DisplayName("Returns pet inventories by status")
    void testInventory() {
        responseBody = getResponseBody("GET", "/store/inventory");
        assertNotNull(responseBody.get("sold").toString());
        assertNotNull(responseBody.get("available").toString());
    }

    @Nested
    class TestOrder {
        int petId = 1;
        int quantity = 100;
        String order_id;
        String shipDate = "2022-01-15T01:31:14.501+0000";
        String status = "placed";
        Boolean complete = true;

        @Test
        @DisplayName("Place an order for a pet")
        void testCreate() {
            requestParams.put("petId", petId);
            requestParams.put("quantity", quantity);
            requestParams.put("shipDate", shipDate);
            requestParams.put("status", status);
            requestParams.put("complete", complete);
            responseBody = getResponseBody("POST","/store/order");

            order_id = responseBody.get("id").toString();
            Logger.info(response.getBody().asString());
        }

        @Test
        @DisplayName("Find purchase order by ID")
        void testGet() {
            testCreate();
            responseBody = getResponseBody("GET","/store/order/"+order_id);
            Logger.info(response.getBody().asString());
            assertEquals(order_id, responseBody.get("id").toString());
            assertEquals(petId, responseBody.get("petId"));
            assertEquals(quantity, responseBody.get("quantity"));
            assertEquals(shipDate, responseBody.get("shipDate").toString());
            assertEquals(status, responseBody.get("status").toString());
            assertEquals(complete, responseBody.get("complete"));
        }

        @Test
        @DisplayName("Delete purchase order by ID")
        void testDelete() {
            testCreate();
            responseBody = getResponseBody("DELETE","/store/order/"+order_id);
            assertEquals("200", responseBody.get("code").toString());
            assertEquals(order_id, responseBody.get("message").toString());
        }
    }
}
