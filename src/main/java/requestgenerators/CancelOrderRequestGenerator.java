package requestgenerators;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class CancelOrderRequestGenerator extends Request{
    public CancelOrderRequestGenerator(RequestSpecification requestSpecification) {
        super(requestSpecification);
    }

    public static Response cancelOrderRequest(String track, String apiPath){
        return given()
                .spec(setRequestSpecification())
                .header("Content-type", "application/json")
                .and()
                .queryParam("track", track)
                .put(apiPath);
    }
}
