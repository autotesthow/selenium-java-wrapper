/*
MIT License

Copyright (c) 2023 Iakiv Kramarenko

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package how.auotest.selenium_java_wrapper;

import how.autotest.selenium_java_wrapper.VerboseWebDriverWait;
import how.autotest.selenium_java_wrapper.commands.Action;
import how.autotest.selenium_java_wrapper.conditions.That;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class TheWebTest {

    WebDriver driver;
    WebDriverWait wait_;

    public <V> V assert_(Function<? super WebDriver, V> isTrue) {
        return wait_.until(isTrue);
    }

    public <V> V do_(Function<? super WebDriver, V> isTrue) {
        return wait_.until(isTrue);
    }

    @BeforeEach
    void setupDriver() {

        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait_ = VerboseWebDriverWait.withIgnoringFlakyTestsErrors(driver, Duration.ofSeconds(4));
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }

    @Test
    void googleFindsSelenide() { // smarter waiting for commands and asserts but "complex element location" was simplified
        driver.get("https://google.com/ncr");
        assert_(That.valueOfLocated(By.cssSelector("[name=q]"), ""));

        do_(Action.type(By.cssSelector("[name=q]"), "github selenide"));
        do_(Action.pressEnter(By.cssSelector("[name=q]")));
        assert_(That.countOfLocated(By.cssSelector("#rso>div"), 5));
        assert_(That.partialTextOfLocated(By.cssSelector("#rso>div"), "selenide › selenide"));
//        assertThat(driver.findElements(By.cssSelector("#rso>div")).get(0).getText()).contains("selenide › selenide");

        do_(Action.click(By.cssSelector("#rso>div:nth-of-type(1) h3")));
//        driver.findElements(By.cssSelector("#rso>div")).get(0).findElement(By.cssSelector("h3")).click();
        assert_(That.titleContains("GitHub - selenide/selenide"));
    }

    @Test
    void googleFindsSelenide_() { // pure Selenium Version
        driver.get("https://google.com/ncr");
        assertThat(driver.findElement(By.cssSelector("[name=q]")).getAttribute("value")).isEqualTo("");

        driver.findElement(By.cssSelector("[name=q]")).sendKeys("github selenide");
        driver.findElement(By.cssSelector("[name=q]")).sendKeys(Keys.ENTER);

        new WebDriverWait(driver, Duration.ofSeconds(4)).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#rso>div")));
        assertThat(driver.findElements(By.cssSelector("#rso>div")).size()).isGreaterThanOrEqualTo(5);
        assertThat(driver.findElements(By.cssSelector("#rso>div")).get(0).getText()).contains("selenide › selenide");

        driver.findElements(By.cssSelector("#rso>div")).get(0).findElement(By.cssSelector("h3")).click();
        assertThat(driver.getTitle()).contains("GitHub - selenide/selenide");
    }

    @Test
    void contextClickOpensContextMenu() {
        driver.get("https://the-internet.herokuapp.com/context_menu");

        do_(Action.contextClick(By.cssSelector("#hot-spot")));

        assertThat(driver.switchTo().alert().getText()).isEqualTo("You selected a context menu");
        driver.switchTo().alert().accept();
    }
}
