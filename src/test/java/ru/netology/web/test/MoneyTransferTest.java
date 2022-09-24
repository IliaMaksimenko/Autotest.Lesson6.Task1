package ru.netology.web.test;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;
import ru.netology.web.page.TransferPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.web.data.DataHelper.*;

public class MoneyTransferTest {

    @AfterEach
    public void tierDawn() {
        Selenide.clearBrowserCookies();
    }

    @Test
    void validBilateralMoneyTransferOld() {
        open("http://localhost:9999");
        var authInfo = DataHelper.getAuthInfo();
        new LoginPage()
                .validLogin(authInfo)
                .validVerify(DataHelper.getVerificationCode());
        int firstCardBalance = new DashboardPage().getCardBalance(DataHelper.getFirstCardInfo());
        int secondCardBalance = new DashboardPage().getCardBalance(DataHelper.getSecondCardInfo());

        String amountToTransfer = String.valueOf(generationValidAmount(secondCardBalance));

        int expectedFirstCardBalance = firstCardBalance + Integer.parseInt(amountToTransfer);
        int expectedSecondCardBalance = secondCardBalance - Integer.parseInt(amountToTransfer);

        new DashboardPage().selectCardToTransfer(DataHelper.getFirstCardInfo());
        new TransferPage().validTransfer(DataHelper.getSecondCardInfo(), amountToTransfer);

        int actualFirstCardBalance = new DashboardPage().getCardBalance(DataHelper.getFirstCardInfo());
        int actualSecondCardBalance = new DashboardPage().getCardBalance(DataHelper.getSecondCardInfo());

        assertEquals(expectedFirstCardBalance, actualFirstCardBalance);
        assertEquals(expectedSecondCardBalance, actualSecondCardBalance);

        new DashboardPage().selectCardToTransfer(DataHelper.getSecondCardInfo());
        new TransferPage().validTransfer(DataHelper.getFirstCardInfo(), amountToTransfer);

        actualFirstCardBalance = new DashboardPage().getCardBalance(DataHelper.getFirstCardInfo());
        actualSecondCardBalance = new DashboardPage().getCardBalance(DataHelper.getSecondCardInfo());

        assertEquals(firstCardBalance, actualFirstCardBalance);
        assertEquals(secondCardBalance, actualSecondCardBalance);
    }

    @Test
    public void validBilateralMoneyTransferNew() {

        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCode();
        var dashboardPage = verificationPage.validVerify(verificationCode);
        var firstCardInfo = getFirstCardInfo();
        var secondCardInfo = getSecondCardInfo();
        var firstCardBalance = dashboardPage.getCardBalance(firstCardInfo);
        var secondCardBalance = dashboardPage.getCardBalance(secondCardInfo);
        var amount = generationValidAmount(firstCardBalance);
        var expectedBalanceFirstCard = firstCardBalance - amount;
        var expectedBalanceSecondCard = secondCardBalance + amount;
        var transferPage = dashboardPage.selectCardToTransfer(secondCardInfo);
        dashboardPage = transferPage.validTransfer(firstCardInfo, String.valueOf(amount));
        var actualBalanceFirstCard = dashboardPage.getCardBalance(firstCardInfo);
        var actualBalanceSecondCard = dashboardPage.getCardBalance(secondCardInfo);

        assertEquals(expectedBalanceFirstCard, actualBalanceFirstCard);
        assertEquals(expectedBalanceSecondCard, actualBalanceSecondCard);

        transferPage = dashboardPage.selectCardToTransfer(firstCardInfo);
        dashboardPage = transferPage.validTransfer(secondCardInfo, String.valueOf(amount));
        actualBalanceFirstCard = dashboardPage.getCardBalance(firstCardInfo);
        actualBalanceSecondCard = dashboardPage.getCardBalance(secondCardInfo);

        assertEquals(firstCardBalance, actualBalanceFirstCard);
        assertEquals(secondCardBalance, actualBalanceSecondCard);
    }
}
