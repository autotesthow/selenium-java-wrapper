package how.autotest.selenium_java_wrapper.conditions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.function.Function;

public class That {
    public static Function<WebDriver, Boolean> countOfLocated(By locator, Integer expected) {
        return new Function<WebDriver, Boolean>() {

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
