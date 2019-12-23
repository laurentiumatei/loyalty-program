# loyalty-program
Simple Spring Boot REST service for acquiring points corresponding to the money spent in a transaction.

## Usage

**Add a new transaction:**
- POST http://host:8080/api/transaction

JSON in the body of the request:

    {
        "customerId": "customerId1",
        "value": 2000,
        "fundSource": "CASH"
    }
    
Field details:

 - *customerId*: the id of the customer of the transaction
 - *value*: money value of the transaction
 - *fundSource*: source of funds for the transaction: CASH - when transaction is payed with real money, WALLET - when transaction is payed with money converted from available points

**Get transaction history for a customer id:**
- GET: http://host:8080/api/history/{customerId}

**Get the balance for a customer id:**
- GET: http://localhost:8080/api/balance/{customerId}

**Trigger allocation of available points from pending points - for testing purposes only:**
- GET: http://localhost:8080/api/allocateAvailablePoints

Note:
This command should be used only for testing purpose. Automatic triggering should take place every Sunday at 23:00.

## Known issues
- Although the logic for allocating available points from pending point is correct, on my machine Spring does not trigger it automatically. I might not have find the correct Spring cron expression.
- There is no proper error handling with proper HTTP Status codes.
- There is no input validation for adding a transaction.
