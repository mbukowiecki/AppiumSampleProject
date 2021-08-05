package com.de.laval.Test;

import com.de.laval.Extensions.Extensions;
import com.de.laval.Pages.DemoPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.junit.Assert;

public class DemoStepDefinition implements Extensions {

    StepDefinitionAdapter stepDefinitionAdapter;

    public DemoStepDefinition (StepDefinitionAdapter stepDefinitionAdapter){
        this.stepDefinitionAdapter = stepDefinitionAdapter;
    }

    private DemoPage getPage(){
        return stepDefinitionAdapter.getDemoPage();
    }

    @Given("^I go to Demo mode$")
    public void iGoToDemoMode(){
        stepDefinitionAdapter.getHomePage().getDemoModeButton().click();
    }

    @Then("I see Demo page is displayed")
    public void iSeeDemoViewOpens() {
        Assert.assertTrue("Demo view didn't open", elementIsVisibleUntilTime(getPage().getSearch(), 10));
    }
}
