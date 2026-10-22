
package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import order.Order;

import static io.restassured.RestAssured.given;

public class OrderClient extends BaseClient {

    @Step("Получение ингредиентов")
    public Response getIngredients() {
        return given()
                .spec(REQUEST_SPEC)
                .when()
                .get("/api/ingredients");
    }

    @Step("Создание заказа с авторизацией")
    public Response create(Order order, String accessToken) {
        return given()
                .spec(REQUEST_SPEC)
                .header("Authorization", accessToken)
                .body(order)
                .when()
                .post("/api/orders");
    }

    @Step("Создание заказа без авторизации")
    public Response createWithoutAuthorization(Order order) {
        return given()
                .spec(REQUEST_SPEC)
                .body(order)
                .when()
                .post("/api/orders");
    }
}