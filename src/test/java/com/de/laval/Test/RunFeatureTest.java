package com.de.laval.Test;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features", glue = "com/de/laval/Test",
        plugin = {"pretty", "json:target/cucumber-reports/report.json"})
public class RunFeatureTest {

}
