package ru.netology.web.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;

import static com.codeborne.selenide.Selenide.*;

public class MoneyTransferTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    void loginUserTest() {
        var authInfo = DataHelper.getAuthInfo();
        int indexFirstCard = 0;
        int indexSecondCard = 1;

        new LoginPage()
                .validLogin(authInfo)
                .validVerify(DataHelper.VerificationCode.getVerificationCode(authInfo));
        new DashboardPage().replenishCard(indexFirstCard);
        new DashboardPage().replenishCard(indexSecondCard);
        Selenide.clearBrowserCookies();
    }
}
