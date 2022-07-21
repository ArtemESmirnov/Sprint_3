import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojos.CreateCourierBody;
import pojos.CreateOrderBody;
import pojos.LoginCourierBody;
import pojos.responses.OrderListResponse;
import pojos.responses.TrackOrderResponse;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static requestgenerators.AcceptOrderRequestGenerator.acceptOrderRequest;
import static requestgenerators.CreateCourierRequestGenerator.createCourierRequest;
import static requestgenerators.CreateOrderRequestGenerator.createOrderRequest;
import static requestgenerators.DeleteCourierRequestGenerator.deleteCourierRequest;
import static requestgenerators.ListOrdersRequestGenerator.listOrdersRequest;
import static requestgenerators.LoginCourierRequestGenerator.loginCourierRequest;
import static requestgenerators.TrackOrderRequestGenerator.trackOrderRequest;

public class ListOrdersTest {
    final static String ordersApiPath = "/api/v1/orders";
    final static String ordersTrackApiPath = "/api/v1/orders/track";
    final static String courierApiPath = "/api/v1/courier";
    final static String courierLoginApiPath = "/api/v1/courier/login";
    final static String ordersAcceptApiPath = "/api/v1/orders/accept/{id}";
    final static String courierDeleteApiPath = "/api/v1/courier/{id}";
    private final String firstName = "Naruto";
    private final String lastName = "Uchiha";
    private final String address = "Konoha, 142 apt.";
    private final String metroStation = "4";
    private final String phone = "+7 800 355 35 35";
    private final int rentTime = 5;
    private final String deliveryDate = "2020-06-06";
    private final String comment = "Saske, come back to Konoha";
    private final String[] color = {"BLACK", "GREY"};
    private TrackOrderResponse trackOrderResponse;
    private final String courierLogin = "uniqueCourierLogin";
    private final String courierPassword = "courierPassword";
    private final String courierName = "courierName";
    private int courierId;
    Response createOrderResponse;

    @Before
    public void setUp(){
        CreateOrderBody createOrderBody = new CreateOrderBody(firstName, lastName, address, metroStation,
                phone, rentTime, deliveryDate, comment, color);
        CreateCourierBody createCourierBody = new CreateCourierBody(courierLogin, courierPassword, courierName);
        LoginCourierBody loginCourierBody = new LoginCourierBody(courierLogin, courierPassword);

        createOrderResponse = createOrderRequest(createOrderBody, ordersApiPath);
        trackOrderResponse = trackOrderRequest(createOrderResponse.path("track"),
                ordersTrackApiPath).as(TrackOrderResponse.class);
        createCourierRequest(createCourierBody, courierApiPath);
        courierId = loginCourierRequest(loginCourierBody, courierLoginApiPath).path("id");
        acceptOrderRequest(trackOrderResponse.getOrder().getId(), courierId, ordersAcceptApiPath);
    }

    @Test
    @DisplayName("Проверка кода ответа при запросе списка заказов")
    public void ListOrdersStatusCodeTest() {
        assertEquals(SC_OK, listOrdersRequest(ordersApiPath).getStatusCode());
    }

    @Test
    @DisplayName("Проверка наличия созданного заказа в возвращаемом списке заказов")
    public void ListOrdersResponseBodyTest(){
        boolean isTrackFound = false;
        int i = 0;
        OrderListResponse orderListResponse = listOrdersRequest(ordersApiPath, courierId).as(OrderListResponse.class);

        while (!isTrackFound && i < orderListResponse.getOrders().size()){
            if(orderListResponse.getOrders().get(i).getTrack() == (int)createOrderResponse.path("track"))
                isTrackFound = true;
            i++;
        }
        assertTrue(isTrackFound);
    }

    @After
    public void cleanUp(){
        deleteCourierRequest(courierId, courierDeleteApiPath);
    }
}
