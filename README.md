# EZ Ledger

SMEs in India are mostly unorganized and have failed to attract major software providers to build them affordable solutions. Softwares like Tally, even though is a product built with great product focus fail to be internet friendly and so have missed out on many feature a modern businesses owner needs.
New initiatives like [UPI](https://www.npci.org.in/what-we-do/upi/product-overview) and [Sahamati](https://sahamati.org.in/) have the potential to solve many probles SMEs face, which are moslty caused due to systemic problems in how Indian businesses are run.

## About the app

We are building an app to help SMEs keep track of their transactions and also help them identify bad or credit risk customers, who may default in their business credit.
Our app helps SMEs track their ledgers, track bank transactions and automatically fill transaction data and also identify bad credit worty users/customers by using both financial data from account aggregators and by usage of the app. By using the app we can identify defaulters on credits and mark them as credit risk.

## Screenshots

Login             |  List of businesses | Bank transactions | Accounts ledger
:-------------------------:|:-------------------------:|:-------------------------:|:-------------------------:
<img src="./docs/1.jpg" alt="Login" width="200"/>  |  <img src="./docs/2.jpg" alt="List of businesses" width="200"/> | <img src="./docs/3.jpg" alt="Bank transactions" width="200"/> | <img src="./docs/4.jpg" alt="Login" width="200"/>

## Tech stack

### Android
* Minimum SDK level 26
* 100% Kotlin based + Coroutines + Flow for asynchronous
* Hilt for dependency injection.
* JetPack
  * Compose - A modern toolkit for building native Android UI.
  * Lifecycle - dispose observing data when lifecycle state changes.
  * ViewModel - UI related data holder, lifecycle aware.
  * Room Persistence - construct database.
* Architecture
  * MVVM Architecture (Declarative View - ViewModel - Model)

### Backend
* Python with types hints
* FastAPI
* [Edgedb](https://www.edgedb.com/)
  * Next-Gen database
  * A whole new language to interact with the database - [EdgeQL](https://www.edgedb.com/docs/edgeql/index)
  * High performance queries, effortlessly
  * Build in migrations
