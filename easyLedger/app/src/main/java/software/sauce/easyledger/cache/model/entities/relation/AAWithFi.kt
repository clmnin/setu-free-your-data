package software.sauce.easyledger.cache.model.entities.relation

import androidx.room.Embedded
import androidx.room.Relation
import software.sauce.easyledger.cache.model.entities.AA.AAAccountEntity
import software.sauce.easyledger.cache.model.entities.AA.FiDeposit.FiDepositEntity

data class AAWithFi(
    @Embedded
    val aa: AAAccountEntity,
    @Relation(
        entity = FiDepositEntity::class,
        parentColumn = "fi_deposit",
        entityColumn = "uuid"
    )
    val deposit: FiDepositWithTransactions?
)
