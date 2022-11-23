# buggycarsrating
Test approach:
The given application Buggy cars rating (https://buggy.justtestit.org). The objective of this project is to find 2 critical bugs and test 5 major functionalities.

Below are 5 critical functionalities which is tested.

Login
Logout
Registration
Get Model details count (Provided column names like Rank oe Votes)
Voting function

Tools Used
Selenium Eclipse
TestNG
Java Commons
Maven Project

Bug Report:

1. Profile detail page remains open after user logout.

Steps to reproduce:
User Login to site
Go to Profile page
Hit Logout

Expected: User navigate to login home page
Actual: Profile details page remains open after logout

2. User name not populated after user successfully vote.

Steps to reproduce:
User Login to site
click on car model
Inser comment and click on Vote
Vote successfully done and count increased


Expected: User name should populate in grid with Comments
Actual: User name not populating

3. Logout Function is not properly working

Steps to reproduce:
User Login to site
click on car model
click on Logout

Expected: User should logout successfully
Actual: User remains login
