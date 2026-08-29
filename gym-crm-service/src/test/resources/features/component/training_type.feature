Feature: Training type management

  Scenario: Get all training types
    Given training types exist
    When I request all training types
    Then the response status should be 200
    And the response should be a JSON array