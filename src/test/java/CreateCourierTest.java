import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import pojos.CreateCourierBody;
import pojos.LoginCourierBody;

import static io.restassured.RestAssured.*;
import static org.junit.Assert.*;

public class CreateCourierTest {
    final String OK_RESPONSE_STRING = "{\"ok\":true}";
    final String EQUAL_LOGINS_RESPONSE_STRING = "{\"message\":\"Этот логин уже используется\"}";
    final String NO_LOGIN_OR_PASSWORD_RESPONSE_STRING = "{\"message\":\"Недостаточно данных для создания учетной записи\"}";
    String login = "uniqueCourierLogin";
    String password = "courierPassword";
    String firstName = "courierName";
    Response response;

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("Проверка кода ответа при попытке создания курьера")
    public void createCourierShouldBePossibleStatusCodeTest(){
        CreateCourierBody courier = new CreateCourierBody(login, password, firstName);

        response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then().statusCode(201)
                .extract().response();
    }

    @Test
    @DisplayName("Проверка тела ответа при попытке создания курьера")
    public void createCourierShouldBePossibleResponseBodyTest(){
        CreateCourierBody courier = new CreateCourierBody(login, password, firstName);

        response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then()
                .extract().response();
        assertEquals(OK_RESPONSE_STRING, response.getBody().asString());
    }

    @Test
    @DisplayName("Проверка кода ответа при попытке создания курьера с уже существующим логином")
    public void createTwoCouriersWithEqualLoginsShouldFailStatusCodeTest(){
        CreateCourierBody courier1 = new CreateCourierBody(login, password, firstName);
        CreateCourierBody courier2 = new CreateCourierBody(login, "password", "firstName");

        response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier1)
                .when()
                .post("/api/v1/courier")
                .then()
                .extract().response();
        given()
                .header("Content-type", "application/json")
                .and()
                .body(courier2)
                .when()
                .post("/api/v1/courier")
                .then().statusCode(409);
    }

    @Test
    @DisplayName("Проверка тела ответа при попытке создания курьера с уже существующим логином")
    public void createTwoCouriersWithEqualLoginsShouldFailResponseBodyTest(){
        CreateCourierBody courier1 = new CreateCourierBody(login, password, firstName);
        CreateCourierBody courier2 = new CreateCourierBody(login, "password", "firstName");

        response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier1)
                .when()
                .post("/api/v1/courier")
                .then()
                .extract().response();
        Response response2 = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier2)
                .when()
                .post("/api/v1/courier")
                .then()
                .extract().response();
        assertEquals(EQUAL_LOGINS_RESPONSE_STRING, response2.getBody().asString());
    }

    @Test
    @DisplayName("Проверка кода ответа при попытке создания курьера без указания логина")
    public void createCourierWithoutLoginShouldFailStatusCodeTest(){
        CreateCourierBody courier = new CreateCourierBody("", password, firstName);

        response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(400)
                .extract().response();
    }

    @Test
    @DisplayName("Проверка тела ответа при попытке создания курьера без указания логина")
    public void createCourierWithoutLoginShouldFailResponseBodyTest(){
        CreateCourierBody courier = new CreateCourierBody("", password, firstName);

        response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then()
                .extract().response();
        assertEquals(NO_LOGIN_OR_PASSWORD_RESPONSE_STRING, response.getBody().asString());
    }

    @Test
    @DisplayName("Проверка кода ответа при попытке создания курьера без указания пароля")
    public void createCourierWithoutPasswordShouldFailStatusCodeTest(){
        CreateCourierBody courier = new CreateCourierBody(login, "", firstName);

        response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(400)
                .extract().response();
    }

    @Test
    @DisplayName("Проверка тела ответа при попытке создания курьера без указания пароля")
    public void createCourierWithoutPasswordShouldFailResponseBodyTest(){
        CreateCourierBody courier = new CreateCourierBody(login, "", firstName);

        response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then()
                .extract().response();
        assertEquals(NO_LOGIN_OR_PASSWORD_RESPONSE_STRING, response.getBody().asString());
    }

    @Test
    @DisplayName("Проверка кода ответа при попытке создания курьера без указания имени")
    public void createCourierWithoutFirstNameShouldBePossibleStatusCodeTest(){
        CreateCourierBody courier = new CreateCourierBody(login, password, "");

        response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(201)
                .extract().response();
    }

    @Test
    @DisplayName("Проверка тела ответа при попытке создания курьера без указания имени")
    public void createCourierWithoutFirstNameShouldBePossibleResponseBodyTest(){
        CreateCourierBody courier = new CreateCourierBody(login, password, "");

        response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then()
                .extract().response();
        assertEquals(OK_RESPONSE_STRING, response.getBody().asString());
    }

    @After
    public void deleteCourier(){
        if(response.statusCode() == 201 || response.statusCode() == 409){
            LoginCourierBody loginCourierBody = new LoginCourierBody(login, password);
            Integer id = given()
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
                    .when()
                    .delete("/api/v1/courier/{id}", id.toString())
                    .then().statusCode(200);
        }
    }
}
