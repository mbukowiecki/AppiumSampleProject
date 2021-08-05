@android
Feature: Home page elements

  Scenario: Demo mode
    Given I launch the app
    When I accept data access permission
    Then I see Home page is displayed

    When I go to Demo mode
    Then I see Demo page is displayed