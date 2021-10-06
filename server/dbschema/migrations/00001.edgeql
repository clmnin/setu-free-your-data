CREATE MIGRATION m1lo32lm3yrrswzkwx5oo4spnlbots35yshqhy45guy7pnbmwrnjsa
    ONTO initial
{
  CREATE SCALAR TYPE default::TransactionType EXTENDING enum<DEBIT, CREDIT>;
  CREATE TYPE default::TransactionLine {
      CREATE REQUIRED PROPERTY amount -> std::int32;
      CREATE REQUIRED PROPERTY currentBalance -> std::int32;
      CREATE REQUIRED PROPERTY mode -> std::str;
      CREATE PROPERTY narration -> std::str;
      CREATE PROPERTY reference -> std::str;
      CREATE REQUIRED PROPERTY trans_type -> default::TransactionType;
      CREATE REQUIRED PROPERTY transactionTimestamp -> std::datetime;
      CREATE REQUIRED PROPERTY txnId -> std::str;
      CREATE REQUIRED PROPERTY valueDate -> std::datetime;
  };
  CREATE ABSTRACT TYPE default::FIBasic {
      CREATE MULTI LINK transactions -> default::TransactionLine;
      CREATE REQUIRED PROPERTY maskedAccNumber -> std::str;
  };
  CREATE TYPE default::AAAccount {
      CREATE MULTI LINK fi -> default::FIBasic;
      CREATE PROPERTY dob -> std::datetime;
      CREATE PROPERTY email -> std::str;
      CREATE PROPERTY name -> std::str;
      CREATE PROPERTY pan -> std::str;
      CREATE REQUIRED PROPERTY phone -> std::str;
  };
  CREATE TYPE default::User {
      CREATE MULTI LINK aa -> default::AAAccount;
      CREATE PROPERTY create_date -> std::datetime {
          SET default := (std::datetime_current());
      };
      CREATE PROPERTY is_active -> std::bool {
          SET default := true;
      };
      CREATE REQUIRED PROPERTY phone -> std::str {
          CREATE CONSTRAINT std::exclusive;
      };
      CREATE PROPERTY write_date -> std::datetime {
          SET default := (std::datetime_current());
      };
  };
  CREATE TYPE default::CreditCard EXTENDING default::FIBasic {
      CREATE REQUIRED PROPERTY availableCredit -> std::int32;
      CREATE REQUIRED PROPERTY cashLimit -> std::int32;
      CREATE REQUIRED PROPERTY creditLimit -> std::int32;
      CREATE REQUIRED PROPERTY currentDue -> std::int32;
      CREATE REQUIRED PROPERTY dueDate -> std::datetime;
      CREATE REQUIRED PROPERTY lastStatementDate -> std::datetime;
      CREATE REQUIRED PROPERTY previousDueAmount -> std::int32;
      CREATE REQUIRED PROPERTY totalDueAmount -> std::int32;
  };
  CREATE SCALAR TYPE default::DepositAccType EXTENDING enum<SAVINGS, CURRENT>;
  CREATE TYPE default::Deposit EXTENDING default::FIBasic {
      CREATE REQUIRED PROPERTY balanceDateTime -> std::datetime;
      CREATE REQUIRED PROPERTY branch -> std::str;
      CREATE REQUIRED PROPERTY currentBalance -> std::int32;
      CREATE REQUIRED PROPERTY currentODLimit -> std::int32 {
          SET default := 0;
      };
      CREATE REQUIRED PROPERTY drawingLimit -> std::int32 {
          SET default := 0;
      };
      CREATE REQUIRED PROPERTY ifscCode -> std::str;
      CREATE REQUIRED PROPERTY micrCode -> std::str;
      CREATE REQUIRED PROPERTY od_acc -> std::bool {
          SET default := false;
      };
      CREATE PROPERTY pending_amt -> std::int32 {
          SET default := 0;
      };
      CREATE REQUIRED PROPERTY status -> std::bool {
          SET default := true;
      };
      CREATE REQUIRED PROPERTY type_ -> default::DepositAccType {
          SET default := (default::DepositAccType.SAVINGS);
      };
  };
  CREATE ABSTRACT TYPE default::FDDeposit EXTENDING default::FIBasic {
      CREATE REQUIRED PROPERTY accountType -> std::str;
      CREATE REQUIRED PROPERTY branch -> std::str;
      CREATE REQUIRED PROPERTY currentValue -> std::int32;
      CREATE REQUIRED PROPERTY ifsc -> std::str;
      CREATE REQUIRED PROPERTY tenureDays -> std::int32;
  };
  CREATE TYPE default::RecurringDeposit EXTENDING default::FDDeposit;
  CREATE TYPE default::TermDeposit EXTENDING default::FDDeposit;
  CREATE TYPE default::Insurance EXTENDING default::FIBasic {
      CREATE REQUIRED PROPERTY policyEndDate -> std::datetime;
      CREATE REQUIRED PROPERTY sumAssured -> std::int32;
      CREATE REQUIRED PROPERTY tenureMonths -> std::int32;
      CREATE REQUIRED PROPERTY tenureYears -> std::int32;
  };
  CREATE SCALAR TYPE default::FiTypes EXTENDING enum<DEPOSIT, TERM_DEPOSIT, RECURRING_DEPOSIT, CREDIT_CARD, INSURANCE_POLICIES>;
};
