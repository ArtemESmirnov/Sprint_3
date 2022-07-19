import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import pojos.CreateCourierBody;
import pojos.LoginCourierBody;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

public class LoginCourierTest{
    final String ACCOUNT_NOT_FOUND_RESPONSE_STRING = "{\"message\":\"Учетная запись не найдена\"}";
    final String NO_LOGIN_OR_PASSWORD_RESPONSE_STRING = "{\"message\":\"Недостаточно данных для входа\"}";
    String login = "uniqueCourierLogin";
    String password = "courierPassword";
    String firstName = "courierName";
    String wrongLogin = "wrongLogin";
    String wrongPassword = "wrongPassword";
    Response response;

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
        CreateCourierBody courier = new CreateCourierBody(login, password, firstName);

        given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then().statusCode(201);
    }

    @Test
    @DisplayName("Проверка кода ответа при попытке авторизации курьера")
    public void loginCourierShouldBePossibleStatusCodeTest(){
        LoginCourierBody courier = new LoginCourierBody(login, password);

        given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier/login")
                .then().statusCode(200);
    }

    @Test
    @DisplayName("Проверка тела ответа при попытке авторизации курьера")
    public void loginCourierShouldBePossibleResponseBodyTest(){
        LoginCourierBody courier = new LoginCourierBody(login, password);

        response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .assertThat().body("id", notNullValue())
                .extract().response();
    }

    @Test
    @DisplayName("Проверка кода ответа при попытке авторизации курьера без указания логина")
    public void loginCourierWithoutLoginShouldFailStatusCodeTest(){
        LoginCourierBody courier = new LoginCourierBody("", password);

        given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier/login")
                .then().statusCode(400);
    }

    @Test
    @DisplayName("Проверка тела ответа при попытке авторизации курьера без указания логина")
    public void loginCourierWithoutLoginShouldFailResponseBodyTest(){
        LoginCourierBody courier = new LoginCourierBody("", password);

        response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .extract().response();
        assertEquals(NO_LOGIN_OR_PASSWORD_RESPONSE_STRING, response.getBody().asString());
    }

    @Test
    @DisplayName("Проверка кода ответа при попытке авторизации курьера без указания пароля")
    public void loginCourierWithoutPasswordShouldFailStatusCodeTest(){
        LoginCourierBody courier = new LoginCourierBody(login, "");

        given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier/login")
                .then().statusCode(400);
    }

    @Test
    @DisplayName("Проверка тела ответа при попытке авторизации курьера без указания пароля")
    public void loginCourierWithoutPasswordShouldFailResponseBodyTest(){
        LoginCourierBody courier = new LoginCourierBody(login, "");

        response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .extract().response();
        assertEquals(NO_LOGIN_OR_PASSWORD_RESPONSE_STRING, response.getBody().asString());
    }

    @Test
    @DisplayName("Проверка кода ответа при попытке авторизации курьера с использованием несуществующего логина")
    public void loginCourierWrongLoginShouldFailStatusCodeTest(){
        LoginCourierBody courier = new LoginCourierBody(wrongLogin, password);

        given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier/login")
                .then().statusCode(404);
    }

    @Test
    @DisplayName("Проверка тела ответа при попытке авторизации курьера с использованием несуществующего логина")
    public void loginCourierWrongLoginShouldFailResponseBodyTest(){
        LoginCourierBody courier = new LoginCourierBody(wrongLogin, password);

        response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .extract().response();
        assertEquals(ACCOUNT_NOT_FOUND_RESPONSE_STRING, response.getBody().asString());
    }

    @Test
    @DisplayName("Проверка кода ответа при попытке авторизации курьера с использованием неправильного пароля")
    public void loginCourierWrongPasswordShouldFailStatusCodeTest(){
        LoginCourierBody courier = new LoginCourierBody(login, wrongPassword);

        given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier/login")
                .then().statusCode(404);
    }

    @Test
    @DisplayName("Проверка тела ответа при попытке авторизации курьера с использованием неправильного пароля")
    public void loginCourierWrongPasswordShouldFailResponseBodyTest(){
        LoginCourierBody courier = new LoginCourierBody(login, wrongPassword);

        response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .extract().response();
        assertEquals(ACCOUNT_NOT_FOUND_RESPONSE_STRING, response.getBody().asString());
    }

    @Test
    @DisplayName("Проверка кода ответа при попытке авторизации курьера с использованием неправильного логина и пароля")
    public void loginCourierWrongLoginAndPasswordShouldFailStatusCodeTest(){
        LoginCourierBody courier = new LoginCourierBody(wrongLogin, wrongPassword);

        given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier/login")
                .then().statusCode(404);
    }

    @Test
    @DisplayName("Проверка тела ответа при попытке авторизации курьера с использованием неправильного логина и пароля")
    public void loginCourierWrongLoginAndPasswordShouldFailResponseBodyTest(){
        LoginCourierBody courier = new LoginCourierBody(wrongLogin, wrongPassword);

        response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .extract().response();
        assertEquals(ACCOUNT_NOT_FOUND_RESPONSE_STRING, response.getBody().asString());
    }

    @After
    public void deleteCourier(){
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
