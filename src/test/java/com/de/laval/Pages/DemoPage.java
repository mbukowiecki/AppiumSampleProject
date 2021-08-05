package com.de.laval.Pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import lombok.Getter;

@Getter
public class DemoPage extends BasePage {

    public DemoPage(AppiumDriver<MobileElement> driver){
        super(driver);}

    @iOSXCUITFindBy(id = "Login")
    @AndroidFindBy(id = "toolbar_logo")
    private MobileElement toolbarLogo;

    @iOSXCUITFindBy(id = "Login")
    @AndroidFindBy(id = "fragment_main_search")
    private MobileElement search;



}
