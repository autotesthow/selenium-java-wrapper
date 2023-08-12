package how.autotest.selenium_java_wrapper;

import com.google.common.collect.ImmutableList;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.MoveTargetOutOfBoundsException;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SmarterWebDriverWait extends WebDriverWait {
    WebDriver driver;

    public SmarterWebDriverWait(WebDriver driver, Duration timeout) {
        super(driver, timeout);
        this.ignoreAll(ImmutableList.of(
                StaleElementReferenceException.class,
                InvalidElementStateException.class,
                JavascriptException.class,
                MoveTargetOutOfBoundsException.class,
                NotFoundException.class

        ));
        this.driver = driver; // we store to use it inside overridden timeoutException
        // ... to take screenshot, store it, and add its path to message
    }

    @Override
    protected RuntimeException timeoutException(String message, Throwable lastException) {
        // TODO: store screenshot based on: ((TakesScreenshot)this.driver).getScreenshotAs ...
        var messageWithCauseAndScreenshot = message
                + "\n"
                + (lastException == null ? "" : "\nCaused by:\n" + lastException.getMessage()); // TODO: add path to screenshot
        return super.timeoutException(messageWithCauseAndScreenshot, lastException);
    }
}
