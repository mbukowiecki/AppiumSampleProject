package com.de.laval.Extensions;

import io.appium.java_client.MobileElement;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public interface Extensions {

    default boolean elementIsVisibleUntilTime(MobileElement element, int seconds) {
        WebDriverWait wait = new WebDriverWait(element.getWrappedDriver(), seconds);

        try {
            wait.pollingEvery(Duration.ofMillis(500)).until(d -> element.isDisplayed());
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

}
