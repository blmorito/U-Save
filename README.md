# U-Save
<p>Our entry for the U:Hack UnionBank hackathon in Cebu, 2017.</p>
<p>This placed 2nd Runner up on the SME Category.</p>
<a href="http://www.youtube.com/watch?feature=player_embedded&v=FtoemBd2iuE
" target="_blank"><img src="http://img.youtube.com/vi/FtoemBd2iuE/1.jpg" 
alt="Unionbank Hackathon held last July 15 and 16 at the Cebu Trade Hall, SM City Cebu" width="360" height="190" border="10" /></a>

## Brief Description
U-Save is an app for SMEs to provide a way for their customers to receive their change from their cash purchases to be digitally transferred to the customers' bank account. This helps encourage people to be more keen to saving money and avoid temptations to buy unnecessary things when going about their day to day life.

## How it Works
The U:Save app is provided to partner SMEs and this app will record the change/"sukli" of the customer. After inputting the change, the cashier/person in charge of the transaction will now require to scan the QR code that is in the customer's mobile banking account U:BANK(A fictional bank that we built to portray a mobile banking system, using U:Hack's API). 

After scanning, the U Save app will show the total amount to be transferred to the customer. The information contains the change, the convenience fee that will go to the store itself, and the total amount. After confirming, the transaction is done, the change now is transferred to the customer's bank savings account.

## Libraries used
- [OkHttp3](https://github.com/square/okhttp) - the http client library we used to communicate to our PHP U:Hack API Wrapper.
- [QRGen](https://github.com/kenglxn/QRGen) - the library we used to generate QR Codes on the app.
- [QReader](https://github.com/nisrulz/qreader) - used to scan and read the generated QR codes.
- U:Hack API - The exposed Web Service from UnionBank (organizers/host of this hackathon) that was made available entirely for this event.
We specifically used the RequestTestAccount, FundTransfer and AccountInformation APIs.

### Team Members
- Brylle Lyndon Orito (Lead)
- Wendel Madjus
- Mark Gindoy
- Jessie Anosa
- Rad Roa
- Daryl Alagdon
