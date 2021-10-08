module default {
    scalar type LedgerGlobalNo extending sequence;
    scalar type FiTypes extending enum<DEPOSIT, TERM_DEPOSIT, RECURRING_DEPOSIT, CREDIT_CARD, INSURANCE_POLICIES>;
    scalar type DepositAccType extending enum<SAVINGS, CURRENT>;
    scalar type TransactionType extending enum<DEBIT, CREDIT>;
    abstract type FDDeposit {
        required property maskedAccNumber -> str;
        required property ifsc -> str;
        required property branch -> str;
        required property tenureDays -> int32;
        required property accountType -> str;
        required property currentValue -> int32;
    }

    # Main User account - not linked with AA
    # here a user can add multiple AA data into a single User account
    # and example is a son taking care of family's finances
    # or a SME with multiple businesses
    type User {
        required property phone -> str {
            constraint exclusive;
        };
        multi link company -> Company;
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

    type Company {
        required property name -> str {
            constraint exclusive;
            constraint max_len_value(100);
            constraint regexp('^[a-z0-9]+(?:-[a-z0-9]+)*$')
        };
        required property display_name -> str {
            constraint max_len_value(200);
        };
        property create_date -> datetime {
            default := (SELECT datetime_current());
        }
        property write_date -> datetime {
            default := (SELECT datetime_current());
        }
        # only one AA account per company in this build
        link aa -> AAAccount;
    }
    type AAAccount {
        required property phone -> str {
            constraint exclusive;
        };
        property name -> str;
        property email -> str;
        property dob -> datetime;
        property pan -> str;
        link fi_deposit ->  Deposit;
        link fi_term ->  TermDeposit;
        link fi_recurring ->  RecurringDeposit;
        link fi_credit_card ->  CreditCard;
        link fi_insurance ->  Insurance;
    }
    type Deposit {
        required property maskedAccNumber -> str;
        required property type_ -> DepositAccType { # SAVINGS, CURRENT
            default := DepositAccType.SAVINGS;
        }
        required property branch -> str;
        required property status -> bool { # ACTIVE, INACTIVE
            default := true;
        }
        property pending_amt -> int32 { # in case of an OD account
            default := 0;
        }
        required property ifscCode -> str;
        required property micrCode -> str;
        multi link transactions -> TransactionLine;
    }

    type TermDeposit extending FDDeposit;
    type RecurringDeposit extending FDDeposit;

    type CreditCard {
        required property maskedAccNumber -> str;
        required property dueDate -> datetime;
        required property cashLimit -> int32;
        required property currentDue -> int32;
        required property creditLimit -> int32;
        required property totalDueAmount -> int32;
        required property availableCredit -> int32;
        required property previousDueAmount -> int32;
        required property lastStatementDate -> datetime;
    }

    type Insurance {
        required property maskedAccNumber -> str;
        required property coverType -> str;
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
        property text -> str;
        property narration -> str;
        property reference -> str;
        required property valueDate -> datetime;
        required property currentBalance -> int32;
        required property transactionTimestamp -> datetime;
    }

    type Ledger {
        required link owner -> Company;
        required link party -> Company;
        property write_date -> datetime {
            default := datetime_current();
        }
        index on (.owner);
        index on (.party);
        constraint exclusive on ( (.owner, .party) );
    }
    type LedgerEntry {
        required property lid -> LedgerGlobalNo;
        required property type_ -> TransactionType;
        required property amt -> int32;
        required property bal -> int32;
        property write_date -> datetime {
            default := datetime_current();
        }
    }
}
