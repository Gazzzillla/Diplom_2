package user;

import java.util.UUID;

public class UserGenerator {

    public static User getRandomUser() {
        String randomValue = UUID.randomUUID().toString();

        return new User(
                "user_" + randomValue + "@test.ru",
                "password123",
                "User " + randomValue
        );
    }
}