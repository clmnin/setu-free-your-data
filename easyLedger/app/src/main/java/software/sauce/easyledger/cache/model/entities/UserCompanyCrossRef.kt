package software.sauce.easyledger.cache.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index

@Entity(indices = [
    Index(value = ["company_uuid"]),
    Index(value = ["user_uuid"])
],
    primaryKeys = ["user_uuid", "company_uuid"])
data class UserCompanyCrossRef(

    @ColumnInfo(name = "user_uuid")
    val userUUID: String,

    @ColumnInfo(name = "company_uuid")
    val companyUUID: String,
)