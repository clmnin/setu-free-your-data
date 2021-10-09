package software.sauce.easyledger.cache.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "companies")
data class CompanyEntity(
    @PrimaryKey
    @ColumnInfo(name = "uuid")
    var uuid: String,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "display_name")
    var display_name: String,
)
