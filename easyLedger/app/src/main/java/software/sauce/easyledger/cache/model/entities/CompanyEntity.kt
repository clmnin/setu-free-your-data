package software.sauce.easyledger.cache.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import software.sauce.easyledger.cache.model.entities.AA.AAAccountEntity

@Entity(
    tableName = "companies",
    foreignKeys = [
        ForeignKey(
            entity = AAAccountEntity::class,
            parentColumns = ["uuid"],
            childColumns = ["aa_uuid"],
        ),
    ]
)
data class CompanyEntity(
    @PrimaryKey
    @ColumnInfo(name = "uuid")
    var uuid: String,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "display_name")
    var display_name: String,

    @ColumnInfo(name = "aa_uuid")
    var aaUUID: String?,
)
