package activities;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.io.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


public class Activity2 {
    final String baseURI = "https://petstore.swagger.io/v2/user";
    @Test(priority = 1)
    public  void postrequest() throws IOException {
        FileInputStream inputJSON = new FileInputStream("src/test/java/resources/userinfo.json");
                String reqBody = new String(inputJSON.readAllBytes());
        Response response =
                given().contentType(ContentType.JSON)
                        .body(reqBody)
                        .when().post(baseURI);

        inputJSON.close();
       response.then().body("code", equalTo(200));
        response.then().body("message", equalTo("1123"));
    }

    @Test(priority =2)
    public void getrequest() throws IOException {
        File outputJSON = new File("src/test/java/resources/userresponse.json");
        Response response = given().contentType(ContentType.JSON).
                pathParam("username","JamesRathod").when().get(baseURI+"/{username}");
        String resBody = response.getBody().asPrettyString();
        outputJSON.createNewFile();
        FileWriter writer = new FileWriter(outputJSON.getPath());
        writer.write(resBody);
        writer.close();
        response.then().body("id",equalTo(1123));
        response.then().body("username",equalTo("JamesRathod"));
        response.then().body("firstName", equalTo("James"));
        response.then().body("lastName", equalTo("Rathod"));
        response.then().body("email", equalTo("jamesrathod@mail.com"));
        response.then().body("password", equalTo("pwd1234"));
        response.then().body("phone", equalTo("9865421780"));

    }
    @Test(priority = 3)
    public void deleterequest(){
        Response response = given().contentType(ContentType.JSON).
                pathParam("username","JamesRathod").when().delete(baseURI + "/{username}");
        response.then().body("code", equalTo(200));
        response.then().body("message", equalTo("JamesRathod"));

    }

}