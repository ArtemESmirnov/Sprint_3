package requestgenerators;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import pojos.CreateCourierBody;

import static io.restassured.RestAssured.given;

public class CreateCourierRequestGenerator extends Request{
    public CreateCourierRequestGenerator(RequestSpecification requestSpecification) {
        super(requestSpecification);
    }

    public static Response createCourierRequest(CreateCourierBody createCourierBody, String apiPath){
        return given()
                .spec(setRequestSpecification())
                .header("Content-type", "application/json")
                .and()
                .body(createCourierBody)
                .when()
                .post(apiPath)
                .then()
                .extract().response();
    }
}
