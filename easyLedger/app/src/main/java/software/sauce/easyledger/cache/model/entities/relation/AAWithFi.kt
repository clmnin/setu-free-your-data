package software.sauce.easyledger.cache.model.entities.relation

import androidx.room.Embedded
import androidx.room.Relation
import software.sauce.easyledger.cache.model.entities.AA.AAAccountEntity
import software.sauce.easyledger.cache.model.entities.AA.FiDeposit.FiDepositEntity
import software.sauce.easyledger.cache.model.entities.AA.FiTermEntity

data class AAWithFi(
    @Embedded
    val aa: AAAccountEntity,
    @Relation(
        entity = FiDepositEntity::class,
        parentColumn = "fi_deposit",
        entityColumn = "uuid"
    )
    val deposit: FiDepositWithTransactions?,
    @Relation(
        entity = FiTermEntity::class,
        parentColumn = "fi_term",
        entityColumn = "uuid"
    )
    val term: FiTermEntity?
)
