package com.de.laval.Pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.support.PageFactory;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Supplier;

public class BasePage {

    protected AppiumDriver<MobileElement> driver;

    protected BasePage(AppiumDriver<MobileElement> driver){
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

//    public void initializePageFactory() {
//        PageFactory.initElements(new AppiumFieldDecorator(driver, java.time.Duration.ofSeconds(3)), this);
//    }

    private Supplier<Platforms> lazyPlatform = () ->  {
        String platformName = this.driver.getPlatformName();
        Optional<Platforms> platform = Arrays.stream(Platforms.values()).filter(v -> v.toString().equalsIgnoreCase(platformName)).findFirst();

        if (!platform.isPresent()) {
            throw new NotFoundException("Could not determine the driver platform.");
        }

        Platforms result = platform.get();
        lazyPlatform = () -> result;
        return result;
    };

    public Platforms getAutomationPlatform() {
        return this.lazyPlatform.get();
    }

    public boolean isiOS(){
        return getAutomationPlatform() == Platforms.iOS;
    }

    public boolean isAndroid() {
        return getAutomationPlatform() == Platforms.Android;
    }

    public enum Platforms {
        iOS,
        Android
    }
}
