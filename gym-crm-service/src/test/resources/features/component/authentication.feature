Feature: Authentication

  Scenario: User logs in successfully
    Given valid login credentials
    When I login with username "john.doe" and password "password123"
    Then the response status should be 200
    And the response should contain username "john.doe"

  Scenario: Login with empty username
    When I login with username "" and password "password123"
    Then the response status should be 400

  Scenario: Login with invalid credentials
    Given authentication service rejects credentials
    When I login with username "john.doe" and password "wrong-password"
    Then the response status should be 401