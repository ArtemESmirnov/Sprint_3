import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import pojos.CreateCourierBody;
import pojos.LoginCourierBody;

import static io.restassured.RestAssured.*;

public class CreateCourierTests {
    String login = "uniqueCourierLogin";
    String password = "courierPassword";
    String firstName = "courierName";
    Response response;

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }

    @Test
    public void createCourierShouldBePossibleTest(){
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
    public void createTwoCouriersWithEqualLoginsShouldFailTest(){

    }

    @After
    public void deleteCourier(){
        if(response.statusCode() == 201){
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
