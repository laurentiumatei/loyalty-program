# loyalty-program
Spring Boot based REST service to simulate a loyalty program based on money spent with a financial entity. A customer aquires points at each transaction, based on the money spent for that transaction.

## How a customer can acquire new points:
Every euro spent on one transaction will give the customer:
- 1 pending point for every euro until 5000 euro value of transaction
- 2 pending points for every euro from 5001 euro to 7500 euro value of transaction
- 3 pending points from 7501 euro value of transaction

New pending points become available points for use at the end of every week if:
- the customer has spent at least 500 euro that week
- at least one transaction exists for that customer on every day of the week

A user will lose all the points if no transaction was made in the last 5 weeks.

## How a customer can use the available points:
User can use some or all available points. Every point is worth 1 eurocent.

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
