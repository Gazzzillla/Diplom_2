package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import user.User;
import user.UserCredentials;

import static io.restassured.RestAssured.given;

public class UserClient {

    private static final String BASE_URL = "https://stellarburgers.education-services.ru";

    @Step("Создание пользователя")
    public Response create(User user) {
        return given()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post("/api/auth/register");
    }

    @Step("Авторизация пользователя")
    public Response login(UserCredentials credentials) {
        return given()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json")
                .body(credentials)
                .when()
                .post("/api/auth/login");
    }

    @Step("Удаление пользователя")
    public Response delete(String accessToken) {
        return given()
                .baseUri(BASE_URL)
                .header("Authorization", accessToken)
                .when()
                .delete("/api/auth/user");
    }
}