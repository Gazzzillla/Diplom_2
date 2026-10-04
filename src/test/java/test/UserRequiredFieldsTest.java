package test;

import client.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import user.User;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)
public class UserRequiredFieldsTest {

    private final String email;
    private final String password;
    private final String name;

    public UserRequiredFieldsTest(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getTestData() {
        String uniqueEmail = "user_" + UUID.randomUUID() + "@test.ru";

        return Arrays.asList(new Object[][]{
                {null, "password123", "User"},
                {uniqueEmail, null, "User"},
                {uniqueEmail, "password123", null}
        });
    }

    @Test
    @DisplayName("Создание пользователя без обязательного поля")
    @Description("Проверка ошибки регистрации при отсутствии email, password или name")
    public void createUserWithoutRequiredFieldReturnsError() {
        User user = new User(email, password, name);
        UserClient userClient = new UserClient();

        userClient.create(user)
                .then()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
}