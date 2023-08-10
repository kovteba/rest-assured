package kovteba;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.util.UriTemplate;

import java.net.URI;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ControllerTest {

    private String path = "http://localhost:{port}/controller";

    private URI uri;

    @LocalServerPort
    private int port;

    @RegisterExtension
    final RestDocumentationExtension restDocumentation = new RestDocumentationExtension("target/generated-snippets");

    private RequestSpecification spec;

    @BeforeEach
    public void init(RestDocumentationContextProvider restDocumentation) {

        uri = new UriTemplate(path).expand(port);

        spec = new RequestSpecBuilder().addFilter(documentationConfiguration(restDocumentation))
                                       .setPort(8080)
                                       .build();

    }

    public static Matcher<Integer> is(Long value) {
        return org.hamcrest.core.Is.is(value.intValue());
    }

    @Test
    public void controller() {

        given(spec)
                .filter(document("{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
        .when()
                .get(uri)
        .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON.toString());
    }

    @Test
    public void controller1() {

        String value = "1";

        given(spec)
                .filter(document("{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(parameterWithName("number").description("Test path param")),
                        requestParameters(parameterWithName("value").description("Test value"))))
                .pathParam("number", 1)
                .param("value", value)
        .when()
                .get(uri + "/{number}")
        .then()
                .statusCode(200)
                .body("response", equalTo("CONTROLLER_1"));
    }
}
