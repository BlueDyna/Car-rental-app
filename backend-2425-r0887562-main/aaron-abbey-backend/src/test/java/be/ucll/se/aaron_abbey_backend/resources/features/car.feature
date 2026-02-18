Feature: Cars

    Scenario: Add a car
        Given I have an owner token
        When I add a car
        Then the car is added
        
    Scenario: Add a car with a license plate that already exists
        Given I have an admin token
        When I add a car for the first time
        And I add a same car with the same license plate
        Then an error is thrown
    
    Scenario: Add a car with a renter
        Given I have a renter token
        When I add a car with a renter
        Then a bad request is thrown


    Scenario: Add a car without token
        When I add a car without a token
        Then an error is thrown
    
