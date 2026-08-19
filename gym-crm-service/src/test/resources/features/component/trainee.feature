Feature: Trainee management

  Scenario: Register trainee successfully
    Given valid trainee registration data
    When I register trainee with first name "John" and last name "Trainee"
    Then the response status should be 200
    And the response should contain "john.trainee"

  Scenario: Get existing trainee
    Given trainee "john.trainee" exists
    When I request trainee "john.trainee"
    Then the response status should be 200
    And the response should contain "John"
    And the response should contain "Trainee"

  Scenario: Get trainee that does not exist
    Given trainee "unknown" does not exist
    When I request trainee "unknown"
    Then the response status should be 404