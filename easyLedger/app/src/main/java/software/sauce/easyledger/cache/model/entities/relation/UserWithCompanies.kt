package software.sauce.easyledger.cache.model.entities.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import software.sauce.easyledger.cache.model.entities.CompanyEntity
import software.sauce.easyledger.cache.model.entities.UserCompanyCrossRef
import software.sauce.easyledger.cache.model.entities.UserEntity

data class UserWithCompanies(
    @Embedded
    val user: UserEntity,

    @Relation(
        parentColumn = "uuid",
        entityColumn = "uuid",
        associateBy = Junction(
            value = UserCompanyCrossRef::class,
            parentColumn = "user_uuid",
            entityColumn = "company_uuid"
        )
    )
    val companies: List<CompanyEntity>
)