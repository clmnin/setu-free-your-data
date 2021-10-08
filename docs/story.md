# Story

## Flow

- [ ] Add Firebase
- [ ] Firebase crashalytics
- [ ] Firebase auth
- [ ] Phone
- [ ] OTP
- [ ] Create company
- [ ] On boarding screen for AA
- [ ] Link your bank accounts
- [ ] Dashboard for each company
  - [ ] Bank Balance
  - [ ] Receivable
  - [ ] Payable
- [ ] 




## Restaurant 1

| Date        | Party        | Narration  | Debit  | Credit | Total          |
|---|---|---|---|---|---|
| 2021-09-13  | Wholesaler 1 | Purchase   |        | 50000  | 50000 Payable  |
| 2021-09-20  | Wholesaler 1 | IOU        |  50000 |        |                |
| 2021-09-20  | Wholesaler 1 | IOU Failed |        | 50000  |                |
| 2021-09-23  | Wholesaler 1 | FT         |  50000 |        | 0              |
| 2021-09-24  | Wholesaler 1 | Purchase   |        | 1806   | 1806 Payable   |
| 2021-09-24  | Wholesaler 1 | UPI        |  1806  |        | 0              |
| 2021-09-25  | Wholesaler 1 | Purchase   |        | 2612   | 2612 Payable   |
| 2021-09-25  | Wholesaler 1 | Cash       |  2612  |        | 0              |
| 2021-09-26  | Wholesaler 1 | Purchase   |        | 50000  | 50000 Payable  |
| 2021-09-26  | Wholesaler 2 | Purchase   |        | 2613   | 50000 Payable  |
| 2021-09-26  | Wholesaler 2 | UPI        |  2612  |        | 0 Payable      |
| 2021-09-27  | Wholesaler 2 | Purchase   |        | 2500   | 50000 Payable  |
| 2021-09-27  | Wholesaler 2 | UPI        |  2500  |        | 0 Payable      |
| 2021-09-28  | Wholesaler 2 | Purchase   |        | 50000  | 50000 Payable  |
| 2021-09-29  | Wholesaler 1 | IOU        |  50000 |        | 0              |
| 2021-09-29  | Wholesaler 1 | IOU Failed |        | 50000  | 50000 Payable  |
| 2021-10-04  | Wholesaler 2 | IOU        |  50000 |        |                |
| 2021-10-04  | Wholesaler 2 | IOU Failed |        | 50000  | 50000 Payable  |
| Total       |              |            |        |        |100000 Payable  |


## Restaurant 2

| Date        | Party        | Narration  | Debit  | Credit | Total          |
|---|---|---|---|---|---|
| 2021-09-13  | Wholesaler 2 | Purchase   |        | 50000  | 50000 Payable  |
| 2021-09-20  | Wholesaler 2 | IOU        |  50000 |        |                |
| 2021-09-20  | Wholesaler 2 | FT         |  50000 |        | 0              |
| 2021-09-22  | Wholesaler 2 | Purchase   |        | 50000  | 50000 Payable  |
| 2021-09-29  | Wholesaler 2 | IOU        |  50000 |        |                |
| 2021-09-29  | Wholesaler 2 | FT         |  50000 |        | 0              |
| 2021-09-29  | Wholesaler 2 | Purchase   |        | 50000  | 50000 Payable  |
| 2021-09-30  | Wholesaler 1 | Purchase   |        | 20000  | 50000 Payable  |
| 2021-10-06  | Wholesaler 2 | IOU        |  50000 |        |                |
| 2021-10-06  | Wholesaler 2 | FT         |  50000 |        | 0              |
| 2021-10-07  | Wholesaler 1 | IOU        |  20000 |        |                |
| 2021-10-07  | Wholesaler 1 | FT         |  20000 |        | 0              |
| Total       |              |            |        |        | 0              |

## Wholesaler 1

| Date        | Party        | Narration  | Debit  | Credit | Total          |
|---|---|---|---|---|---|
| 2021-09-13  | Restaurant 1 | Sale       |  50000 |        | 50000 Receive  |
| 2021-09-20  | Restaurant 1 | IOU        |        | 50000  |                |
| 2021-09-20  | Restaurant 1 | IOU Failed |  50000 |        |                |
| 2021-09-24  | Restaurant 1 | FT         |        | 50000  | 0              |
| 2021-09-24  | Restaurant 1 | Sale       |  1806  |        | 1806 Receive   |
| 2021-09-24  | Restaurant 1 | UPI        |        | 1806   | 0              |
| 2021-09-25  | Restaurant 1 | Sale       |  2612  |        | 2612 Receive   |
| 2021-09-25  | Restaurant 1 | Cash       |        | 2612   | 0 Payable      |
| 2021-09-26  | Restaurant 1 | Sale       |  50000 |        | 50000 Receive  |
| 2021-09-29  | Restaurant 1 | IOU        |        | 50000  |                |
| 2021-09-29  | Restaurant 1 | IOU Failed |  50000 |        |                |
| 2021-09-30  | Restaurant 2 | Sale       |  20000 |        | 70000 Receive  |
| 2021-09-07  | Restaurant 2 | IOU        |        | 20000  |                |
| 2021-09-07  | Restaurant 2 | FT         |        | 20000  | 50000 Receive  |
| Total       |              |            |        |        | 50000 Receive  |

## Wholesaler 2

| Date        | Party        | Narration  | Debit  | Credit | Total          |
|---|---|---|---|---|---|
| 2021-09-26  | Restaurant 1 | Purchase   |  2613  |        | 2613 Receive   |
| 2021-09-26  | Restaurant 1 | Cash       |        | 2612   | 0              |
| 2021-09-27  | Restaurant 1 | Purchase   |  3000  |        | 3000 Receive   |
| 2021-09-27  | Restaurant 1 | Cash       |        | 3000   | 0              |
| 2021-09-28  | Restaurant 1 | Purchase   |  50000 |        | 50000 Receive  |
| 2021-10-04  | Restaurant 1 | IOU        |        | 50000  |                |
| 2021-10-04  | Restaurant 1 | IOU Failed |  50000 |        |                |
| 2021-09-13  | Restaurant 2 | Purchase   |  50000 |        | 50000 Receive  |
| 2021-09-20  | Restaurant 2 | IOU        |        | 50000  |                |
| 2021-09-20  | Restaurant 2 | FT         |        | 50000  | 0              |
| 2021-09-22  | Restaurant 2 | Purchase   |  50000 |        | 50000 Receive  |
| 2021-09-29  | Restaurant 2 | IOU        |        | 50000  |                |
| 2021-09-29  | Restaurant 2 | FT         |        | 50000  | 0              |
| 2021-09-29  | Restaurant 2 | Purchase   |  50000 |        | 50000 Receive  |
| 2021-10-06  | Restaurant 2 | IOU        |        | 50000  |                |
| 2021-10-06  | Restaurant 2 | FT         |        | 50000  | 0              |
| Total       |              |            |        |        | 50000 Receive  |