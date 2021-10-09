package software.sauce.easyledger.cache.model.mapper

import software.sauce.easyledger.cache.converter.DateTimeConverters
import software.sauce.easyledger.cache.model.entities.AA.AAAccountEntity
import software.sauce.easyledger.cache.model.entities.AA.FiDeposit.BankTransactionLineEntity
import software.sauce.easyledger.cache.model.entities.AA.FiDeposit.FiDepositEntity
import software.sauce.easyledger.cache.model.entities.AA.FiRecurringEntity
import software.sauce.easyledger.cache.model.entities.AA.FiTermEntity
import software.sauce.easyledger.cache.model.entities.LedgerEntity
import software.sauce.easyledger.network.model.AA.AAAccount
import software.sauce.easyledger.network.model.AA.BankTransactionLine
import software.sauce.easyledger.network.model.AA.FIDeposit
import software.sauce.easyledger.network.model.AA.FiFixedDeposit
import software.sauce.easyledger.network.model.Ledger

class AAMapper {
    fun mapAAToEntity(model: AAAccount, FiDepositUUID: String?, FiTermUUID: String?, FiRecurringUUID: String?): AAAccountEntity {
        return AAAccountEntity(
            uuid=model.uuid,
            phone=model.phone,
            name=model.name,
            email=model.email,
            dob=model.dob,
            pan=model.pan,
            FiDepositUUID=FiDepositUUID,
            FiTermUUID=FiTermUUID,
            FiRecurringUUID=FiRecurringUUID
        )
    }

    fun mapRecurringToEntity(model: FiFixedDeposit): FiRecurringEntity {
        return FiRecurringEntity(
            uuid=model.uuid,
            maskedAccNumber = model.maskedAccNumber,
            ifscCode = model.ifscCode,
            branch = model.branch,
            tenureDays = model.tenureDays,
            accountType = model.accountType,
            currentValue = model.currentValue
        )
    }

    fun mapTermToEntity(model: FiFixedDeposit): FiTermEntity {
        return FiTermEntity(
            uuid=model.uuid,
            maskedAccNumber = model.maskedAccNumber,
            ifscCode = model.ifscCode,
            branch = model.branch,
            tenureDays = model.tenureDays,
            accountType = model.accountType,
            currentValue = model.currentValue
        )
    }

    fun mapDepositToEntity(model: FIDeposit): FiDepositEntity {
        return FiDepositEntity(
            uuid=model.uuid,
            maskedAccNumber = model.maskedAccNumber,
            typeSavingOrCurrent = model.typeSavingOrCurrent,
            branch = model.branch,
            status = model.status,
            pendingAmount = model.pendingAmount,
            ifscCode = model.ifscCode,
            micrCode = model.micrCode
        )
    }

    fun mapBankTransactionToEntity(model: List<BankTransactionLine>): List<BankTransactionLineEntity> {
        return model.map {
            BankTransactionLineEntity(
                uuid=it.uuid,
                mode = it.mode,
                transaType = it.transaType,
                amount = it.amount,
                text = it.text,
                narration = it.narration,
                reference = it.reference,
                valueDate = it.valueDate,
                currentBalance = it.currentBalance,
                transactionTimestamp = DateTimeConverters.stringToDate(it.transactionTimestamp)
            )
        }
    }

    fun mapLedgerToEntity(model: List<Ledger>): List<LedgerEntity> {
        return model.map {
            LedgerEntity(
                uuid=it.uuid,
                ownerUUID = it.ownerUUID,
                partyUUID = it.partyUUID,
                type = it.type,
                narration = it.narration,
                amt = it.amt,
                bal = it.bal,
                writeDate = DateTimeConverters.stringToDate(it.writeDate)
            )
        }
    }
}