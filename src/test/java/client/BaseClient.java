
package client;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

import static io.restassured.http.ContentType.JSON;

public class BaseClient {

    private static final String BASE_URL =
            System.getProperty(
                    "baseUrl",
                    "https://qa-stellarburgers.education-services.ru"
            );

    protected static final RequestSpecification REQUEST_SPEC =
            new RequestSpecBuilder()
                    .setBaseUri(BASE_URL)
                    .setContentType(JSON)
                    .build();
}
