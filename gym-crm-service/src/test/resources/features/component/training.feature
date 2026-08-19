Feature: Training management

  Scenario: Add training successfully
    Given valid training data
    When I add a training
    Then the response status should be 200

  Scenario: Delete training successfully
    When I delete training with id 1
    Then the response status should be 200

  Scenario: Add training with invalid trainee username
    When I add a training with empty trainee username
    Then the response status should be 400