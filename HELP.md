SandBox Banking Application

Database Schema
=======================================
Table 1:Account--Account Master Table to store Customer Account Information along with Account Balance.
Table 2:Transaction--To Store information for each transaction.

H2 CONSOLE-SEED DATA
===========================================
H2 Console: http://localhost:8080/h2-console/
JDBC URL:jdbc:h2:mem:bankDB



Rest Endpoints
======================================
1]Get Account Info-->[GET]http://localhost:8080/account/<accountId>
Eg:http://localhost:8080/account/222

2]Transfer Money-->[POST]http://localhost:8080/moneyTransfer
Sample Request:
{
	"senderAccountId":222,
	"receiverAccountId":333,
	"amount":205
}

Sample Response:
{
    "response": "Insufficient Balance"
}

H2 Console: http://localhost:8080/h2-console/
