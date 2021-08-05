package com.de.laval.Pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import lombok.Getter;

@Getter
public class HomePage extends BasePage {

    public HomePage(AppiumDriver<MobileElement> driver){super(driver);}

    @iOSXCUITFindBy(id = "Login")
    @AndroidFindBy(id = "com.android.permissioncontroller:id/permission_allow_button")
    private MobileElement allowAccessToDataButton;

    @iOSXCUITFindBy(id = "Login")
    @AndroidFindBy(className = "android.widget.EditText")
    private MobileElement activationCodeInput;

    @iOSXCUITFindBy(id = "Login")
    @AndroidFindBy(id = "activity_registration_register_btn")
    private MobileElement registerButton;

    @iOSXCUITFindBy(id = "Login")
    @AndroidFindBy(id = "activity_registration_demo_btn")
    private MobileElement demoModeButton;

    @iOSXCUITFindBy(id = "Login")
    @AndroidFindBy(id = "activity_registration_imei")
    private MobileElement deviceId;

    @iOSXCUITFindBy(id = "Login")
    @AndroidFindBy(id = "activity_registration_unregister_btn")
    private MobileElement unregisterButton;
}
