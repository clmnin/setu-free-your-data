package software.sauce.easyledger.cache.model.entities.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import software.sauce.easyledger.cache.model.entities.AA.FiDeposit.BankTransactionLineEntity
import software.sauce.easyledger.cache.model.entities.AA.FiDeposit.DepositTransactionCrossRef
import software.sauce.easyledger.cache.model.entities.AA.FiDeposit.FiDepositEntity

data class FiDepositWithTransactions(
    @Embedded
    val deposit: FiDepositEntity,

    @Relation(
        parentColumn = "uuid",
        entityColumn = "uuid",
        associateBy = Junction(
            value = DepositTransactionCrossRef::class,
            parentColumn = "deposit_uuid",
            entityColumn = "bank_trans_uuid"
        )
    )
    val transactions: List<BankTransactionLineEntity>
)