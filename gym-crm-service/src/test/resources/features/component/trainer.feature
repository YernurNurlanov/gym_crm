Feature: Trainer management

  Scenario: Register trainer successfully
    Given valid trainer registration data
    When I register trainer with first name "John", last name "Doe" and specialization 1
    Then the response status should be 200
    And the response should contain "john.doe"

  Scenario: Get existing trainer
    Given trainer "john.doe" exists
    When I request trainer "john.doe"
    Then the response status should be 200
    And the response should contain "John"
    And the response should contain "Doe"

  Scenario: Get trainer that does not exist
    Given trainer "unknown" does not exist
    When I request trainer "unknown"
    Then the response status should be 404

  Scenario: Register trainer with invalid first name
    When I register trainer with empty first name
    Then the response status should be 400