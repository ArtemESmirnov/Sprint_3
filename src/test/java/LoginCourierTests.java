import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import pojos.CreateCourierBody;
import pojos.LoginCourierBody;

import java.util.Objects;

import static io.restassured.RestAssured.*;
import static org.junit.Assert.*;

public class LoginCourierTests {
    final String ACCOUNT_NOT_FOUND_RESPONSE_STRING = "{\"message\":\"Учетная запись не найдена\"}";
    final String NO_LOGIN_OR_PASSWORD_RESPONSE_STRING = "{\"message\":\"Недостаточно данных для входа\"}";
    String login = "uniqueCourierLogin";
    String password = "courierPassword";
    String firstName = "courierName";
    String wrongLogin = "wrongLogin";
    String wrongPassword = "wrongPassword";
    Response response;


    public static String getJsonPath(Response response, String key) {
        String complete = response.asString();
        JsonPath js = new JsonPath(complete);
        try{
            if(Objects.equals(js.get(key).toString(), ""))
                return null;
            return js.get(key).toString();
        }
        catch (NullPointerException exception){
            return null;
        }
    }

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
    public void loginCourierShouldBePossibleResponseBodyTest(){
        LoginCourierBody courier = new LoginCourierBody(login, password);

        response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .extract().response();
        assertNotNull(getJsonPath(response, "id"));
    }

    @Test
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
