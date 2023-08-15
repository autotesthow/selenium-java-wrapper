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
package how.autotest.selenium_java_wrapper.commands;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

import java.util.function.Function;

public class Action {

    private static void assertIsNotOverlapped(
            WebDriver driver,
            WebElement webelement,
            int centerXOffset,
            int centerYOffset
    ) {
        ((JavascriptExecutor)driver).executeScript(
                """
                var element = arguments[0];
                var centerXOffset = arguments[1];
                var centerYOffset = arguments[2];

                var isVisible = !!(
                    element.offsetWidth
                    || element.offsetHeight
                    || element.getClientRects().length
                ) && window.getComputedStyle(element).visibility !== 'hidden';

                if (!isVisible) {
                    throw `element ${element.outerHTML} is not visible`;
                }

                var rect = element.getBoundingClientRect();
                var x = rect.left + rect.width/2 + centerXOffset;
                var y = rect.top + rect.height/2 + centerYOffset;

                var elementByXnY = document.elementFromPoint(x,y);
                if (elementByXnY == null) {
                    return [element, null];
                }

                var isOverlapped = !element.isSameNode(elementByXnY);

                if (isOverlapped) {
                    throw `element ${element.outerHTML} is not interactable, ` +
                        `because other element would receive a click: ${elementByXnY.outerHTML}`;
                }
                """,
                webelement,
                centerXOffset,
                centerYOffset
        );
    }

    private static WebElement findAndAssertNotOverlapped(
            WebDriver driver,
            By locator,
            int centerXOffset,
            int centerYOffset
    ) {

        var webelement = driver.findElement(locator);
        assertIsNotOverlapped(driver, webelement, centerXOffset, centerYOffset);
        return webelement;
    }

    private static WebElement findAndAssertNotOverlappedAtCenter(
            WebDriver driver,
            By locator
    ) {
        return findAndAssertNotOverlapped(driver, locator, 0, 0);
    }

    public static Function<WebDriver, Boolean> type(By locator, String text) {
        return new Function<>() {

            @Override
            public Boolean apply(WebDriver driver) {
                var webelement = findAndAssertNotOverlappedAtCenter(driver, locator);
                webelement.sendKeys(text);
                return true;
            }

            @Override
            public String toString() {
                return "command: \n\ntype text '"+ text + "' into located " + locator;
            }
        };
    }

    public static Function<? super WebDriver, Boolean> pressEnter(By locator) {
        return new Function<>() {
            @Override
            public Boolean apply(WebDriver driver) {
                var webelement = findAndAssertNotOverlappedAtCenter(driver, locator);
                webelement.sendKeys(Keys.ENTER);
                return true;
            }

            @Override
            public String toString() {
                return "command: \n\npress enter at located " + locator;
            }
        };
    }

    public static Function<? super WebDriver, Boolean> click(By locator) {
        return new Function<>() {
            @Override
            public Boolean apply(WebDriver driver) {
                driver.findElement(locator).click();
                return true;
            }

            @Override
            public String toString() {
                return "command: \n\nclick on located " + locator;
            }
        };
    }

    public static Function<? super WebDriver, Boolean> contextClick(By locator) {
        return new Function<>() {
            @Override
            public Boolean apply(WebDriver driver) {
                var webelement = findAndAssertNotOverlappedAtCenter(driver, locator);
                new Actions(driver).contextClick(webelement).perform();
                return true;
            }

            @Override
            public String toString() {
                return "command: \n\ncontext click on located " + locator;
            }
        };
    }
}
