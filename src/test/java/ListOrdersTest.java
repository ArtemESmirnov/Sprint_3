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

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertTrue;

public class ListOrdersTest {
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
    private String courierLogin = "uniqueCourierLogin";
    private String courierPassword = "courierPassword";
    private String courierName = "courierName";
    private int courierId;
    Response createOrderResponse;

    @Before
    public void setUp(){
        baseURI = "http://qa-scooter.praktikum-services.ru/";
        CreateOrderBody createOrderBody = new CreateOrderBody(firstName, lastName, address, metroStation,
                phone, rentTime, deliveryDate, comment, color);
        CreateCourierBody createCourierBody = new CreateCourierBody(courierLogin, courierPassword, courierName);
        LoginCourierBody loginCourierBody = new LoginCourierBody(courierLogin, courierPassword);
        createOrderResponse = given()
                .header("Content-type", "application/json")
                .and()
                .body(createOrderBody)
                .when()
                .post("/api/v1/orders")
                .then()
                .extract().response();
        trackOrderResponse = given()
                .header("Content-type", "application/json")
                .queryParam("t", (int)createOrderResponse.path("track"))
                .get("/api/v1/orders/track")
                .body().as(TrackOrderResponse.class);
        given()
                .header("Content-type", "application/json")
                .and()
                .body(createCourierBody)
                .when()
                .post("/api/v1/courier");
        courierId = given()
                .header("Content-type", "application/json")
                .and()
                .body(loginCourierBody)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .extract()
                .path("id");
        given()
                .header("Content-type", "application/json")
                .queryParam("courierId", courierId)
                .put("/api/v1/orders/accept/{id}", trackOrderResponse.getOrder().getId());
    }

    @Test
    @DisplayName("Проверка кода ответа при запросе списка заказов")
    public void ListOrdersStatusCodeTest() {
        given()
                .header("Content-type", "application/json")
                .when()
                .get("/api/v1/orders")
                .then()
                .assertThat().statusCode(200);
    }

    @Test
    @DisplayName("Проверка наличия созданного заказа в возвращаемом списке заказов")
    public void ListOrdersResponseBodyTest(){
        boolean isTrackFound = false;
        int i = 0;
        OrderListResponse orderListResponse = given()
                .header("Content-type", "application/json")
                .when()
                .queryParam("courierId", courierId)
                .get("/api/v1/orders")
                .body().as(OrderListResponse.class);

        while (!isTrackFound && i < orderListResponse.getOrders().size()){
            if(orderListResponse.getOrders().get(i).getTrack() == (int)createOrderResponse.path("track"))
                isTrackFound = true;
            i++;
        }
        assertTrue(isTrackFound);
    }

    @After
    public void cleanUp(){
        given()
                .header("Content-type", "application/json")
                .when()
                .delete("/api/v1/courier/{id}", courierId);
    }
}
