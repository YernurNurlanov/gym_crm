Feature: Trainer workload management

  Scenario: Add training workload for a new trainer
    Given a new trainer workload request:
      | username  | john.doe |
      | firstName | John     |
      | lastName  | Doe      |
      | active    | true     |
      | date      | 2026-08-15 |
      | duration  | 60       |
      | action    | ADD      |
    And the trainer does not exist
    When the trainer workload message is received
    Then the trainer workload is saved
    And the saved trainer has username "john.doe"
    And the saved trainer has first name "John"
    And the saved trainer has last name "Doe"
    And the saved trainer is active
    And the saved workload for year 2026 and month AUGUST is 60 minutes


  Scenario: Add another training to existing trainer
    Given an existing trainer "john.doe" with workload 60 minutes for AUGUST 2026
    And a new trainer workload request:
      | username  | john.doe |
      | firstName | John     |
      | lastName  | Doe      |
      | active    | true     |
      | date      | 2026-08-20 |
      | duration  | 90       |
      | action    | ADD      |
    When the trainer workload message is received
    Then the trainer workload is saved
    And the saved workload for year 2026 and month AUGUST is 150 minutes


  Scenario: Delete training workload
    Given an existing trainer "john.doe" with workload 150 minutes for AUGUST 2026
    And a new trainer workload request:
      | username  | john.doe |
      | firstName | John     |
      | lastName  | Doe      |
      | active    | true     |
      | date      | 2026-08-20 |
      | duration  | 90       |
      | action    | DELETE  |
    When the trainer workload message is received
    Then the trainer workload is saved
    And the saved workload for year 2026 and month AUGUST is 60 minutes


  Scenario: Reject workload deletion when duration becomes negative
    Given an existing trainer "john.doe" with workload 60 minutes for AUGUST 2026
    And a new trainer workload request:
      | username  | john.doe |
      | firstName | John     |
      | lastName  | Doe      |
      | active    | true     |
      | date      | 2026-08-20 |
      | duration  | 90       |
      | action    | DELETE  |
    When the trainer workload message is received
    Then the workload update fails with message "Monthly workload cannot be negative."
    And the trainer workload is not saved