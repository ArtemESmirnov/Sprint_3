package requestgenerators;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class ListOrdersRequestGenerator extends Request{
    public ListOrdersRequestGenerator(RequestSpecification requestSpecification) {
        super(requestSpecification);
    }

    public static Response listOrdersRequest(String apiPath){
        return given()
                .spec(setRequestSpecification())
                .header("Content-type", "application/json")
                .when()
                .get(apiPath);
    }

    public static Response listOrdersRequest(String apiPath, int courierId){
        return given()
                .spec(setRequestSpecification())
                .header("Content-type", "application/json")
                .when()
                .queryParam("courierId", courierId)
                .get(apiPath);
    }
}
