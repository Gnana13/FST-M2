package activities;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.internal.RequestSpecificationImpl;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class Activity3 {
    RequestSpecification requestspec;
    ResponseSpecification responsespec;
    @BeforeClass
    public void setup(){
        requestspec= new RequestSpecBuilder().
                setContentType(ContentType.JSON).
                setBaseUri("https://petstore.swagger.io/v2/pet").build();
        responsespec = new ResponseSpecBuilder().
                expectStatusCode(200).
                expectContentType("application/json").
                expectBody("status",equalTo("alive")).build();
    }
    @DataProvider
    public Object[][] petInfo(){
        Object[][] testData = new Object[][] {
                { 77232, "Riley", "alive" },
                { 77233, "Hansel", "alive" }
        };
        return testData;
        }

    @Test(priority = 1, dataProvider = "petInfo")
    public void postrequest(int id, String name, String status){
       Map<String, Object> reqBody = new HashMap<>() ;
       reqBody.put("id",id);
       reqBody.put("name",name);
       reqBody.put("status",status);
       Response response = given().spec(requestspec).body(reqBody).when().post();
       System.out.println(response.getBody().asPrettyString());
       response.then().spec(responsespec);
    }
    @Test(priority = 2, dataProvider = "petInfo")
    public void getrequest(int id, String name, String status){
       Response response= given().spec(requestspec).pathParam("petId",id).when().get("/{petId}");
       System.out.println(response.getBody().asPrettyString());
       response.then().spec(responsespec).body("name",equalTo(name));
    }
    @Test(priority = 3, dataProvider = "petInfo")
    public void deleterequest(int id, String name, String status){
        Response response = given().spec(requestspec).pathParam("petId",id).when().delete("/{petId}");
        response.then().body("code",equalTo(200));

    }
}