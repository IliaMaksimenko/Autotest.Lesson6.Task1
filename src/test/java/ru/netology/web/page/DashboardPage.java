package ru.netology.web.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";

    private final SelenideElement amount = $("[data-test-id=\"amount\"] input");
    private final SelenideElement accountNumber = $("[data-test-id=\"from\"] input");

    public int getCardBalance(int indexCard) {
        val text = $$(".list__item div").get(indexCard).text();
        return extractBalance(text);
    }

    private int extractBalance(String text) {
        val start = text.indexOf(balanceStart);
        val finish = text.indexOf(balanceFinish);
        val value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }

    public DashboardPage() {
        $("[data-test-id=\"dashboard\"]").shouldBe(visible).shouldHave(text("  Личный кабинет"));
        $(By.tagName("h1")).shouldBe(visible).shouldHave(text("Ваши карты"));
    }

    public void replenishCard(int indexCard) {

        String amountReplenish = String.valueOf(DataHelper.TransferAmount.getAmount());
        int[] cardBalance = new int[]{
                getCardBalance(0),
                getCardBalance(1)
        };
        String[] numberAccount = new String[]{
                "5559 0000 0000 0002",
                "5559 0000 0000 0001"
        };
        String[] numberAccountBalabce = new String[]{
                $$(".list__item div").get(0).text().substring(0, 19),
                $$(".list__item div").get(1).text().substring(0, 19)
        };
        ElementsCollection buttonReplenish = $$("[data-test-id=\"action-deposit\"]");
        buttonReplenish.get(indexCard).click();
        $(By.tagName("h1")).shouldBe(visible).shouldHave(text("Пополнение карты"));
        amount.click();
        amount.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        amount.setValue(amountReplenish);
        accountNumber.click();
        accountNumber.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        accountNumber.setValue(numberAccount[indexCard]);
        String balance = amount.getValue().replaceAll(" ", "");
        $("[data-test-id=\"action-transfer\"]").click();
        $$(".list__item div").get(indexCard)
                .shouldHave(text(numberAccountBalabce[indexCard]
                        + ", баланс: " + (cardBalance[indexCard] + Integer.parseInt(balance))
                        + " р.\nПополнить"));
    }
}
