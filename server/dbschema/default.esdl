module default {
    scalar type FiTypes extending enum<DEPOSIT, TERM_DEPOSIT, RECURRING_DEPOSIT, CREDIT_CARD, INSURANCE_POLICIES>;
    scalar type DepositAccType extending enum<SAVINGS, CURRENT>;
    scalar type TransactionType extending enum<DEBIT, CREDIT>;

    # Main User account - not linked with AA
    # here a user can add multiple AA data into a single User account
    # and example is a son taking care of family's finances
    # or a SME with multiple businesses
    type User {
        required property phone -> str {
            constraint exclusive;
        };
        multi link aa -> AAAccount;
        property is_active -> bool {
            default := true;
        }
        property create_date -> datetime {
            default := datetime_current();
        }
        property write_date -> datetime {
            default := datetime_current();
        }
    }

    type AAAccount {
        required property phone -> str;
        property name -> str;
        property email -> str;
        property dob -> datetime;
        property pan -> str;
        # required property fi_types -> array(FiTypes); # array of FiTypes
        multi link fi ->  FIBasic;
    }

    abstract type FIBasic {
        required property maskedAccNumber -> str;
        multi link transactions -> TransactionLine;
    }

    abstract type FDDeposit extending FIBasic {
        required property ifsc -> str;
        required property branch -> str;
        required property tenureDays -> int32;
        required property accountType -> str;
        required property currentValue -> int32;
    }

    type Deposit extending FIBasic {
        required property type_ -> DepositAccType { # SAVINGS, CURRENT
            default := DepositAccType.SAVINGS;
        }
        required property branch -> str;
        required property status -> bool { # ACTIVE, INACTIVE
            default := true;
        }
        required property od_acc -> bool { # is it an OD account or not
            default := false;
        }
        property pending_amt -> int32 { # in case of an OD account
            default := 0;
        }
        required property ifscCode -> str;
        required property micrCode -> str;
        required property drawingLimit -> int32 {
            default := 0;
        }
        required property currentBalance -> int32;
        required property currentODLimit -> int32 {
            default := 0;
        }
        required property balanceDateTime -> datetime;
    }

    type TermDeposit extending FDDeposit;
    type RecurringDeposit extending FDDeposit;

    type CreditCard extending FIBasic {
        required property dueDate -> datetime;
        required property cashLimit -> int32;
        required property currentDue -> int32;
        required property creditLimit -> int32;
        required property totalDueAmount -> int32;
        required property availableCredit -> int32;
        required property previousDueAmount -> int32;
        required property lastStatementDate -> datetime;
    }

    # add insurance type too
    type Insurance extending FIBasic {
        required property sumAssured -> int32;
        required property tenureYears -> int32;
        required property tenureMonths -> int32;
        required property policyEndDate -> datetime;
    }

    type TransactionLine {
        required property mode -> str;
        required property trans_type -> TransactionType;
        required property txnId -> str;
        required property amount -> int32;
        property narration -> str;
        property reference -> str;
        required property valueDate -> datetime;
        required property currentBalance -> int32;
        required property transactionTimestamp -> datetime;
    }
}
