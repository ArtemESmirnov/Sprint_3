import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import pojos.CreateCourierBody;
import pojos.LoginCourierBody;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;
import static requestgenerators.CreateCourierRequestGenerator.createCourierRequest;
import static requestgenerators.DeleteCourierRequestGenerator.deleteCourierRequest;
import static requestgenerators.LoginCourierRequestGenerator.loginCourierRequest;

public class CreateCourierTest {
    final static String courierApiPath = "/api/v1/courier";
    final static String courierLoginApiPath = "/api/v1/courier/login";
    final static String courierDeleteApiPath = "/api/v1/courier/{id}";
    final String OK_RESPONSE_STRING = "{\"ok\":true}";
    final String EQUAL_LOGINS_RESPONSE_STRING = "Этот логин уже используется. Попробуйте другой.";
    final String NO_LOGIN_OR_PASSWORD_RESPONSE_STRING = "Недостаточно данных для создания учетной записи";
    String login = "uniqueCourierLogin";
    String password = "courierPassword";
    String firstName = "courierName";
    Response response;

    @Test
    @DisplayName("Проверка кода ответа при попытке создания курьера")
    public void createCourierShouldBePossibleStatusCodeTest(){
        CreateCourierBody courier = new CreateCourierBody(login, password, firstName);

        response = createCourierRequest(courier, courierApiPath);
        assertEquals(SC_CREATED, response.statusCode());
    }

    @Test
    @DisplayName("Проверка тела ответа при попытке создания курьера")
    public void createCourierShouldBePossibleResponseBodyTest(){
        CreateCourierBody courier = new CreateCourierBody(login, password, firstName);

        response = createCourierRequest(courier, courierApiPath);
        assertEquals(OK_RESPONSE_STRING, response.getBody().asString());
    }

    @Test
    @DisplayName("Проверка кода ответа при попытке создания курьера с уже существующим логином")
    public void createTwoCouriersWithEqualLoginsShouldFailStatusCodeTest(){
        CreateCourierBody courier1 = new CreateCourierBody(login, password, firstName);
        CreateCourierBody courier2 = new CreateCourierBody(login, "password", "firstName");

        response = createCourierRequest(courier1, courierApiPath);
        assertEquals(SC_CONFLICT, createCourierRequest(courier2, courierApiPath).statusCode());
    }

    @Test
    @DisplayName("Проверка тела ответа при попытке создания курьера с уже существующим логином")
    public void createTwoCouriersWithEqualLoginsShouldFailResponseBodyTest(){
        CreateCourierBody courier1 = new CreateCourierBody(login, password, firstName);
        CreateCourierBody courier2 = new CreateCourierBody(login, "password", "firstName");

        response = createCourierRequest(courier1, courierApiPath);
        assertEquals(EQUAL_LOGINS_RESPONSE_STRING, createCourierRequest(courier2, courierApiPath).path("message"));
    }

    @Test
    @DisplayName("Проверка кода ответа при попытке создания курьера без указания логина")
    public void createCourierWithoutLoginShouldFailStatusCodeTest(){
        CreateCourierBody courier = new CreateCourierBody("", password, firstName);

        response = createCourierRequest(courier, courierApiPath);
        assertEquals(SC_BAD_REQUEST, response.statusCode());
    }

    @Test
    @DisplayName("Проверка тела ответа при попытке создания курьера без указания логина")
    public void createCourierWithoutLoginShouldFailResponseBodyTest(){
        CreateCourierBody courier = new CreateCourierBody("", password, firstName);

        response = createCourierRequest(courier, courierApiPath);
        assertEquals(NO_LOGIN_OR_PASSWORD_RESPONSE_STRING, response.path("message"));
    }

    @Test
    @DisplayName("Проверка кода ответа при попытке создания курьера без указания пароля")
    public void createCourierWithoutPasswordShouldFailStatusCodeTest(){
        CreateCourierBody courier = new CreateCourierBody(login, "", firstName);

        response = createCourierRequest(courier, courierApiPath);
        assertEquals(SC_BAD_REQUEST, response.statusCode());
    }

    @Test
    @DisplayName("Проверка тела ответа при попытке создания курьера без указания пароля")
    public void createCourierWithoutPasswordShouldFailResponseBodyTest(){
        CreateCourierBody courier = new CreateCourierBody(login, "", firstName);

        response = createCourierRequest(courier, courierApiPath);
        assertEquals(NO_LOGIN_OR_PASSWORD_RESPONSE_STRING, response.path("message"));
    }

    @Test
    @DisplayName("Проверка кода ответа при попытке создания курьера без указания имени")
    public void createCourierWithoutFirstNameShouldBePossibleStatusCodeTest(){
        CreateCourierBody courier = new CreateCourierBody(login, password, "");

        response = createCourierRequest(courier, courierApiPath);
        assertEquals(SC_CREATED, response.statusCode());
    }

    @Test
    @DisplayName("Проверка тела ответа при попытке создания курьера без указания имени")
    public void createCourierWithoutFirstNameShouldBePossibleResponseBodyTest(){
        CreateCourierBody courier = new CreateCourierBody(login, password, "");

        response = createCourierRequest(courier, courierApiPath);
        assertEquals(OK_RESPONSE_STRING, response.getBody().asString());
    }

    @After
    public void deleteCourier(){
        if(response.statusCode() == SC_CREATED || response.statusCode() == SC_CONFLICT){
            LoginCourierBody loginCourierBody = new LoginCourierBody(login, password);

            Integer id = loginCourierRequest(loginCourierBody, courierLoginApiPath).path("id");
            assertEquals(SC_OK, deleteCourierRequest(id, courierDeleteApiPath).statusCode());
        }
    }
}
