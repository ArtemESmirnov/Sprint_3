package requestgenerators;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class TrackOrderRequestGenerator extends Request{
    public TrackOrderRequestGenerator(RequestSpecification requestSpecification) {
        super(requestSpecification);
    }

    public static Response trackOrderRequest(int track, String apiPath){
        return given()
                .spec(setRequestSpecification())
                .header("Content-type", "application/json")
                .queryParam("t", track)
                .get(apiPath);
    }
}
