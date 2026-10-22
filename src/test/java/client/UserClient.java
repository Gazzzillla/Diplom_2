
package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import user.User;
import user.UserCredentials;

import static io.restassured.RestAssured.given;

public class UserClient extends BaseClient {

    @Step("Создание пользователя")
    public Response create(User user) {
        return given()
                .spec(REQUEST_SPEC)
                .body(user)
                .when()
                .post("/api/auth/register");
    }

    @Step("Авторизация пользователя")
    public Response login(UserCredentials credentials) {
        return given()
                .spec(REQUEST_SPEC)
                .body(credentials)
                .when()
                .post("/api/auth/login");
    }

    @Step("Удаление пользователя")
    public Response delete(String accessToken) {
        return given()
                .spec(REQUEST_SPEC)
                .header("Authorization", accessToken)
                .when()
                .delete("/api/auth/user");
    }
}