package com.de.laval.Test;

import com.de.laval.Pages.DemoPage;
import com.de.laval.Pages.HomePage;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class StepDefinitionAdapter {

    protected Map<String, Object> TestState = new HashMap<String, Object>();
    private AppiumDriver<MobileElement> driver;
    private static AppiumDriverLocalService service;
    private WebDriverWait wait;
    private final DesiredCapabilities capabilities = new DesiredCapabilities();
    private final String platformName = getTestParameter("platformName", true).toLowerCase();

    public AppiumDriver<MobileElement> getDriver() {
        return driver;
    }

    public WebDriverWait getWait() {return wait;}

    public HomePage getHomePage() {
        return new HomePage(driver);
    }

    public DemoPage getDemoPage() {
        return new DemoPage(driver);
    }

    public void startAppiumServer(){
        AppiumServiceBuilder builder;

        builder = new AppiumServiceBuilder();
        builder.withIPAddress("0.0.0.0")
                .usingPort(4723)
                .usingDriverExecutable(new File("/usr/local/bin/node"))
                .withAppiumJS(new File("/usr/local/lib/node_modules/appium/build/lib/main.js"))
                .withArgument(GeneralServerFlag.SESSION_OVERRIDE)
                .withArgument(GeneralServerFlag.LOG_TIMESTAMP);

        service = AppiumDriverLocalService.buildService(builder);
        service.start();
    }

    public void runApp() throws Exception {
        //  initialize platform specific driver
        URL serverUrl = new URL(capabilities.getCapability("URL").toString());
        driver = platformName.equals("ios") ? new IOSDriver<>(serverUrl, capabilities) :
                new AndroidDriver<>(serverUrl, capabilities);
        wait = new WebDriverWait(driver, 30);
    }

    public String getTestParameter(String key, boolean isRequired) {
        String parameter = System.getProperty(key) != null ? System.getProperty(key) : System.getenv(key);

        String filePath = "./src/test/resources/env.properties";
        File f = new File(filePath);
        if (f.exists()) {
            Properties envLocalProps = new Properties();
            try {
                envLocalProps.load(new FileInputStream(filePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (envLocalProps.getProperty(key) != null) parameter = envLocalProps.getProperty(key);
        }

        if (isRequired && parameter == null) {
            throw new IllegalArgumentException("There is no argument with key '" + key + "' passed to the test run. " +
                    "You can either provide that argument to the maven call via '-D" + key + "=...' or " +
                    "set an environment variable with the key.");
        }
        return parameter;
    }

    private void setArgumentCapabilityIfPresent(DesiredCapabilities target, String key) {
        String value = getTestParameter(key, false);

        if (value != null) {
            try {
                int valueAsInt = Integer.parseInt(value);
                target.setCapability(key, valueAsInt);
            } catch (NumberFormatException e) {
                target.setCapability(key, value);
            }
        }
    }

    @Before
    public void setupDriver(Scenario scenario) throws Exception {
        startAppiumServer();

        //  get the required test parameters
        String appPath = getTestParameter("app", true);

        //  read property file for current platform
        Properties fileCapabilities = new Properties();
        String filePath = "./src/test/resources/appiumConfig/" + platformName + ".properties";
        fileCapabilities.load(new FileInputStream(filePath));

        //  add capabilities from resources
        fileCapabilities.forEach((k, v) -> capabilities.setCapability((String) k, v));

        //  add argument capabilities (they might overwrite file capabilities)
        capabilities.setCapability("app", appPath);
        setArgumentCapabilityIfPresent(capabilities, "platformVersion");
        setArgumentCapabilityIfPresent(capabilities, "deviceName");
        setArgumentCapabilityIfPresent(capabilities, "deviceId");
        capabilities.setCapability("noSign", true);
    }

    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            createDebugData();
            try {
                //embed screenshot if scenario failed
                byte[] screenshot = driver.getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", "errorScreenshot");
                //  embed page source
                scenario.attach(driver.getPageSource(), "text/plain", "pageSource");
                List<LogEntry> logEntries = driver.manage().logs().get("logcat").getAll();
                StringBuffer output = new StringBuffer();
                logEntries.forEach(logEntry -> output.append(logEntry.getLevel().toString()).
                        append(logEntry.getMessage()).append("\n"));
                scenario.log(output.toString());
            } catch (WebDriverException wdex) {
                System.err.println(wdex.getMessage());
            } catch (ClassCastException ccex) {
                ccex.printStackTrace();
            }
        }

        //quit the driver
        if (driver != null) {
            driver.quit();
            driver = null;
        }
        service.stop();
    }

    public void createDebugData() {
        createDebugData("");
    }

    public void createDebugData(String filesPrefix) {
        String localDebug = getTestParameter("localDebug", false);
        if (localDebug != null && localDebug.equals("true")) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd_HH.mm.ss");
            String formatDateTime = (filesPrefix != null ? filesPrefix + "_" : "")
                    + LocalDateTime.now().format(formatter);
            File screenshotFile = driver.getScreenshotAs(OutputType.FILE);
            String debugDir = "target/debug/";
            try {
                FileUtils.copyFile(screenshotFile, new File(debugDir + formatDateTime + "_screenshot.jpg"));
                PrintWriter out = new PrintWriter(debugDir + formatDateTime + "_pageSource.xml");
                out.println(driver.getPageSource());
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                File logFile = new File(debugDir + formatDateTime + "_logcat.log");
                PrintWriter logFileWritter = new PrintWriter(logFile);
                List<LogEntry> logEntries = driver.manage().logs().get("logcat").getAll();
                StringBuffer output = new StringBuffer();
                logEntries.forEach(logEntry -> output.append(logEntry.getLevel().toString()).
                        append(logEntry.getMessage()).append("\n"));
                logFileWritter.println(output.toString());
                logFileWritter.flush();
                logFileWritter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
