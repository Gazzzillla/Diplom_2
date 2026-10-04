package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import order.Order;

import static io.restassured.RestAssured.given;

public class OrderClient {

    private static final String BASE_URL = "https://stellarburgers.education-services.ru";

    @Step("Получение ингредиентов")
    public Response getIngredients() {
        return given()
                .baseUri(BASE_URL)
                .when()
                .get("/api/ingredients");
    }

    @Step("Создание заказа с авторизацией")
    public Response create(Order order, String accessToken) {
        return given()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .body(order)
                .when()
                .post("/api/orders");
    }

    @Step("Создание заказа без авторизации")
    public Response createWithoutAuthorization(Order order) {
        return given()
                .baseUri(BASE_URL)
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post("/api/orders");
    }
}