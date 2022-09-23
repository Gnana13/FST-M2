package LiveProject;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class GithubProject {
    RequestSpecification requestspec;
    String SSHkey ="ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQCMYG4JRgXKOIaNC0s+bPXxgThexlkeBwPhghkpBIxKaK0Lo8aYblwrnaxkuxaXyP7f8mZMtTtThmyteotPVC6NGfM4CSoeE28/zZ98k0u/7DS3VJN37rmvD5oCKR+hktWQrJiCa/FYwCXhZMNgkL3wt0F7McVxgJAG6Ko6RdJFVTEQMMTPAPEg1+cGqtboIu4mbnenI9SNv8sDtSkmcnsgf12Ea6ETbB4c2n6N59FUWu1F7O8paIsYjVI4VqlN++uXVyoJT5Fl72SOZLe8BmQebVZxpPomk44E4drYZfxwXM1kiIsxbExJkhnl4ABT+eDIDneANUieirlhcUIyfIpT";
    int id=0;
    @BeforeClass
    public void setUp(){
        requestspec= new RequestSpecBuilder().
                setContentType(ContentType.JSON).
                addHeader("Authorization","token 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").
                setBaseUri("https://api.github.com").build();
    }
    @Test(priority = 1)
    public void postrequest(){
        String reqbody ="{\"title\": \"TestAPIKey\",\"key\":\""+ SSHkey +"\"}";
        Response response = given().spec(requestspec).body(reqbody).
                when().post("/user/keys");
        System.out.println(response.getBody());
        id=response.then().extract().body().path("id");
        response.then().statusCode(201);
    }
    @Test(priority = 2)
    public void getrequest(){
        Response response = given().spec(requestspec).pathParam("keyId",id).when().get("/user/keys/{keyId}");
        System.out.println(response.getBody());
        response.then().statusCode(200);
    }
    @Test(priority = 3)
    public void deleterequest(){
        Response response = given().spec(requestspec).pathParam("keyId",id).when().delete("/user/keys/{keyId}");
        System.out.println(response.getBody());
        response.then().statusCode(204);
    }
}
