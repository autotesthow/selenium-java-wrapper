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
package how.autotest.selenium_java_wrapper.conditions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.function.Function;

public class That {
    public static Function<WebDriver, Boolean> countOfLocated(By locator, Integer expected) {
        return new Function<>() {

            int actual;

            @Override
            public Boolean apply(WebDriver driver) {
                this.actual = driver.findElements(locator).size();
                return this.actual >= expected;
            }

            @Override
            public String toString() {
                return "located " + locator
                        + "\n expected count >= " + expected
                        + "\nbut \n actual count = " + this.actual + "\n\n";
            }
        };
    }

    public static Function<WebDriver, Boolean> valueOfLocated(By locator, String expected) {
        return new Function<>() {

            String actual;

            @Override
            public Boolean apply(WebDriver driver) {
                this.actual = driver.findElement(locator).getAttribute("value");
                return this.actual.equals(expected);
            }

            @Override
            public String toString() {
                return "located " + locator
                        + "\n expected value = " + expected
                        + "\nbut \n actual value " + (this.actual == null ? "was failed to get" : "= " + this.actual) + "\n\n";
            }
        };
    }

    public static Function<WebDriver, Boolean> partialTextOfLocated(By locator, String expected) {
        return new Function<>() {

            String actual;

            @Override
            public Boolean apply(WebDriver driver) {
                actual = driver.findElement(locator).getText();
                return actual.contains(expected);
            }

            @Override
            public String toString() {
                var describedLocator = "located " + locator;
                var describedExpected = "\n expected partial text = " + expected;
                var describedActual = "\nbut \n actual text " + (
                        this.actual == null ? "was failed to get" : "= " + this.actual
                ) + "\n\n";

                return describedLocator + describedExpected + describedActual;
            }
        };
    }

    public static Function<WebDriver, Boolean> titleContains(String expected) {
        return new Function<>() {

            String actual;

            @Override
            public Boolean apply(WebDriver driver) {
                actual = driver.getTitle();
                return actual.contains(expected);
            }

            @Override
            public String toString() {
                var describedEntity = "page";
                var describedExpected = "\n expected title contains " + expected;
                var describedActual = "\nbut \n actual title " + (
                        this.actual == null ? "was failed to get" : "= " + this.actual
                ) + "\n\n";

                return describedEntity + describedExpected + describedActual;
            }
        };
    }
}
