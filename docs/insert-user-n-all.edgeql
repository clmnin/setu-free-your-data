INSERT User {
    phone := "1234567890",
    aa := (
        INSERT AAAccount {
            phone := "1234567890", 
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
                    currentBalance := 10000,
                    balanceDateTime := to_datetime('2021-10-07T13:20:00+05:30'),
                    transactions := (INSERT TransactionLine {
                        mode := "FT",
                        trans_type := TransactionType.CREDIT,
                        txnId := "M61530891",
                        amount := 60000,
                        narration := "MMT/IMPS/009900356311/Swiggy/Swiggy/HDFC0000847",
                        reference := "RFN02209245",
                        valueDate := to_datetime('2021-09-19' ++ 'T23:59:59+05:30'),
                        currentBalance := 0,
                        transactionTimestamp := to_datetime('2021-09-24T23:09:17+05:30')
                    })
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
            )
        }
    )
};

SELECT User {
    phone,
    aa : {
        phone,
        name,
        fi_deposit: {
            type_,
            currentBalance,
            transactions : {
                mode,
                trans_type,
                amount
            }
        },
        fi_term : {
            tenureDays,
            currentValue
        }
    }
};