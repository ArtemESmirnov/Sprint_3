package requestgenerators;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class DeleteCourierRequestGenerator extends Request{
    public DeleteCourierRequestGenerator(RequestSpecification requestSpecification) {
        super(requestSpecification);
    }

    public static Response deleteCourierRequest(Integer id, String apiPath){
        return given()
                .spec(setRequestSpecification())
                .header("Content-type", "application/json")
                .when()
                .delete(apiPath, id.toString());
    }
}
