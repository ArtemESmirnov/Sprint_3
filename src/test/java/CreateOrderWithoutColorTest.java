import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import pojos.CreateOrderBody;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;
import static requestgenerators.CancelOrderRequestGenerator.cancelOrderRequest;
import static requestgenerators.CreateOrderRequestGenerator.createOrderRequest;

public class CreateOrderWithoutColorTest {
    final static String ordersApiPath = "/api/v1/orders";
    final static String ordersCancelApiPath = "/api/v1/orders/cancel";
    private final String firstName = "Naruto";
    private final String lastName = "Uchiha";
    private final String address = "Konoha, 142 apt.";
    private final String metroStation = "4";
    private final String phone = "+7 800 355 35 35";
    private final int rentTime = 5;
    private final String deliveryDate = "2020-06-06";
    private final String comment = "Saske, come back to Konoha";
    Response response;

    @Test
    @DisplayName("Проверка кода ответа при создании заказа без поля color")
    public void createOrderWithoutColorStatusCodeTest(){
        CreateOrderBody createOrderBody = new CreateOrderBody(firstName, lastName, address, metroStation,
                phone, rentTime, deliveryDate, comment);

        response = createOrderRequest(createOrderBody, ordersApiPath);
        assertEquals(SC_CREATED, response.getStatusCode());
    }

    @Test
    @DisplayName("Проверка тела ответа при создании заказа без поля color")
    public void createOrderWithoutColorResponseBodyTest(){
        CreateOrderBody createOrderBody = new CreateOrderBody(firstName, lastName, address, metroStation,
                phone, rentTime, deliveryDate, comment);

        response = createOrderRequest(createOrderBody, ordersApiPath);
        assertNotNull(response.path("track"));
        assertNotEquals("", response.path("track"));
    }

    @After
    public void cancelOrder(){
        assertEquals(SC_OK,
                cancelOrderRequest(response.path("track").toString(), ordersCancelApiPath).getStatusCode());
    }
}
