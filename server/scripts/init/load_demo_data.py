import asyncio
from edgedb import AsyncIOConnection

from scripts.init.checkdb import check_db, logger

from typing import AsyncContextManager


async def create_aas(con: AsyncIOConnection) -> None:
    async for tx in con.retrying_transaction():
        async with tx:
            # create AA for 9999999999 (Good Restaurant)
            await tx.execute("""
            INSERT AAAccount {
                phone := "9999999999", 
                name := "Good Restaurant", 
                email := "good.restaurant@gmail.com",
                dob := to_datetime('1970-07-24T23:59:59+05:30'),
                pan := "AAAPL1234C",
                fi_deposit := (
                    INSERT Deposit {
                        maskedAccNumber := "XXXXXXX123",
                        type_ := DepositAccType.CURRENT,
                        branch := "Jayanagar 7th Block",
                        ifscCode := "APNB0001154",
                        micrCode := "500245646",
                        transactions := {
                            (INSERT TransactionLine {
                                    mode := "FT",
                                    trans_type := TransactionType.CREDIT,
                                    txnId := "M61530891",
                                    amount := 60000,
                                    text := "Swiggy",
                                    narration := "MMT/IMPS/009900356311/Swiggy/Swiggy/HDFC0000847",
                                    reference := "RFN02209245",
                                    valueDate := to_datetime('2021-09-19' ++ 'T23:59:59+05:30'),
                                    currentBalance := 0,
                                    transactionTimestamp := to_datetime('2021-09-19T23:09:17+05:30')
                            }),
                            (INSERT TransactionLine {
                                mode := "FT",
                                trans_type := TransactionType.DEBIT,
                                txnId := "M61530892",
                                amount := 50000,
                                text := "WholeSaler 2",
                                narration := "MMT/IMPS/009900356311/payment/SAILAKSHMI/HDFC0000847",
                                reference := "RFN02221651",
                                valueDate := to_datetime('2021-09-20' ++ 'T23:59:59+05:30'),
                                currentBalance := 0,
                                transactionTimestamp := to_datetime('2021-09-20T23:14:28+05:30')
                            }),
                            (INSERT TransactionLine {
                                mode := "FT",
                                trans_type := TransactionType.CREDIT,
                                txnId := "M61530893",
                                amount := 60000,
                                text := "Swiggy",
                                narration := "MMT/IMPS/009900356311/Swiggy/Swiggy/HDFC0000847",
                                reference := "RFN02209245",
                                valueDate := to_datetime('2021-09-28' ++ 'T23:59:59+05:30'),
                                currentBalance := 10000,
                                transactionTimestamp := to_datetime('2021-09-28T23:09:17+05:30')
                            }),
                            (INSERT TransactionLine {
                                mode := "FT",
                                trans_type := TransactionType.DEBIT,
                                txnId := "M61530894",
                                amount := 50000,
                                text := "WholeSaler 2",
                                narration := "MMT/IMPS/009900356311/payment/SAILAKSHMI/HDFC0000847 ",
                                reference := "RFN02221651",
                                valueDate := to_datetime('2021-09-29' ++ 'T23:59:59+05:30'),
                                currentBalance := 70000,
                                transactionTimestamp := to_datetime('2021-09-29T23:14:28+05:30')
                            }),
                            (INSERT TransactionLine {
                                mode := "FT",
                                trans_type := TransactionType.CREDIT,
                                txnId := "M61530895",
                                amount := 60000,
                                text := "Swiggy",
                                narration := "MMT/IMPS/009900356311/Swiggy/Swiggy/HDFC0000847",
                                reference := "RFN02209245",
                                valueDate := to_datetime('2021-10-05' ++ 'T23:59:59+05:30'),
                                currentBalance := 20000,
                                transactionTimestamp := to_datetime('2021-10-05T23:09:17+05:30')
                            }),
                            (INSERT TransactionLine {
                                mode := "FT",
                                trans_type := TransactionType.DEBIT,
                                txnId := "M61530896",
                                amount := 50000,
                                text := "WholeSaler 2",
                                narration := "MMT/IMPS/009900356311/payment/SAILAKSHMI/HDFC0000847 ",
                                reference := "RFN02221651",
                                valueDate := to_datetime('2021-10-06' ++ 'T23:59:59+05:30'),
                                currentBalance := 80000,
                                transactionTimestamp := to_datetime('2021-10-06T23:14:28+05:30')
                            }),
                            (INSERT TransactionLine {
                                mode := "FT",
                                trans_type := TransactionType.DEBIT,
                                txnId := "M61530897",
                                amount := 20000,
                                text := "WholeSaler 1",
                                narration := "MMT/IMPS/009900356311/payment/SAILAKSHMI/HDFC0000847 ",
                                reference := "RFN02221651",
                                valueDate := to_datetime('2021-10-07' ++ 'T23:59:59+05:30'),
                                currentBalance := 30000,
                                transactionTimestamp := to_datetime('2021-10-07T23:14:28+05:30')
                            }),

                        }
                    }
                ),
                fi_term := (
                    INSERT TermDeposit {
                        maskedAccNumber := "XXXXXXX123",
                        ifsc := "APNB0001154",
                        branch := "Jayanagar 7th Block",
                        tenureDays := 180,
                        accountType := "FIXED",
                        currentValue := 11984590
                    }
                ),
                fi_recurring := (
                    INSERT RecurringDeposit {
                        maskedAccNumber := "XXXXXXX123",
                        ifsc := "APNB0001154",
                        branch := "Jayanagar 7th Block",
                        tenureDays := 180,
                        accountType := "FIXED",
                        currentValue := 11984590
                    }
                ),
                fi_credit_card := (
                    INSERT CreditCard {
                        maskedAccNumber := "XXXXXXX123",
                        dueDate := to_datetime('2021-10-20T13:20:00+05:30'),
                        cashLimit := 20000,
                        currentDue := 3000,
                        creditLimit := 60000,
                        totalDueAmount := 9756,
                        availableCredit := 51345,
                        previousDueAmount := 7654,
                        lastStatementDate := to_datetime('2021-05-20T13:20:00+05:30'),

                    }
                ),
                fi_insurance := (
                    INSERT Insurance {
                        maskedAccNumber := "XXXXXXX789",
                        coverType := "BUSINESS",
                        sumAssured := 1500000,
                        tenureYears := 12,
                        tenureMonths := 144,
                        policyEndDate := to_datetime('2030-05-20T13:20:00+05:30')

                    }
                )
            }
            """)
            # create AA for 9999999998 (Bad Restaurant)
            await tx.execute("""
            INSERT AAAccount {
                phone := "9999999998", 
                name := "Bad Restaurant", 
                email := "bad.restaurant@gmail.com",
                dob := to_datetime('1970-07-24T23:59:59+05:30'),
                pan := "AAAPL1234C",
                fi_deposit := (
                    INSERT Deposit {
                        maskedAccNumber := "XXXXXXX123",
                        type_ := DepositAccType.CURRENT,
                        branch := "Jayanagar 7th Block",
                        ifscCode := "APNB0001154",
                        micrCode := "500245646",
                        transactions := {
                            (INSERT TransactionLine {
                                mode := "FT",
                                trans_type := TransactionType.CREDIT,
                                txnId := "M61530881",
                                amount := 60000,
                                text := "Swiggy",
                                narration := "MMT/IMPS/009900356311/Swiggy/Swiggy/HDFC0000847",
                                reference := "RFN02209245",
                                valueDate := to_datetime('2021-09-23' ++ 'T20:59:59+05:30'),
                                currentBalance := 0,
                                transactionTimestamp := to_datetime('2021-09-23T20:09:17+05:30')
                            }),
                            (INSERT TransactionLine {
                                mode := "FT",
                                trans_type := TransactionType.DEBIT,
                                txnId := "M61530882",
                                amount := 50000,
                                text := "WholeSaler 1",
                                narration := "MMT/IMPS/009900356311/payment/SAILAKSHMI/HDFC0000847",
                                reference := "RFN02209245",
                                valueDate := to_datetime('2021-09-23' ++ 'T23:59:59+05:30'),
                                currentBalance := 60000,
                                transactionTimestamp := to_datetime('2021-09-23T23:09:17+05:30')
                            }),
                            (INSERT TransactionLine {
                                mode := "UPI",
                                trans_type := TransactionType.DEBIT,
                                txnId := "M61530883",
                                amount := 1806,
                                text := "WholeSaler 1",
                                narration := "UPI/935314560764/wholesaler1/wholesaler1@axisbank/Axis Bank",
                                reference := "RFN02209245",
                                valueDate := to_datetime('2021-09-24' ++ 'T23:59:59+05:30'),
                                currentBalance := 10000,
                                transactionTimestamp := to_datetime('2021-09-24T13:20:14+05:30')
                            }),
                            (INSERT TransactionLine {
                                mode := "OTHER",
                                trans_type := TransactionType.DEBIT,
                                txnId := "M61530884",
                                amount := 3000,
                                text := "ATM",
                                narration := "ATM Withdrawal",
                                reference := "RFN02172080",
                                valueDate := to_datetime('2021-09-25' ++ 'T23:59:59+05:30'),
                                currentBalance := 8194,
                                transactionTimestamp := to_datetime('2021-09-25T14:47:18+05:30')
                            }),
                            (INSERT TransactionLine {
                                mode := "UPI",
                                trans_type := TransactionType.DEBIT,
                                txnId := "M61530885",
                                amount := 2612,
                                text := "WholeSaler 2",
                                narration := "UPI/935314560764/wholesaler2/wholesaler2@axisbank/Axis Bank",
                                reference := "RFN02172080",
                                valueDate := to_datetime('2021-09-26' ++ 'T23:59:59+05:30'),
                                currentBalance := 5194,
                                transactionTimestamp := to_datetime('2021-09-26T14:47:18+05:30')
                            }),
                            (INSERT TransactionLine {
                                mode := "UPI",
                                trans_type := TransactionType.DEBIT,
                                txnId := "M61530886",
                                amount := 2500,
                                text := "WholeSaler 2",
                                narration := "UPI/935314560764/wholesaler2/wholesaler2@axisbank/Axis Bank",
                                reference := "RFN02172080",
                                valueDate := to_datetime('2021-09-27' ++ 'T23:59:59+05:30'),
                                currentBalance := 2582,
                                transactionTimestamp := to_datetime('2021-09-27T14:47:18+05:30')
                            }),
                        }
                    }
                )
            }
            """)
            # create AA for 9999999997 (Wholesaler 1)
            await tx.execute("""
            INSERT AAAccount {
                phone := "9999999997", 
                name := "Wholesaler 1", 
                email := "wholesaler1@gmail.com",
                dob := to_datetime('1970-07-24T23:59:59+05:30'),
                pan := "AAAPL1234C",
                fi_deposit := (
                    INSERT Deposit {
                        maskedAccNumber := "XXXXXXX123",
                        type_ := DepositAccType.CURRENT,
                        branch := "Jayanagar 7th Block",
                        ifscCode := "APNB0001154",
                        micrCode := "500245646",
                        transactions := {
                            (INSERT TransactionLine {
                                mode := "FT",
                                trans_type := TransactionType.CREDIT,
                                txnId := "M61530882",
                                amount := 50000,
                                text := "Restaurant 1",
                                narration := "MMT/IMPS/009900356311/payment/SAILAKSHMI/HDFC0000847",
                                reference := "RFN02209245",
                                valueDate := to_datetime('2021-09-23' ++ 'T23:59:59+05:30'),
                                currentBalance := 1200000,
                                transactionTimestamp := to_datetime('2021-09-23T23:14:28+05:30')
                            }),
                            (INSERT TransactionLine {
                                mode := "UPI",
                                trans_type := TransactionType.CREDIT,
                                txnId := "M61530883",
                                amount := 1806,
                                text := "Restaurant 1",
                                narration := "UPI/935314560764/restaurant1/restaurant1@axisbank/Axis Bank",
                                reference := "RFN00013383",
                                valueDate := to_datetime('2021-09-24' ++ 'T23:59:59+05:30'),
                                currentBalance := 1250000,
                                transactionTimestamp := to_datetime('2021-09-24T13:20:14+05:30')
                            }),
                            (INSERT TransactionLine {
                                mode := "FT",
                                trans_type := TransactionType.CREDIT,
                                txnId := "M61530897",
                                amount := 20000,
                                text := "Restaurant 2",
                                narration := "MMT/IMPS/009900356311/payment/SAILAKSHMI/HDFC0000847",
                                reference := "RFN02221651",
                                valueDate := to_datetime('2021-10-07' ++ 'T23:59:59+05:30'),
                                currentBalance := 1270000,
                                transactionTimestamp := to_datetime('2021-10-07T13:20:14+05:30')
                            })
                        }
                    }
                ),
                fi_term := (
                    INSERT TermDeposit {
                        maskedAccNumber := "XXXXXXX123",
                        ifsc := "APNB0001154",
                        branch := "Jayanagar 7th Block",
                        tenureDays := 180,
                        accountType := "FIXED",
                        currentValue := 11984590
                    }
                ),
                fi_recurring := (
                    INSERT RecurringDeposit {
                        maskedAccNumber := "XXXXXXX123",
                        ifsc := "APNB0001154",
                        branch := "Jayanagar 7th Block",
                        tenureDays := 180,
                        accountType := "FIXED",
                        currentValue := 11984590
                    }
                ),
                fi_credit_card := (
                    INSERT CreditCard {
                        maskedAccNumber := "XXXXXXX123",
                        dueDate := to_datetime('2021-10-20T13:20:00+05:30'),
                        cashLimit := 20000,
                        currentDue := 3000,
                        creditLimit := 60000,
                        totalDueAmount := 9756,
                        availableCredit := 51345,
                        previousDueAmount := 7654,
                        lastStatementDate := to_datetime('2021-05-20T13:20:00+05:30'),

                    }
                ),
                fi_insurance := (
                    INSERT Insurance {
                        maskedAccNumber := "XXXXXXX789",
                        coverType := "BUSINESS",
                        sumAssured := 1500000,
                        tenureYears := 12,
                        tenureMonths := 144,
                        policyEndDate := to_datetime('2030-05-20T13:20:00+05:30')

                    }
                )
            }
            """)
            # create AA for 9999999996 (Wholesaler 2)
            await tx.execute("""
            INSERT AAAccount {
                phone := "9999999996", 
                name := "Wholesaler 2", 
                email := "wholesaler2@gmail.com",
                dob := to_datetime('1970-07-24T23:59:59+05:30'),
                pan := "AAAPL1234C",
                fi_deposit := (
                    INSERT Deposit {
                        maskedAccNumber := "XXXXXXX123",
                        type_ := DepositAccType.CURRENT,
                        branch := "Jayanagar 7th Block",
                        ifscCode := "APNB0001154",
                        micrCode := "500245646",
                        transactions := {
                            (INSERT TransactionLine {
                                mode := "FT",
                                trans_type := TransactionType.CREDIT,
                                txnId := "M61530892",
                                amount := 50000,
                                text := "Restaurant 2",
                                narration := "MMT/IMPS/009900356311/payment/SAILAKSHMI/HDFC0000847",
                                reference := "RFN02221651",
                                valueDate := to_datetime('2021-09-20' ++ 'T23:59:59+05:30'),
                                currentBalance := 60000,
                                transactionTimestamp := to_datetime('2021-09-20T23:14:28+05:30')
                            }),
                            (INSERT TransactionLine {
                                mode := "UPI",
                                trans_type := TransactionType.CREDIT,
                                txnId := "M61530885",
                                amount := 2612,
                                text := "Restaurant 1",
                                narration := "MMT/IMPS/009900356311/payment/SAILAKSHMI/HDFC0000847",
                                reference := "RFN02221651",
                                valueDate := to_datetime('2021-09-26' ++ 'T23:59:59+05:30'),
                                currentBalance := 110000,
                                transactionTimestamp := to_datetime('2021-09-26T23:14:28+05:30')
                            }),
                            (INSERT TransactionLine {
                                mode := "UPI",
                                trans_type := TransactionType.CREDIT,
                                txnId := "M61530886",
                                amount := 2500,
                                text := "Restaurant 1",
                                narration := "MMT/IMPS/009900356311/payment/SAILAKSHMI/HDFC0000847",
                                reference := "RFN02221651",
                                valueDate := to_datetime('2021-09-27' ++ 'T23:59:59+05:30'),
                                currentBalance := 112612,
                                transactionTimestamp := to_datetime('2021-09-27T23:14:28+05:30')
                            }),
                            (INSERT TransactionLine {
                                mode := "FT",
                                trans_type := TransactionType.CREDIT,
                                txnId := "M61530894",
                                amount := 50000,
                                text := "Restaurant 2",
                                narration := "MMT/IMPS/009900356311/payment/SAILAKSHMI/HDFC0000847",
                                reference := "RFN02221651",
                                valueDate := to_datetime('2021-09-29' ++ 'T23:59:59+05:30'),
                                currentBalance := 115112,
                                transactionTimestamp := to_datetime('2021-09-29T23:14:28+05:30')
                            }),
                            (INSERT TransactionLine {
                                mode := "FT",
                                trans_type := TransactionType.CREDIT,
                                txnId := "M61530894",
                                amount := 50000,
                                text := "Restaurant 2",
                                narration := "MMT/IMPS/009900356311/payment/SAILAKSHMI/HDFC0000847",
                                reference := "RFN02221651",
                                valueDate := to_datetime('2021-10-06' ++ 'T23:59:59+05:30'),
                                currentBalance := 165112,
                                transactionTimestamp := to_datetime('2021-10-06T23:14:28+05:30')
                            }),
                        }
                    }
                ),
                fi_term := (
                    INSERT TermDeposit {
                        maskedAccNumber := "XXXXXXX123",
                        ifsc := "APNB0001154",
                        branch := "Jayanagar 7th Block",
                        tenureDays := 180,
                        accountType := "FIXED",
                        currentValue := 11984590
                    }
                ),
                fi_recurring := (
                    INSERT RecurringDeposit {
                        maskedAccNumber := "XXXXXXX123",
                        ifsc := "APNB0001154",
                        branch := "Jayanagar 7th Block",
                        tenureDays := 180,
                        accountType := "FIXED",
                        currentValue := 11984590
                    }
                ),
                fi_credit_card := (
                    INSERT CreditCard {
                        maskedAccNumber := "XXXXXXX123",
                        dueDate := to_datetime('2021-10-20T13:20:00+05:30'),
                        cashLimit := 20000,
                        currentDue := 3000,
                        creditLimit := 60000,
                        totalDueAmount := 9756,
                        availableCredit := 51345,
                        previousDueAmount := 7654,
                        lastStatementDate := to_datetime('2021-05-20T13:20:00+05:30'),

                    }
                ),
                fi_insurance := (
                    INSERT Insurance {
                        maskedAccNumber := "XXXXXXX789",
                        coverType := "BUSINESS",
                        sumAssured := 1500000,
                        tenureYears := 12,
                        tenureMonths := 144,
                        policyEndDate := to_datetime('2030-05-20T13:20:00+05:30')

                    }
                )
            }
            """)


async def init_user(con: AsyncIOConnection, user_phone: str) -> None:
    async for tx in con.retrying_transaction():
        async with tx:
            await tx.execute(f"""INSERT User {{ phone := '{user_phone}' }}""")


async def init_user_company(con: AsyncIOConnection, user_phone: str, aa_disp_name: str, aa_phone: str) -> None:
    company_displ_name = aa_disp_name
    company_name = company_displ_name.lower().replace(" ", "")
    async for tx in con.retrying_transaction():
        async with tx:
            await tx.execute(f"""INSERT Company {{
                name := '{company_name}',
                display_name := '{company_displ_name}',
                aa := (SELECT AAAccount FILTER .phone = '{aa_phone}')
            }}""")
            await tx.execute(f"""UPDATE User FILTER .phone = '{user_phone}' SET {{
                 company += (SELECT Company FILTER .name = '{company_name}')
            }}""")


async def init_db(con: AsyncIOConnection) -> None:
    await create_aas(con)
    user_phone = "9999999999"
    await init_user(con, user_phone=user_phone)
    aa_accounts = {
        "9999999999": "Good Restaurant",
        "9999999998": "Bad Restaurant",
        "9999999997": "Wholesaler 1",
        "9999999996": "Wholesaler 2"
    }
    for key, value in aa_accounts.items():
        await init_user_company(con, user_phone=user_phone, aa_disp_name=value, aa_phone=key)


async def main() -> None:
    logger.info("INIT: Initializing service")
    con = await check_db()
    logger.info("INIT: Service finished initializing")
    if con:
        logger.info("INIT: Creating default data")
        await init_db(con)
        logger.info("INIT: Default data created")
        await con.aclose()

if __name__ == "__main__":
    asyncio.run(main())
