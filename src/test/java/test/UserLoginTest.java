package test;

import client.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserCredentials;
import user.UserGenerator;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserLoginTest {

    private UserClient userClient;
    private User user;
    private String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getRandomUser();

        Response response = userClient.create(user);
        accessToken = response.path("accessToken");
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.delete(accessToken);
        }
    }

    @Test
    @DisplayName("Вход существующего пользователя")
    @Description("Проверка успешной авторизации зарегистрированного пользователя")
    public void loginExistingUserReturnsSuccess() {
        UserCredentials credentials = UserCredentials.from(user);

        userClient.login(credentials)
                .then()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue());
    }

    @Test
    @DisplayName("Вход с неверными данными")
    @Description("Проверка ошибки авторизации пользователя с неверными email и паролем")
    public void loginWithInvalidCredentialsReturnsError() {
        UserCredentials credentials = new UserCredentials(
                "wrong_" + user.getEmail(),
                "wrongPassword"
        );

        userClient.login(credentials)
                .then()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}