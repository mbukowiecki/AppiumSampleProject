package com.de.laval.Test;

import com.de.laval.Pages.HomePage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

public class HomeStepDefinition {

    StepDefinitionAdapter stepDefinitionAdapter;

    public HomeStepDefinition (StepDefinitionAdapter stepDefinitionAdapter){
        this.stepDefinitionAdapter = stepDefinitionAdapter;
    }

    private HomePage getPage(){
        return stepDefinitionAdapter.getHomePage();
    }

    @Given("^I launch the app$")
    public void iLaunchTheApp() throws Exception {
        stepDefinitionAdapter.runApp();
    }

    @When("I accept data access permission")
    public void iAcceptDataAccessPermission() {
        getPage().getAllowAccessToDataButton().click();
    }

    @Then("I see Home page is displayed")
    public void iSeeHomePageIsDisplayed() {
        Assert.assertTrue("Home page has not been displayed", getPage().getRegisterButton().isDisplayed());
    }

    @When("I enter activation code")
    public void iEnterActivationCode() {
        getPage().getActivationCodeInput().sendKeys("1234567");
        getPage().getRegisterButton().click();
    }
}
