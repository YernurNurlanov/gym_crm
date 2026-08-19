Feature: Integration between Gym CRM and Trainer Workload services

  Background:
    Given the trainer workload database is clean
    And a trainee with username "string.string5" exists
    And a trainer with username "string.int" exists

  Scenario: Training creation updates trainer workload

    When a training for trainer "string.int" with duration 60 minutes is created

    Then the training creation request is successful

    And the workload is processed

    And trainer workload for "string.int" contains 60 minutes


  Scenario: Training deletion updates trainer workload

    When a training for trainer "string.int" with duration 60 minutes is created

    Then the training creation request is successful

    And the workload is processed

    And trainer workload for "string.int" contains 60 minutes

    When the created training is deleted

    Then the training deletion request is successful

    And the workload is processed

    And trainer workload for "string.int" contains 0 minutes


  Scenario: Training creation fails when trainer does not exist

    Given no trainer with username "unknown.trainer" exists

    When a training for trainer "unknown.trainer" with duration 60 minutes is created

    Then the training creation request fails