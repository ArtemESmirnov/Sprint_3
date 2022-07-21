package requestgenerators;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class AcceptOrderRequestGenerator extends Request{
    public AcceptOrderRequestGenerator(RequestSpecification requestSpecification) {
        super(requestSpecification);
    }

    public static Response acceptOrderRequest(int orderId, int courierId, String apiPath){
        return given()
                .spec(setRequestSpecification())
                .header("Content-type", "application/json")
                .queryParam("courierId", courierId)
                .put(apiPath, orderId);
    }
}
