package software.sauce.easyledger.cache.model.entities.relation

import androidx.room.Embedded
import androidx.room.Relation
import software.sauce.easyledger.cache.model.entities.AA.AAAccountEntity
import software.sauce.easyledger.cache.model.entities.CompanyEntity
import software.sauce.easyledger.cache.model.entities.LedgerEntity

data class CompanyWithAA (
    @Embedded
    val company: CompanyEntity,
    @Relation(
        entity = AAAccountEntity::class,
        parentColumn = "aa_uuid",
        entityColumn = "uuid"
    )
    val aa: AAWithFi?,

    @Relation(
        entity = LedgerEntity::class,
        parentColumn = "uuid",
        entityColumn = "owner"
    )
    val ledger: List<LedgerEntity>?
)