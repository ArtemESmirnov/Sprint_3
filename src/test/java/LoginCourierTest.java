import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import pojos.CreateCourierBody;
import pojos.LoginCourierBody;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;
import static requestgenerators.CreateCourierRequestGenerator.createCourierRequest;
import static requestgenerators.DeleteCourierRequestGenerator.deleteCourierRequest;
import static requestgenerators.LoginCourierRequestGenerator.loginCourierRequest;

public class LoginCourierTest{
    final static String courierLoginApiPath = "/api/v1/courier/login";
    final static String courierApiPath = "/api/v1/courier";
    final static String courierDeleteApiPath = "/api/v1/courier/{id}";
    final String ACCOUNT_NOT_FOUND_RESPONSE_STRING = "Учетная запись не найдена";
    final String NO_LOGIN_OR_PASSWORD_RESPONSE_STRING = "Недостаточно данных для входа";
    String login = "uniqueCourierLogin";
    String password = "courierPassword";
    String firstName = "courierName";
    String wrongLogin = "wrongLogin";
    String wrongPassword = "wrongPassword";
    Response response;

    @Before
    public void setUp() {
        CreateCourierBody courier = new CreateCourierBody(login, password, firstName);

        assertEquals(SC_CREATED, createCourierRequest(courier, courierApiPath).statusCode());
    }

    @Test
    @DisplayName("Проверка кода ответа при попытке авторизации курьера")
    public void loginCourierShouldBePossibleStatusCodeTest(){
        LoginCourierBody courier = new LoginCourierBody(login, password);

        assertEquals(SC_OK, loginCourierRequest(courier, courierLoginApiPath).statusCode());
    }

    @Test
    @DisplayName("Проверка тела ответа при попытке авторизации курьера")
    public void loginCourierShouldBePossibleResponseBodyTest(){
        LoginCourierBody courier = new LoginCourierBody(login, password);

        response = loginCourierRequest(courier, courierLoginApiPath);
        assertNotNull(response.path("id"));
        assertNotEquals("", response.path("id"));
    }

    @Test
    @DisplayName("Проверка кода ответа при попытке авторизации курьера без указания логина")
    public void loginCourierWithoutLoginShouldFailStatusCodeTest(){
        LoginCourierBody courier = new LoginCourierBody("", password);

        assertEquals(SC_BAD_REQUEST, loginCourierRequest(courier, courierLoginApiPath).statusCode());
    }

    @Test
    @DisplayName("Проверка тела ответа при попытке авторизации курьера без указания логина")
    public void loginCourierWithoutLoginShouldFailResponseBodyTest(){
        LoginCourierBody courier = new LoginCourierBody("", password);

        assertEquals(NO_LOGIN_OR_PASSWORD_RESPONSE_STRING, loginCourierRequest(courier, courierLoginApiPath).path("message"));
    }

    @Test
    @DisplayName("Проверка кода ответа при попытке авторизации курьера без указания пароля")
    public void loginCourierWithoutPasswordShouldFailStatusCodeTest(){
        LoginCourierBody courier = new LoginCourierBody(login, "");

        assertEquals(SC_BAD_REQUEST, loginCourierRequest(courier, courierLoginApiPath).statusCode());
    }

    @Test
    @DisplayName("Проверка тела ответа при попытке авторизации курьера без указания пароля")
    public void loginCourierWithoutPasswordShouldFailResponseBodyTest(){
        LoginCourierBody courier = new LoginCourierBody(login, "");

        assertEquals(NO_LOGIN_OR_PASSWORD_RESPONSE_STRING, loginCourierRequest(courier, courierLoginApiPath).path("message"));
    }

    @Test
    @DisplayName("Проверка кода ответа при попытке авторизации курьера с использованием несуществующего логина")
    public void loginCourierWrongLoginShouldFailStatusCodeTest(){
        LoginCourierBody courier = new LoginCourierBody(wrongLogin, password);

        assertEquals(SC_NOT_FOUND, loginCourierRequest(courier, courierLoginApiPath).statusCode());
    }

    @Test
    @DisplayName("Проверка тела ответа при попытке авторизации курьера с использованием несуществующего логина")
    public void loginCourierWrongLoginShouldFailResponseBodyTest(){
        LoginCourierBody courier = new LoginCourierBody(wrongLogin, password);

        assertEquals(ACCOUNT_NOT_FOUND_RESPONSE_STRING, loginCourierRequest(courier, courierLoginApiPath).path("message"));
    }

    @Test
    @DisplayName("Проверка кода ответа при попытке авторизации курьера с использованием неправильного пароля")
    public void loginCourierWrongPasswordShouldFailStatusCodeTest(){
        LoginCourierBody courier = new LoginCourierBody(login, wrongPassword);

        assertEquals(SC_NOT_FOUND, loginCourierRequest(courier, courierLoginApiPath).statusCode());
    }

    @Test
    @DisplayName("Проверка тела ответа при попытке авторизации курьера с использованием неправильного пароля")
    public void loginCourierWrongPasswordShouldFailResponseBodyTest(){
        LoginCourierBody courier = new LoginCourierBody(login, wrongPassword);

        assertEquals(ACCOUNT_NOT_FOUND_RESPONSE_STRING, loginCourierRequest(courier, courierLoginApiPath).path("message"));
    }

    @Test
    @DisplayName("Проверка кода ответа при попытке авторизации курьера с использованием неправильного логина и пароля")
    public void loginCourierWrongLoginAndPasswordShouldFailStatusCodeTest(){
        LoginCourierBody courier = new LoginCourierBody(wrongLogin, wrongPassword);

        assertEquals(SC_NOT_FOUND, loginCourierRequest(courier, courierLoginApiPath).statusCode());
    }

    @Test
    @DisplayName("Проверка тела ответа при попытке авторизации курьера с использованием неправильного логина и пароля")
    public void loginCourierWrongLoginAndPasswordShouldFailResponseBodyTest(){
        LoginCourierBody courier = new LoginCourierBody(wrongLogin, wrongPassword);

        assertEquals(ACCOUNT_NOT_FOUND_RESPONSE_STRING, loginCourierRequest(courier, courierLoginApiPath).path("message"));
    }

    @After
    public void deleteCourier(){
        LoginCourierBody loginCourierBody = new LoginCourierBody(login, password);

        Integer id = loginCourierRequest(loginCourierBody, courierLoginApiPath).path("id");
        assertEquals(SC_OK, deleteCourierRequest(id, courierDeleteApiPath).statusCode());
    }
}
