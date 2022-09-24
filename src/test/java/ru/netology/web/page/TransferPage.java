package ru.netology.web.page;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class TransferPage {

    private final SelenideElement amountInput = $("[data-test-id=\"amount\"] input");
    private final SelenideElement fromInput = $("[data-test-id=\"from\"] input");
    private final SelenideElement transferButton = $("[data-test-id=\"action-transfer\"]");

    public DashboardPage validTransfer(DataHelper.CardInfo cardInfo, String amountToTransfer) {
        makeTransfer(cardInfo, amountToTransfer);
        return new DashboardPage();
    }

    public void makeTransfer(DataHelper.CardInfo cardInfo, String amountToTransfer) {
        amountInput.click();
        amountInput.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        amountInput.setValue(amountToTransfer);
        fromInput.click();
        fromInput.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        fromInput.setValue(cardInfo.getCardNumber());
        transferButton.click();
    }

    public TransferPage() {
        $(By.tagName("h1")).shouldBe(visible).shouldHave(text("Пополнение карты"));
    }

}
