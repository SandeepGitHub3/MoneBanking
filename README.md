# MoneBanking
Sanbox Banking Application

Database Schema
------------------------------------
Table 1:Account--Account Master Table to store Customer Account Information along with Account Balance.
Table 2:Transaction--To Store information for each transaction.

H2 Console-Seed Data
------------------------------------
H2 Console: http://localhost:8080/h2-console/

JDBC URL:jdbc:h2:mem:bankDB

ACCOUNT_ID  	ACCOUNT_HOLDER  	BALANCE  
111	            Miss Marple     	1000.00
222	            Sherlock Holmes	    200.00
333	            Hercule Poirot	    300.00

Application Launch
---------------------------------------
Run Launcher class---->BankingApplication

Rest End-points
======================================
1]Get Account Info-->[GET]http://localhost:8080/account/<accountId>

Eg:http://localhost:8080/account/222

Sample Response:
{
    "account": {
        "accountId": 222,
        "accountHolder": "Sherlock Holmes",
        "balance": 0
    },
    "transactions": [
        {
            "transactionId": 1,
            "accountId": 222,
            "transactionType": "DEBIT",
            "amount": 200,
            "transactionDate": "2019-05-25"
        }
    ]
}

2]Transfer Money-->[POST]http://localhost:8080/moneyTransfer
Sample Request:
{
	"senderAccountId":222,
	"receiverAccountId":333,
	"amount":205
}

Sample Response:
{
    "response": "Money Transferred Successfully"
}

Further scope:
-------------------
1. Juints to be added for each individual classes.
2. Add Authentication/Authorization Support.
3.Add validation checks for Requests to guard against Bad Requests.
4.Add DB level Foreign key and Unique Constraints
5.Race condition checks.
