package test;

import client.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserGenerator;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserCreateTest {

    private UserClient userClient;
    private User user;
    private String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getRandomUser();
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.delete(accessToken);
        }
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    @Description("Проверка успешного создания пользователя с уникальными email, паролем и именем")
    public void createUniqueUserReturnsSuccess() {
        Response response = userClient.create(user);

        response.then()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue());

        accessToken = response.path("accessToken");
    }

    @Test
    @DisplayName("Создание уже зарегистрированного пользователя")
    @Description("Проверка ошибки при повторной регистрации пользователя с теми же данными")
    public void createExistingUserReturnsError() {
        Response firstResponse = userClient.create(user);
        accessToken = firstResponse.path("accessToken");

        Response secondResponse = userClient.create(user);

        secondResponse.then()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }
}