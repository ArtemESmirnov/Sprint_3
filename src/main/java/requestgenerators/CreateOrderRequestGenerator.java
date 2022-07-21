package requestgenerators;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import pojos.CreateOrderBody;

import static io.restassured.RestAssured.given;

public class CreateOrderRequestGenerator extends Request{
    public CreateOrderRequestGenerator(RequestSpecification requestSpecification) {
        super(requestSpecification);
    }

    public static Response createOrderRequest(CreateOrderBody createOrderBody, String apiPath){
        return given()
                .spec(setRequestSpecification())
                .header("Content-type", "application/json")
                .and()
                .body(createOrderBody)
                .when()
                .post(apiPath);
    }
}
