CREATE MIGRATION m132mvylapmydja2uarn5ocgums3hmylou6qroc7l556gehosvp4fq
    ONTO initial
{
  CREATE TYPE default::CreditCard {
      CREATE REQUIRED PROPERTY availableCredit -> std::int32;
      CREATE REQUIRED PROPERTY cashLimit -> std::int32;
      CREATE REQUIRED PROPERTY creditLimit -> std::int32;
      CREATE REQUIRED PROPERTY currentDue -> std::int32;
      CREATE REQUIRED PROPERTY dueDate -> std::datetime;
      CREATE REQUIRED PROPERTY lastStatementDate -> std::datetime;
      CREATE REQUIRED PROPERTY maskedAccNumber -> std::str;
      CREATE REQUIRED PROPERTY previousDueAmount -> std::int32;
      CREATE REQUIRED PROPERTY totalDueAmount -> std::int32;
  };
  CREATE SCALAR TYPE default::TransactionType EXTENDING enum<DEBIT, CREDIT>;
  CREATE TYPE default::TransactionLine {
      CREATE REQUIRED PROPERTY amount -> std::int32;
      CREATE REQUIRED PROPERTY currentBalance -> std::int32;
      CREATE REQUIRED PROPERTY mode -> std::str;
      CREATE PROPERTY narration -> std::str;
      CREATE PROPERTY reference -> std::str;
      CREATE PROPERTY text -> std::str;
      CREATE REQUIRED PROPERTY trans_type -> default::TransactionType;
      CREATE REQUIRED PROPERTY transactionTimestamp -> std::datetime;
      CREATE REQUIRED PROPERTY txnId -> std::str;
      CREATE REQUIRED PROPERTY valueDate -> std::datetime;
  };
  CREATE SCALAR TYPE default::DepositAccType EXTENDING enum<SAVINGS, CURRENT>;
  CREATE TYPE default::Deposit {
      CREATE MULTI LINK transactions -> default::TransactionLine;
      CREATE REQUIRED PROPERTY branch -> std::str;
      CREATE REQUIRED PROPERTY ifscCode -> std::str;
      CREATE REQUIRED PROPERTY maskedAccNumber -> std::str;
      CREATE REQUIRED PROPERTY micrCode -> std::str;
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
  CREATE TYPE default::Insurance {
      CREATE REQUIRED PROPERTY coverType -> std::str;
      CREATE REQUIRED PROPERTY maskedAccNumber -> std::str;
      CREATE REQUIRED PROPERTY policyEndDate -> std::datetime;
      CREATE REQUIRED PROPERTY sumAssured -> std::int32;
      CREATE REQUIRED PROPERTY tenureMonths -> std::int32;
      CREATE REQUIRED PROPERTY tenureYears -> std::int32;
  };
  CREATE ABSTRACT TYPE default::FDDeposit {
      CREATE REQUIRED PROPERTY accountType -> std::str;
      CREATE REQUIRED PROPERTY branch -> std::str;
      CREATE REQUIRED PROPERTY currentValue -> std::int32;
      CREATE REQUIRED PROPERTY ifsc -> std::str;
      CREATE REQUIRED PROPERTY maskedAccNumber -> std::str;
      CREATE REQUIRED PROPERTY tenureDays -> std::int32;
  };
  CREATE TYPE default::RecurringDeposit EXTENDING default::FDDeposit;
  CREATE TYPE default::TermDeposit EXTENDING default::FDDeposit;
  CREATE TYPE default::AAAccount {
      CREATE LINK fi_credit_card -> default::CreditCard;
      CREATE LINK fi_deposit -> default::Deposit;
      CREATE LINK fi_insurance -> default::Insurance;
      CREATE LINK fi_recurring -> default::RecurringDeposit;
      CREATE LINK fi_term -> default::TermDeposit;
      CREATE PROPERTY dob -> std::datetime;
      CREATE PROPERTY email -> std::str;
      CREATE PROPERTY name -> std::str;
      CREATE PROPERTY pan -> std::str;
      CREATE REQUIRED PROPERTY phone -> std::str {
          CREATE CONSTRAINT std::exclusive;
      };
  };
  CREATE TYPE default::Company {
      CREATE LINK aa -> default::AAAccount;
      CREATE PROPERTY create_date -> std::datetime {
          SET default := (SELECT
              std::datetime_current()
          );
      };
      CREATE REQUIRED PROPERTY display_name -> std::str {
          CREATE CONSTRAINT std::max_len_value(200);
      };
      CREATE REQUIRED PROPERTY name -> std::str {
          CREATE CONSTRAINT std::exclusive;
          CREATE CONSTRAINT std::max_len_value(100);
          CREATE CONSTRAINT std::regexp('^[a-z0-9]+(?:-[a-z0-9]+)*$');
      };
      CREATE PROPERTY write_date -> std::datetime {
          SET default := (SELECT
              std::datetime_current()
          );
      };
  };
  CREATE TYPE default::User {
      CREATE MULTI LINK company -> default::Company;
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
  CREATE SCALAR TYPE default::LedgerGlobalNo EXTENDING std::sequence;
  CREATE TYPE default::LedgerEntry {
      CREATE REQUIRED LINK owner -> default::Company;
      CREATE REQUIRED LINK party -> default::Company;
      CREATE CONSTRAINT std::exclusive ON ((.owner, .party));
      CREATE INDEX ON (.party);
      CREATE INDEX ON (.owner);
      CREATE REQUIRED PROPERTY amt -> std::int32;
      CREATE REQUIRED PROPERTY bal -> std::int32;
      CREATE REQUIRED PROPERTY lid -> default::LedgerGlobalNo;
      CREATE REQUIRED PROPERTY narration -> std::str;
      CREATE REQUIRED PROPERTY type_ -> default::TransactionType;
      CREATE PROPERTY write_date -> std::datetime {
          SET default := (std::datetime_current());
      };
  };
  CREATE SCALAR TYPE default::FiTypes EXTENDING enum<DEPOSIT, TERM_DEPOSIT, RECURRING_DEPOSIT, CREDIT_CARD, INSURANCE_POLICIES>;
};
