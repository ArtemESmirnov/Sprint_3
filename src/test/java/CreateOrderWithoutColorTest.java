import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import pojos.CreateOrderBodyWithoutColor;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.notNullValue;


public class CreateOrderWithoutColorTest {
    private final String firstName = "Naruto";
    private final String lastName = "Uchiha";
    private final String address = "Konoha, 142 apt.";
    private final String metroStation = "4";
    private final String phone = "+7 800 355 35 35";
    private final int rentTime = 5;
    private final String deliveryDate = "2020-06-06";
    private final String comment = "Saske, come back to Konoha";
    Response response;

    @Before
    public void setUp(){
        baseURI = "http://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("Проверка кода ответа при создании заказа без поля color")
    public void createOrderWithoutColorStatusCodeTest(){
        CreateOrderBodyWithoutColor createOrderBody = new CreateOrderBodyWithoutColor(firstName, lastName, address, metroStation,
                phone, rentTime, deliveryDate, comment);
        response = given()
                .header("Content-type", "application/json")
                .and()
                .body(createOrderBody)
                .when()
                .post("/api/v1/orders")
                .then()
                .statusCode(201)
                .and()
                .extract().response();
    }

    @Test
    @DisplayName("Проверка тела ответа при создании заказа без поля color")
    public void createOrderWithoutColorResponseBodyTest(){
        CreateOrderBodyWithoutColor createOrderBody = new CreateOrderBodyWithoutColor(firstName, lastName, address, metroStation,
                phone, rentTime, deliveryDate, comment);
        response = given()
                .header("Content-type", "application/json")
                .and()
                .body(createOrderBody)
                .when()
                .post("/api/v1/orders")
                .then()
                .assertThat().body("track", notNullValue())
                .and()
                .extract().response();
    }

    @After
    public void cancelOrder(){
        given()
                .header("Content-type", "application/json")
                .and()
                .queryParam("track", response.path("track").toString())
                .put("/api/v1/orders/cancel")
                .then().statusCode(200);
    }
}
