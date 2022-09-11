package ru.netology.web.data;

import com.github.javafaker.Faker;
import lombok.Value;

public class DataHelper {

    private DataHelper() {
    }

    @Value
    public static class AuthInfo {
        private String login;
        private String password;
    }

    public static AuthInfo getAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }

    @Value
    public static class VerificationCode {

        private String code;

        public static VerificationCode getVerificationCode(AuthInfo authInfo) {
            return new VerificationCode("12345");
        }
    }

    @Value
    public static class TransferAmount {

        private String amount;

        public static TransferAmount getAmount() {
            Faker faker = new Faker();
            return new TransferAmount(String.valueOf(faker.number().numberBetween(1, 10000)));
        }

    }


}
