package LiveProject;
import java.util.HashMap;
import java.util.Map;
import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


import static io.restassured.RestAssured.given;

    @ExtendWith(PactConsumerTestExt.class)
    public class consumerTest {
        Map<String, String> headers = new HashMap<>();
        String resourcepath = "/api/users";

        @Pact(consumer = "UserConsumer", provider = "UserProvider")
        public RequestResponsePact createPact(PactDslWithProvider builder) {
            headers.put("Content-Type", "application/json");
            DslPart requestResponseBody = new PactDslJsonBody()
                    .numberType("id",98)
                    .stringType("firstName","Gnana")
                    .stringType("lastName","Kanthi")
                    .stringType("email");
            return builder.given("A request to create a user")
                    .uponReceiving("A request to create a user")
                    .method("POST")
                    .path(resourcepath)
                    .headers(headers)
                    .body(requestResponseBody)
                    .willRespondWith()
                    .status(201)
                    .body(requestResponseBody)
                    .toPact();
        }

        @Test
        @PactTestFor(providerName = "UserProvider", port = "8282")
        public void consumerTest() {
            final String baseURI = "http://localhost:8282";
            Map<String, Object> reqBody = new HashMap<>();
            reqBody.put("id", 98);
            reqBody.put("firstName", "Gnana");
            reqBody.put("lastName", "Kanthi");
            reqBody.put("email", "james@example.com");

            Response response = given().headers(headers)
                    .body(reqBody).when().post(baseURI + resourcepath);
            System.out.println(response.getBody());
            response.then().statusCode(201).log().all();

        }

    }

