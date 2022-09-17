package ru.netology.web.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";
    private final SelenideElement amount = $("[data-test-id=\"amount\"] input");
    private final SelenideElement accountNumber = $("[data-test-id=\"from\"] input");
    private final ElementsCollection cards = $$(".list__item div");

    public int getCardBalance(DataHelper.CardInfo cardInfo) {
        val text = cards.findBy(attribute("data-test-id", cardInfo.getTestId())).getText();
        return extractBalance(text);
    }

    private int extractBalance(String text) {
        val start = text.indexOf(balanceStart);
        val finish = text.indexOf(balanceFinish);
        val value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }

    public TransferPage selectCardToTransfer(DataHelper.CardInfo cardInfo){
        cards.findBy(attribute("data-test-id", cardInfo.getTestId())).$("button").click();
        return new TransferPage();
    }

    public DashboardPage() {
        $("[data-test-id=\"dashboard\"]").shouldBe(visible).shouldHave(text("  Личный кабинет"));
        $(By.tagName("h1")).shouldBe(visible).shouldHave(text("Ваши карты"));
    }

}
