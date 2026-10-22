package test;

import client.OrderClient;
import client.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import order.Order;
import user.User;
import user.UserGenerator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class OrderCreateTest {

    private UserClient userClient;
    private OrderClient orderClient;
    private String accessToken;
    private List<String> ingredients;

    @Before
    public void setUp() {
        userClient = new UserClient();
        orderClient = new OrderClient();

        User user = UserGenerator.getRandomUser();
        Response userResponse = userClient.create(user);
        accessToken = userResponse.path("accessToken");

        Response ingredientsResponse = orderClient.getIngredients();
        String firstIngredient = ingredientsResponse.path("data[0]._id");
        String secondIngredient = ingredientsResponse.path("data[1]._id");

        ingredients = Arrays.asList(firstIngredient, secondIngredient);
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.delete(accessToken);
        }
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и ингредиентами")
    @Description("Проверка успешного создания заказа авторизованным пользователем с валидными ингредиентами")
    public void createOrderWithAuthorizationAndIngredientsReturnsSuccess() {
        Order order = new Order(ingredients);

        orderClient.create(order, accessToken)
                .then()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Проверка ошибки создания заказа без ингредиентов")
    public void createOrderWithoutIngredientsReturnsError() {
        Order order = new Order(Collections.emptyList());

        orderClient.create(order, accessToken)
                .then()
                .statusCode(SC_BAD_REQUEST)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиента")
    @Description("Проверка ошибки создания заказа с несуществующим хешем ингредиента")
    public void createOrderWithInvalidIngredientHashReturnsError() {
        Order order = new Order(
                Collections.singletonList("invalidIngredientHash")
        );

        orderClient.create(order, accessToken)
                .then()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Проверка успешного создания заказа без авторизации с валидными ингредиентами")
    public void createOrderWithoutAuthorizationReturnsSuccess() {
        Order order = new Order(ingredients);

        orderClient.createWithoutAuthorization(order)
                .then()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("order.number", notNullValue());
    }
}