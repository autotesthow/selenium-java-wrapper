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
package how.autotest.selenium_java_wrapper;

import com.google.common.collect.ImmutableList;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.MoveTargetOutOfBoundsException;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class VerboseWebDriverWait extends WebDriverWait {
    WebDriver driver;

    public VerboseWebDriverWait(WebDriver driver, Duration timeout) {
        super(driver, timeout);
        this.driver = driver; // we store to use it inside overridden timeoutException
        // ... to take screenshot, store it, and add its path to message
    }

    public static WebDriverWait withIgnoringFlakyTestsErrors(WebDriver driver, Duration timeout) {
        return (WebDriverWait) new VerboseWebDriverWait(driver, timeout).ignoreAll(ImmutableList.of(
                StaleElementReferenceException.class,
                InvalidElementStateException.class,
                JavascriptException.class,
                MoveTargetOutOfBoundsException.class,
                NotFoundException.class

        ));
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
