package requestgenerators;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import pojos.LoginCourierBody;

import static io.restassured.RestAssured.given;

public class LoginCourierRequestGenerator extends Request {
    public LoginCourierRequestGenerator(RequestSpecification requestSpecification) {
        super(requestSpecification);
    }

    public static Response loginCourierRequest(LoginCourierBody loginCourierBody, String apiPath){
        return given()
                .spec(setRequestSpecification())
                .header("Content-type", "application/json")
                .and()
                .body(loginCourierBody)
                .when()
                .post(apiPath);
    }
}
