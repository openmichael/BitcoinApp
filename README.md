# BitcoinApp
This is a sample Android app demonstrating bitcoin price by using coindesk API to retrieve data and analyze. 
The app retrieves price data from the website API and store it to the android sqlite database.

App features:

1.	Retrieve current price:
The app retrieve and display current bitcoin price from a website API.
The daily maximum and daily minimum price and percentage is also displayed on the screen.

2.	Save price data:
The current price data retrieve from the website API is saved to the database.

3.	Pop-up alert:
This app will show a pop-up alert if the price percentage change is more than 1% in 5, 30, 60 minute.

4.	Price chart:
Two charts with daily and monthly price data will be shown in the daily chart fragment and monthly chart fragment by using a MPAndroid chart library.

5.	Show current news:
Current news title are retrieved from coindesk.com website using JSoup library. 
Due to how the website is constructed, only some of the news can be clicked and linked to the same webpage.  

6.	Database function:
A monthly price data will be retrieved from website API and stored into database when the app is first created.
All the price data retrieved from the website will be stored into the database. 
The background service will analyze the data and perform on the UI.

This is only a sample app. The UI is not fully embellished and more functions features can be added.
Please feel free to contact me if you have any other questions.
