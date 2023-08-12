package how.auotest.selenium_java_wrapper;

import how.autotest.selenium_java_wrapper.SmarterWebDriverWait;
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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GoogleTest {

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
        wait_ = new SmarterWebDriverWait(driver, Duration.ofSeconds(4));
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }

    @Test
    void findsSelenide() { // first examples of command and assertion that use SMARTER waiting
        driver.get("https://google.com/ncr");
        assertThat(driver.findElement(By.cssSelector("[name=q]")).getAttribute("value")).isEqualTo("");

        do_(Action.type(By.cssSelector("[name=q]"), "github selenide")); // <- SMARTER
        driver.findElement(By.cssSelector("[name=q]")).sendKeys(Keys.ENTER);

        assert_(That.countOfLocated(By.cssSelector("#rso>div"), 5)); // <- SMARTER
        assertThat(driver.findElements(By.cssSelector("#rso>div")).get(0).getText()).contains("selenide › selenide");

        driver.findElements(By.cssSelector("#rso>div")).get(0).findElement(By.cssSelector("h3")).click();
        assertThat(driver.getTitle()).contains("GitHub - selenide/selenide");
    }

    @Test
    void findsSelenide_() { // pure Selenium Version
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
}
