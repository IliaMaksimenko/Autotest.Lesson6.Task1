package ru.netology.web.test;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;
import ru.netology.web.page.TransferPage;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.web.data.DataHelper.CardInfo.generationValidAmount;

public class MoneyTransferTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @AfterEach
    void tearDawn() {
        Selenide.clearBrowserCookies();
    }

    @Test
    void validBilateralMoneyTransfer() {
        var authInfo = DataHelper.getAuthInfo();
        new LoginPage()
                .validLogin(authInfo)
                .validVerify(DataHelper.VerificationCode.getVerificationCode());

        int firstCardBalance = new DashboardPage().getCardBalance(DataHelper.CardInfo.getFirstCardInfo());
        int secondCardBalance = new DashboardPage().getCardBalance(DataHelper.CardInfo.getSecondCardInfo());

        String amountToTransfer = String.valueOf(generationValidAmount(secondCardBalance));

        int expectedFirstCardBalance = firstCardBalance + Integer.parseInt(amountToTransfer);
        int expectedSecondCardBalance = secondCardBalance - Integer.parseInt(amountToTransfer);

        new DashboardPage().selectCardToTransfer(DataHelper.CardInfo.getFirstCardInfo());
        new TransferPage().validTransfer(DataHelper.CardInfo.getSecondCardInfo(), amountToTransfer);

        int actualFirstCardBalance = new DashboardPage().getCardBalance(DataHelper.CardInfo.getFirstCardInfo());
        int actualSecondCardBalance = new DashboardPage().getCardBalance(DataHelper.CardInfo.getSecondCardInfo());

        assertEquals(expectedFirstCardBalance, actualFirstCardBalance);
        assertEquals(expectedSecondCardBalance, actualSecondCardBalance);

        new DashboardPage().selectCardToTransfer(DataHelper.CardInfo.getSecondCardInfo());
        new TransferPage().validTransfer(DataHelper.CardInfo.getFirstCardInfo(), amountToTransfer);

        int newActualFirstCardBalance = new DashboardPage().getCardBalance(DataHelper.CardInfo.getFirstCardInfo());
        int newActualSecondCardBalance = new DashboardPage().getCardBalance(DataHelper.CardInfo.getSecondCardInfo());

        assertEquals(firstCardBalance, newActualFirstCardBalance);
        assertEquals(secondCardBalance, newActualSecondCardBalance);
    }
}
