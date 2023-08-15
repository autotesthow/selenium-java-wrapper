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
                actual = driver.findElements(locator).size();
                return actual >= expected;
            }

            @Override
            public String toString() {
                return "located " + locator + "\n expected count >= " + expected + "\nbut \n actual count = " + actual + "\n\n";
            }
        };
    }
}
