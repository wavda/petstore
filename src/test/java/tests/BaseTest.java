package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import utils.ExecutionWatcher;
import utils.ExtentTestManager;
import utils.Logger;

@ExtendWith(ExecutionWatcher.class)
public class BaseTest {
    public final String api_key = System.getProperty("api_key", "special-key");

    public RequestSpecification request;
    public Response response;
    public JSONObject requestParams;


    @BeforeEach
    public void startTest(TestInfo testInfo) {
        ExtentTestManager.startTest(testInfo.getDisplayName());
        String className = testInfo.getTestClass().toString();
        Logger.info(className);

        RestAssured.baseURI = "https://petstore.swagger.io/v2";
        request = RestAssured.given();
        request.header("Content-type", "application/json");
        request.header("api_key", api_key);
        requestParams = new JSONObject();
    }

    @AfterEach
    public void endTest(){
        Logger.info(response.getBody().asString());
        Assertions.assertEquals(200, response.statusCode());
    }

    public JSONObject getResponseBody(String method, String endpoint) {
        Logger.info(method.toUpperCase()+ " " + endpoint);

        switch (method.toUpperCase()){
            case "GET":
                response = request.get(endpoint).then().extract().response();
                break;
            case "POST":
                request.body(requestParams.toString());
                response = request.post(endpoint).then().extract().response();
                break;
            case "DELETE":
                response = request.delete(endpoint).then().extract().response();
                break;
        }

        return new JSONObject(response.getBody().asString());
    }
}
